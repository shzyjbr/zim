package com.zzk.common.util;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public final class HttpClient {

    private static MediaType mediaType = MediaType.parse("application/json");

    public static Response call(OkHttpClient okHttpClient, String params, String url) throws IOException {
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        return response;
    }

    public static Response callWithHeads(OkHttpClient okHttpClient, String url,
                                         String params, Map<String, String> map) throws IOException {
        RequestBody requestBody = RequestBody.create(mediaType, params);
        Request.Builder post = new Request.Builder()
                .url(url)
                .post(requestBody);
        for(Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            post.addHeader(key, value);
        }
        Request request = post.build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        return response;
    }
}

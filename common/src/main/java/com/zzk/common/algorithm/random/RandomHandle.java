package com.zzk.common.algorithm.random;

import com.zzk.common.algorithm.RouteHandle;
import com.zzk.common.enums.StatusEnum;
import com.zzk.common.exception.ZIMException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机路由
 */
public class RandomHandle implements RouteHandle {

    @Override
    public String routeServer(List<String> values, String key) {
        int size = values.size();
        if (size == 0) {
            throw new ZIMException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }
        int offset = ThreadLocalRandom.current().nextInt(size);

        return values.get(offset);
    }
}

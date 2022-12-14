package com.zzk.common.algorithm.loop;

import com.zzk.common.algorithm.RouteHandle;
import com.zzk.common.enums.StatusEnum;
import com.zzk.common.exception.ZIMException;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮询
 */
public class LoopHandle implements RouteHandle {
    private AtomicLong index = new AtomicLong();

    @Override
    public String routeServer(List<String> values,String key) {
        if (values.size() == 0) {
            throw new ZIMException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }
        Long position = index.incrementAndGet() % values.size();
        if (position < 0) {
            position = 0L;
        }

        return values.get(position.intValue());
    }
}

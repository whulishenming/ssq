package com.lsm.ssq.task;

import com.lsm.ssq.constant.RedisKeyConstant;
import com.lsm.ssq.plugins.RedisKit;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class SSQScheduled {
    @Autowired
    private RedisKit redisKit;

    @Test
    public void getLatestSSQ() {
        Date now = new Date();
        Integer currentYear = Integer.parseInt(DateFormatUtils.format(now, "yyyyMMdd").substring(2, 4));
        System.out.println(currentYear);
        if (redisKit.exists(RedisKeyConstant.SSQ_YEAR)) {

        }else{
            while (currentYear > Integer.parseInt(redisKit.get(RedisKeyConstant.SSQ_YEAR))) {

            }
        }
    }
}

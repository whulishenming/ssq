package com.lsm.ssq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lsm.ssq.constant.RedisKeyConstant;
import com.lsm.ssq.model.SSQHistoryRecords;
import com.lsm.ssq.plugins.RedisKit;
import com.lsm.ssq.repository.SSQHistoryRecordsRepository;
import com.lsm.ssq.service.ISSQService;
import com.lsm.ssq.utils.SSQSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class SSQServiceImpl implements ISSQService {
    @Autowired
    private RedisKit redisKit;
    @Autowired
    private SSQHistoryRecordsRepository ssqHistoryRecordsRepository;

    private static final String shtml = ".shtml";

    @Override
    @Transactional
    public boolean insert(String url, Integer periods) throws Exception{

        boolean insertFlag = false;

        if(redisKit.hExists(RedisKeyConstant.ALL_THE_SSQ, periods.toString())){
            return false;
        }

        if(periods<10000){
            url = url + "0" + periods + shtml;
        }else {
            url = url + periods + shtml;
        }

        //重试3次
        for (int j = 0; j < 3; j++) {
            SSQHistoryRecords record = null;
            try {
                record = SSQSpider.forEntityList(url, SSQHistoryRecords.class);
            } catch (org.jsoup.HttpStatusException e) {
                log.warn("url not found:{}", url);
                break;
            }
            if(record != null){

                redisKit.hSet(RedisKeyConstant.ALL_THE_SSQ, record.getPeriods().toString(), JSONObject.toJSONString(record));

                if (redisKit.exists(RedisKeyConstant.BLUE_BALL_STATISTICS)) {

                    redisKit.zIncrby(RedisKeyConstant.BLUE_BALL_STATISTICS, record.getFirstBlueBall().toString(), 1);
                    // 各个位置红球的统计
                    redisKit.zIncrby(RedisKeyConstant.FIRST_RED_BALL_STATISTICS, record.getFirstRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.SECOND_RED_BALL_STATISTICS, record.getSecondRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.THIRD_RED_BALL_STATISTICS, record.getThirdRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.fourth_red_ball_statistics, record.getFourthRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.FIFTH_RED_BALL_STATISTICS, record.getFifthRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.SIX_RED_BALL_STATISTICS, record.getSixthRedBall().toString(), 1);
                    // 所有红球的统计
                    redisKit.zIncrby(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getFirstRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getSecondRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getThirdRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getFourthRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getFifthRedBall().toString(), 1);
                    redisKit.zIncrby(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getSixthRedBall().toString(), 1);

                }else{
                    redisKit.zAdd(RedisKeyConstant.BLUE_BALL_STATISTICS, record.getFirstBlueBall().toString(), 1);

                    redisKit.zAdd(RedisKeyConstant.FIRST_RED_BALL_STATISTICS, record.getFirstRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.SECOND_RED_BALL_STATISTICS, record.getSecondRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.THIRD_RED_BALL_STATISTICS, record.getThirdRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.fourth_red_ball_statistics, record.getFourthRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.FIFTH_RED_BALL_STATISTICS, record.getFifthRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.SIX_RED_BALL_STATISTICS, record.getSixthRedBall().toString(), 1);

                    redisKit.zAdd(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getFirstRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getSecondRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getThirdRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getFourthRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getFifthRedBall().toString(), 1);
                    redisKit.zAdd(RedisKeyConstant.ALL_RED_BALL_STATISTICS, record.getSixthRedBall().toString(), 1);
                }

                Date now = new Date();
                record.setCreateTime(now);
                record.setUpdateTime(now);
                record.setIsDeleted((byte) 0);
                ssqHistoryRecordsRepository.save(record);

                //更新最大的
                if(redisKit.exists(RedisKeyConstant.PERIODS_NOW)){
                    Integer perviodsNow = Integer.parseInt(redisKit.get(RedisKeyConstant.PERIODS_NOW));
                    if(perviodsNow < periods){
                        redisKit.set(RedisKeyConstant.PERIODS_NOW, periods.toString());
                    }
                }else {
                    redisKit.set(RedisKeyConstant.PERIODS_NOW, periods.toString());
                }

                insertFlag = true;

                break;
            }
        }
        return insertFlag;
    }

}

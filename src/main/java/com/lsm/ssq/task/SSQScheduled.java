package com.lsm.ssq.task;

import com.alibaba.fastjson.JSONObject;
import com.lsm.ssq.constant.RedisKeyConstant;
import com.lsm.ssq.model.SSQHistoryRecords;
import com.lsm.ssq.plugins.MailKit;
import com.lsm.ssq.plugins.RedisKit;
import com.lsm.ssq.service.ISSQService;
import com.lsm.ssq.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
public class SSQScheduled {
    @Autowired
    private RedisKit redisKit;
    @Autowired
    private ISSQService ssqServiceImpl;
    @Autowired
    private MailKit mailKit;

    @Value("${doubleball.preurl}")
    private String url;

    public void getLatestSSQ() {
        if(redisKit.exists(RedisKeyConstant.NEXT_PERIOD)){
            Integer nextPeriod = Integer.parseInt(redisKit.get(RedisKeyConstant.NEXT_PERIOD));

            try {
                boolean isSuccess = ssqServiceImpl.insert(url, nextPeriod);
                if (isSuccess) {
                    SSQHistoryRecords latestRecord = JSONObject.parseObject(redisKit.hGet(RedisKeyConstant.ALL_THE_SSQ, nextPeriod.toString()), SSQHistoryRecords.class);
                    StringBuilder stringBuffer = new StringBuilder("");
                    stringBuffer.append(latestRecord.getFirstRedBall()).append("-")
                            .append(latestRecord.getSecondRedBall()).append("-")
                            .append(latestRecord.getThirdRedBall()).append("-")
                            .append(latestRecord.getFourthRedBall()).append("-")
                            .append(latestRecord.getFifthRedBall()).append("-")
                            .append(latestRecord.getSixthRedBall()).append("-")
                            .append(latestRecord.getFirstBlueBall());
                    // 预测下期开球，并发邮件
                    predictSSQ(nextPeriod.toString(), stringBuffer.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void predictSSQ(String periods, String latestRecord) {
        int[] balls = new int[34];
        StringBuilder stringBuffer = new StringBuilder(periods + "期开奖结果：" + latestRecord + System.getProperty("line.separator"));

        Integer firstBlueBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRange(RedisKeyConstant.BLUE_BALL_STATISTICS, 0, 5)));
        Integer firstRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.FIRST_RED_BALL_STATISTICS, 0, 4)));
        balls[firstRedBall] = 1;
        Integer secondRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.SECOND_RED_BALL_STATISTICS, 0, 4)));
        while (balls[secondRedBall] == 1) {
            secondRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.SECOND_RED_BALL_STATISTICS, 0, 4)));
        }
        balls[secondRedBall] = 1;
        Integer thirdRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.THIRD_RED_BALL_STATISTICS, 0, 4)));
        while (balls[thirdRedBall] == 1) {
            thirdRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.THIRD_RED_BALL_STATISTICS, 0, 4)));
        }
        balls[thirdRedBall] = 1;
        Integer fourthRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.fourth_red_ball_statistics, 0, 4)));
        while (balls[fourthRedBall] == 1) {
            fourthRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.fourth_red_ball_statistics, 0, 4)));
        }
        balls[fourthRedBall] = 1;
        Integer fifthRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.FIFTH_RED_BALL_STATISTICS, 0, 4)));
        while (balls[fifthRedBall] == 1) {
            fifthRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.FIFTH_RED_BALL_STATISTICS, 0, 4)));
        }
        balls[fifthRedBall] = 1;
        Integer sixthRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.SIX_RED_BALL_STATISTICS, 0, 4)));
        while (balls[sixthRedBall] == 1) {
            sixthRedBall = Integer.parseInt(RandomUtil.randomFromSet(redisKit.zRevRange(RedisKeyConstant.SIX_RED_BALL_STATISTICS, 0, 4)));
        }

        stringBuffer.append("下期预测结果")
                .append(firstRedBall).append("-")
                .append(secondRedBall).append("-")
                .append(thirdRedBall).append("-")
                .append(fourthRedBall).append("-")
                .append(fifthRedBall).append("-")
                .append(sixthRedBall).append("-")
                .append(firstBlueBall);

        mailKit.sendSimpleMail("lishenming8@126.com", "双色球预测", stringBuffer.toString());
        mailKit.sendSimpleMail("958653609@qq.com", "双色球预测", stringBuffer.toString());
    }
}

package com.lsm.ssq;

import com.alibaba.fastjson.JSONObject;
import com.lsm.ssq.constant.RedisKeyConstant;
import com.lsm.ssq.model.SSQHistoryRecords;
import com.lsm.ssq.plugins.MailKit;
import com.lsm.ssq.plugins.RedisKit;
import com.lsm.ssq.repository.SSQHistoryRecordsRepository;
import com.lsm.ssq.service.ISSQService;
import com.lsm.ssq.task.SSQScheduled;
import com.lsm.ssq.utils.RandomUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SsqApplicationTests {
	@Autowired
	private RedisKit redisKit;
	@Autowired
	private ISSQService ssqServiceImpl;
	@Autowired
	private MailKit mailKit;
	@Autowired
	private SSQScheduled ssqScheduled;
	@Autowired
	private SSQHistoryRecordsRepository ssqHistoryRecordsRepository;

	/*@Test
	public void contextLoads() {
		for (int i = 17; i <18 ; i++) {
			for (int j = 1; j < 108; j++) {
				try {
					ssqServiceImpl.insert("http://kaijiang.500.com/shtml/ssq/", i * 1000 + j);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}*/
/*
	@Test
	public void predictSSQ() {
		SSQHistoryRecords latestRecord = ssqHistoryRecordsRepository.findByPeriods(17108);

		StringBuilder stringBuffer = new StringBuilder("");
		stringBuffer.append(latestRecord.getFirstRedBall()).append("-")
				.append(latestRecord.getSecondRedBall()).append("-")
				.append(latestRecord.getThirdRedBall()).append("-")
				.append(latestRecord.getFourthRedBall()).append("-")
				.append(latestRecord.getFifthRedBall()).append("-")
				.append(latestRecord.getSixthRedBall()).append("-")
				.append(latestRecord.getFirstBlueBall());
		ssqScheduled.predictSSQ("17108", stringBuffer.toString());
	}*/

	/*@Test
	public void test() {
		for (int i = 0; i < 10000; i++) {
			if(redisKit.exists(RedisKeyConstant.NEXT_PERIOD)){
				Integer nextPeriod = Integer.parseInt(redisKit.get(RedisKeyConstant.NEXT_PERIOD));

				try {
					boolean isSuccess = ssqServiceImpl.insert("http://kaijiang.500.com/shtml/ssq/", nextPeriod);
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

		writeTxt(stringBuffer.toString());

	}

	private void writeTxt(String string){
		byte[] buff = new byte[] {};
		FileOutputStream fos = null;
		String filePath = "C:\\data\\ssq.txt";
		String str = string+System.getProperty("line.separator");
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			buff = str.getBytes();
			fos = new FileOutputStream(file, true);
			fos.write(buff);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}*/

}

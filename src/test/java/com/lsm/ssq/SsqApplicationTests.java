package com.lsm.ssq;

import com.alibaba.fastjson.JSONObject;
import com.lsm.ssq.constant.RedisKeyConstant;
import com.lsm.ssq.model.SSQHistoryRecords;
import com.lsm.ssq.plugins.MailKit;
import com.lsm.ssq.plugins.RedisKit;
import com.lsm.ssq.service.ISSQService;
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

	@Test
	public void contextLoads() {
		for (int i = 3; i <5 ; i++) {
			for (int j = 1; j < 184; j++) {
				try {
					ssqServiceImpl.insert("http://kaijiang.500.com/shtml/ssq/", i * 1000 + j);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void test() {
		for (int i = 0; i < 10000; i++) {
			if(redisKit.exists(RedisKeyConstant.PERIODS_NOW)){
				Integer needPeriods = Integer.parseInt(redisKit.get(RedisKeyConstant.PERIODS_NOW));

				Integer year = needPeriods/1000;

				Integer yearPeriod = needPeriods - year*1000;

				try {
					if (!ssqServiceImpl.insert("http://kaijiang.500.com/shtml/ssq/", year * 1000 + yearPeriod + 1)) {
						ssqServiceImpl.insert("http://kaijiang.500.com/shtml/ssq/",(year + 1) * 1000 + 1);
					}
				} catch (Exception e) {
					try {
						ssqServiceImpl.insert("http://kaijiang.500.com/shtml/ssq/",(year + 1) * 1000 + 1);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}

				Integer newPeriods = Integer.parseInt(redisKit.get(RedisKeyConstant.PERIODS_NOW));

				if (!needPeriods.equals(newPeriods)) {
					SSQHistoryRecords latestRecord = JSONObject.parseObject(redisKit.hGet(RedisKeyConstant.ALL_THE_SSQ, newPeriods.toString()), SSQHistoryRecords.class);
					StringBuilder stringBuffer = new StringBuilder("");
					stringBuffer.append(latestRecord.getFirstRedBall()).append("-")
							.append(latestRecord.getSecondRedBall()).append("-")
							.append(latestRecord.getThirdRedBall()).append("-")
							.append(latestRecord.getFourthRedBall()).append("-")
							.append(latestRecord.getFifthRedBall()).append("-")
							.append(latestRecord.getSixthRedBall()).append("-")
							.append(latestRecord.getFirstBlueBall());
					// 预测下期开球，并发邮件
					predictSSQ(newPeriods.toString(), stringBuffer.toString());
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
	}

}

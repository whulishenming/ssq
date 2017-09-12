package com.lsm.ssq.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_ssq_history_records")
@Data
public class SSQHistoryRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_red_ball")
    private Integer firstRedBall;

    @Column(name = "second_red_ball")
    private Integer secondRedBall;

    @Column(name = "third_red_ball")
    private Integer thirdRedBall;

    @Column(name = "fourth_red_ball")
    private Integer fourthRedBall;

    @Column(name = "fifth_red_ball")
    private Integer fifthRedBall;

    @Column(name = "six_red_ball")
    private Integer sixthRedBall;

    @Column(name = "first_blue_ball")
    private Integer firstBlueBall;

    @Column(name = "periods")
    private Integer periods;

    @Column(name = "next_period")
    private Integer nextPeriod;

    @Column(name = "lottery_date")
    private Date lotteryDate;

    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}

package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
@Slf4j
public class Mytask {
    @Scheduled(cron = "0/5 * * * * ?")
    public void excuteTask(){
        //System.out.println("自动任务执行{}", LocalDateTime.now());
        log.info("自动任务执行{}", LocalDateTime.now());
    }
}

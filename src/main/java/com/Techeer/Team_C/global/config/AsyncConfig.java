package com.Techeer.Team_C.global.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // 기본 실행 대기하는 Thread의 수
        executor.setMaxPoolSize(10);    // 동시 동작하는 최대 Thread의 수(큐가 다 차면)
        executor.setQueueCapacity(50);  // 대기 큐 최대 개수
        executor.setThreadNamePrefix("Mail-ASYNC-");
        executor.initialize();
        return executor;
    }
}

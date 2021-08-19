package hackathon.restart.computer.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ExecutorServiceConfig {
	
	@Bean(name = "threadPoolTaskExecutor")
	public Executor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor pool =  new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(3);
		pool.setMaxPoolSize(5);
		pool.setQueueCapacity(10);
		return pool;
	}

}

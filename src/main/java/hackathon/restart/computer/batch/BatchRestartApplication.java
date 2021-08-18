package hackathon.restart.computer.batch;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.thymeleaf.util.StringUtils;

import hackathon.restart.computer.batch.controller.BatchController;
import hackathon.restart.computer.batch.enums.ExitCode;

@SpringBootApplication(scanBasePackages = {"hackathon.restart.computer"})
@EntityScan("hackathon.restart.computer.entity")
public class BatchRestartApplication {
	
	public static void main(String[] args) {
		SimpleCommandLinePropertySource commandLineArgs = new SimpleCommandLinePropertySource(args);
		String batchName = commandLineArgs.getProperty("batchName");
		
		if (StringUtils.isEmpty(batchName)) {
			System.exit(ExitCode.ERROR.getCode());
		}
		
		int code = ExitCode.NORMAL.getCode();
		SpringApplication application = new SpringApplication(BatchRestartApplication.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		
		try (ConfigurableApplicationContext config = application.run(args)) {
			BatchController batchController = (BatchController) config.getBean(batchName);
			Class<?> targetClass = AopUtils.getTargetClass(batchController);
			code = batchController.run(commandLineArgs).getCode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			code = ExitCode.ERROR.getCode();
		}
		System.exit(code);
	}
}

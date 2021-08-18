package hackathon.restart.computer.batch.controller;

import org.springframework.core.env.SimpleCommandLinePropertySource;

import hackathon.restart.computer.batch.enums.ExitCode;

@FunctionalInterface
public interface BatchController {

	public ExitCode run (SimpleCommandLinePropertySource commandLineArgs) throws Exception;
}

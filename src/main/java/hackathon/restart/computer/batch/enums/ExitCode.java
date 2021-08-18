package hackathon.restart.computer.batch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExitCode {

	NORMAL(0),
	SUCCESS(10),
	WARNING(40),
	ERROR(90);
	
	private final int code;
}

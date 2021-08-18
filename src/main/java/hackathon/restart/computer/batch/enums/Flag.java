package hackathon.restart.computer.batch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Flag {

	OFF(0),
	ON(1);
	
	private final int code;
}

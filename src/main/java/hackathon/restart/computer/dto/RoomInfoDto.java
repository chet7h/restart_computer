package hackathon.restart.computer.dto;

import lombok.Data;

@Data
public class RoomInfoDto {
	private Integer id;
	private String roomName;
	private String linkCamera;
	private String token;
	private String bateryPercent;
	private String stopModeFlg;
	
}

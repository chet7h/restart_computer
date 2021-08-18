package hackathon.restart.computer.dto;

import hackathon.restart.computer.entity.RoomInfo;
import lombok.Data;

@Data
public class BatchRoomInfoDto extends RoomInfo {
	private String phone;
	
}

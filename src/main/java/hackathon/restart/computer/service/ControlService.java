package hackathon.restart.computer.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.entity.RoomInfo;

@Service
public class ControlService {
	@Autowired
	private RoomInfoService roomInfoService;

	public boolean isOverTime(int periodTime, LocalDateTime dateTimeTo, LocalDateTime dateTimeFrom) {
		return dateTimeTo.minusMinutes(periodTime).compareTo(dateTimeFrom) > 0;
	}
	
	public int getTypePower(int battery, int wifi) {
		int typePower = 0;
		
		System.out.println("battery : " + battery + " " + "wifi : " + wifi);
		if(battery < 20 || wifi < 20) {
			typePower = 4;
		} else if(battery < 40 || wifi < 40) {
			typePower = 3;
		} else if(battery < 80 || wifi < 80) {
			typePower = 2;
		} else {
			typePower = 1;
		}
		return typePower;
	}
	
	public boolean isStopMode(int deviceId) {
		RoomInfo roomInfo = roomInfoService.findById(deviceId).get();
		if(roomInfo.getStopModeFlg().equals("0")) {
			return false;
		}
		return true;
	}
}

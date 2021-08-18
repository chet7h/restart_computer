package hackathon.restart.computer.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.batch.enums.Flag;
import hackathon.restart.computer.batch.support.SMSActionSupport;
import hackathon.restart.computer.dao.RoomInfoReponsitory;
import hackathon.restart.computer.entity.RoomInfo;

@Service
public class RoomInfoService {

	@Autowired
	private RoomInfoReponsitory roomInfoReponsitory;
	@Autowired
	private SMSActionSupport smsActionSupport;

	public Optional<RoomInfo> findByToken(String token) {
		return roomInfoReponsitory.findByToken(token);
	}

	public Optional<RoomInfo> findById(Integer id) {
		return roomInfoReponsitory.findById(id);
	}

	public int updateTokenRoom(int roomId, String userUpdate, LocalDateTime updateTime) {
		return roomInfoReponsitory.updateTokenRoom(roomId, userUpdate, updateTime);
	}

	public List<RoomInfo> checkPinRobotAndSenMsg() throws IOException {
		List<RoomInfo> listRoomInfo = roomInfoReponsitory.findAll();
		List<RoomInfo> listRoomNomal = new ArrayList<>();

		for (RoomInfo roomInfo : listRoomInfo) {
			String phoneAdmin = roomInfoReponsitory.getPhoneAdminBayRoomID(roomInfo.getId());

			if (Integer.parseInt(roomInfo.getBateryPercent()) <= 5 && roomInfo.getStopModeFlg() == Flag.OFF) {
				roomInfo.setStopModeFlg(Flag.ON);
				smsActionSupport.sendSMS(phoneAdmin,
						"Low battery! " + roomInfo.getBateryPercent() + "%<br>Room name: " + roomInfo.getRoomName());
				roomInfo.setUpdate_by_user("batchRestartComputer");
				roomInfo.setUpdate_date(LocalDateTime.now());

			} else if (Integer.parseInt(roomInfo.getBateryPercent()) <= 20 && roomInfo.getStopModeFlg() == Flag.OFF
					&& roomInfo.getSendSmsFlg() == Flag.OFF) {
				smsActionSupport.sendSMS(phoneAdmin,
						"Low battery! " + roomInfo.getBateryPercent() + "%<br>Room name: " + roomInfo.getRoomName());
				roomInfo.setSendSmsFlg(Flag.ON);
				roomInfo.setUpdate_by_user("batchRestartComputer");
				roomInfo.setUpdate_date(LocalDateTime.now());

			} else if (Integer.parseInt(roomInfo.getBateryPercent()) >= 40 && roomInfo.getStopModeFlg() == Flag.ON) {
				roomInfo.setStopModeFlg(Flag.OFF);
				smsActionSupport.sendSMS(phoneAdmin,
						"Battery >= " + roomInfo.getBateryPercent() + "%<br>Room name: " + roomInfo.getRoomName());
				roomInfo.setSendSmsFlg(Flag.OFF);
				roomInfo.setUpdate_by_user("batchRestartComputer");
				roomInfo.setUpdate_date(LocalDateTime.now());

			} else {
				listRoomNomal.add(roomInfo);
			}
		}

		listRoomInfo.removeAll(listRoomNomal);
		return roomInfoReponsitory.saveAll(listRoomInfo);
	}

}

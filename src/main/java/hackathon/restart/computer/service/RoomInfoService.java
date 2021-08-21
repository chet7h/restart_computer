package hackathon.restart.computer.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.StringUtils;

import hackathon.restart.computer.dao.ListWattingUserRepository;
import hackathon.restart.computer.dao.RoomInfoReponsitory;
import hackathon.restart.computer.entity.RobotInfo;
import hackathon.restart.computer.entity.RoomInfo;
import hackathon.restart.computer.support.SMSActionSupport;

@Service
public class RoomInfoService {

	@Autowired
	private RoomInfoReponsitory roomInfoReponsitory;
	@Autowired
	private SMSActionSupport smsActionSupport;
	@Autowired
	private ListWattingUserRepository listwattinguserRepository;

	public Optional<RoomInfo> findByToken(String token) {
		return roomInfoReponsitory.findByToken(token);
	}

	public Optional<RoomInfo> findById(Integer id) {
		return roomInfoReponsitory.findById(id);
	}

	public int updateTokenRoom(int roomId, String userUpdate, LocalDateTime updateTime) {
		return roomInfoReponsitory.updateTokenRoom(roomId, userUpdate, updateTime);
	}

	@Async("threadPoolTaskExecutor")
	public void checkPinRobotAndSenMsg(RobotInfo robotInfo) throws IOException {
		RoomInfo roomInfo = roomInfoReponsitory.findById(robotInfo.getDeviceId()).get();
		// get phone admin of room
		String phoneAdmin;
		// get list phon user waiting
		List<String> listPhone;
		boolean isUpdateRoom = false;
		if (robotInfo.getBatteryPercentage() <= 5 && "0".equals(roomInfo.getStopModeFlg())) {
			phoneAdmin = roomInfoReponsitory.getPhoneAdminBayRoomID(roomInfo.getId());
			if(!StringUtils.isEmpty(phoneAdmin)) {
				smsActionSupport.sendSMS(phoneAdmin,
						"Low battery! " + robotInfo.getBatteryPercentage() + "%<br>Room name: " + roomInfo.getRoomName());
			}

			roomInfo.setStopModeFlg("1");
			roomInfo.setUpdate_by_user("batchRestartComputer");
			roomInfo.setUpdate_date(LocalDateTime.now());
			isUpdateRoom = true;

		} else if (robotInfo.getBatteryPercentage() <= 20 && "0".equals(roomInfo.getStopModeFlg())
				&& "0".equals(roomInfo.getSendSmsFlg())) {
			phoneAdmin = roomInfoReponsitory.getPhoneAdminBayRoomID(roomInfo.getId());
			if(!StringUtils.isEmpty(phoneAdmin)) {
				smsActionSupport.sendSMS(phoneAdmin,
						"Low battery! " + robotInfo.getBatteryPercentage() + "%<br>Room name: " + roomInfo.getRoomName());
				roomInfo.setSendSmsFlg("1");
				roomInfo.setUpdate_by_user("batchRestartComputer");
				roomInfo.setUpdate_date(LocalDateTime.now());
				isUpdateRoom = true;
			}

		} else if (robotInfo.getBatteryPercentage() >= 40 && "1".equals(roomInfo.getStopModeFlg())) {
			listPhone = listwattinguserRepository.getPhoneUserWailtingByRoomId(robotInfo.getDeviceId());
			// send sms for user waiting
			if (!ListUtils.isEmpty(listPhone)) {
				for (String phone : listPhone) {
					if(!StringUtils.isEmpty(phone)) {
						smsActionSupport.sendSMS(phone, "Robot is ready: Battery = " + robotInfo.getBatteryPercentage() + "%<br>Room name: "
								+ roomInfo.getRoomName());
					}
				}
			}

			roomInfo.setStopModeFlg("0");
			roomInfo.setSendSmsFlg("0");
			roomInfo.setUpdate_by_user("batchRestartComputer");
			roomInfo.setUpdate_date(LocalDateTime.now());
			isUpdateRoom = true;
		}

		if (isUpdateRoom) {
			roomInfoReponsitory.save(roomInfo);
		}
	}
	public int updateTokenRoom2(String token, String user_control, String user, LocalDateTime updateDate, int id) {
		return roomInfoReponsitory.updateTokenRoom2(token,user_control, user, updateDate,id);
	}

	public int updateFlgMode(String flg, int roomId, String userUpdate, LocalDateTime updateTime) {
		return roomInfoReponsitory.updateFlgMode(flg, roomId, userUpdate, updateTime);
	}
	
	public List<RoomInfo> findAllRoom(){
		return roomInfoReponsitory.findAll();
	}
}

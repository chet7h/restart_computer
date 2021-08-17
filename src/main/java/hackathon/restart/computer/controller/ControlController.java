package hackathon.restart.computer.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hackathon.restart.computer.controller.rest.RobotApiController;
import hackathon.restart.computer.entity.RobotInfo;
import hackathon.restart.computer.entity.RoomInfo;
import hackathon.restart.computer.entity.Users;
import hackathon.restart.computer.service.CustomUser;
import hackathon.restart.computer.service.RoomInfoService;

@Controller
@RequestMapping("control")
public class ControlController {

	@Autowired
	private RobotApiController robotApiController;
	@Autowired
	private RoomInfoService roomInfoService;
	
	int limitTime = 10;

	@GetMapping({ "/", "" })
	public String index(Model model, Principal principal) {
		CustomUser loginedUser = (CustomUser) ((Authentication) principal).getPrincipal();
		Users userInfo = loginedUser.getUsers();

		//3. Get information
		int roomId = Integer.parseInt(userInfo.getRoom_id());
		RoomInfo roomInfo = roomInfoService.findById(roomId).get();
		RobotInfo robotInfo = robotApiController.getRobotInfo(roomId, roomInfo.getToken());
		robotApiController.updateTokenToMemory(roomId, roomInfo.getToken());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("roomInfo", roomInfo);
		model.addAttribute("robotInfo", robotInfo);
		
		//4. Check is stop mode
		model.addAttribute("isStopMode", isStopMode(roomId));
		
		return "control";
	}
	
	@PostMapping("/updateWfAndBp")
	public String reloadInforRobot(@RequestParam String deviceId, @RequestParam String token,Model model) {

		RobotInfo robotInfo = robotApiController.getRobotInfo(Integer.parseInt(deviceId), token);

		model.addAttribute("robotInfo", robotInfo);
		model.addAttribute("isStopMode", isStopMode(Integer.parseInt(deviceId)));
		
		RoomInfo roomInfo = roomInfoService.findById(Integer.parseInt(deviceId)).get();
		model.addAttribute("isOverTime", isOverTime(10, LocalDateTime.now(), roomInfo.getUpdate_date()));
		return "control :: content2";
	}
	
	@PostMapping("/endControl")
	public String endControl(@RequestParam String deviceId) {
		
		roomInfoService.updateTokenRoom(Integer.parseInt(deviceId), "Page Control", LocalDateTime.now());
		return "redirect:/login";
	}
	
	private boolean isStopMode(int deviceId) {
		RoomInfo roomInfo = roomInfoService.findById(deviceId).get();
		if(roomInfo.getStopModeFlg().equals("0")) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param periodTime
	 * @param dateTimeTo currentime
	 * @param dateTimeFrom 
	 * @return
	 */
	private boolean isOverTime(int periodTime, LocalDateTime dateTimeTo, LocalDateTime dateTimeFrom) {
		return dateTimeTo.minusMinutes(periodTime).compareTo(dateTimeFrom) > 0;
	}
	

}

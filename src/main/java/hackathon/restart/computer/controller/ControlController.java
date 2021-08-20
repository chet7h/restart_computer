package hackathon.restart.computer.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hackathon.restart.computer.controller.rest.RobotApiController;
import hackathon.restart.computer.dto.RstListWattingUserDto;
import hackathon.restart.computer.entity.RobotInfo;
import hackathon.restart.computer.entity.RoomInfo;
import hackathon.restart.computer.entity.Users;
import hackathon.restart.computer.service.ControlService;
import hackathon.restart.computer.service.CustomUser;
import hackathon.restart.computer.service.ListWattingUserService;
import hackathon.restart.computer.service.RoomInfoService;
import hackathon.restart.computer.service.UsersService;

@Controller
@RequestMapping("control")
public class ControlController {

	@Autowired
	private RobotApiController robotApiController;
	@Autowired
	private RoomInfoService roomInfoService;
	@Autowired
	private ControlService controlService;
	
	@Autowired
	UsersService usersService;
	@Autowired
	ListWattingUserService listWattingUserService;

	@GetMapping({ "/", "" })
	public String index(Model model, Principal principal) {
		CustomUser loginedUser = (CustomUser) ((Authentication) principal).getPrincipal();
		Users userInfo = loginedUser.getUsers();

		//3. Get information
		int roomId = userInfo.getRoom_id();
		RoomInfo roomInfo = roomInfoService.findById(roomId).get();
		
		// Check access control
		if(!userInfo.getUsername().equals(roomInfo.getUpdate_by_user())) {
			return "redirect:/listWatingUser";
		}
		RobotInfo robotInfo = robotApiController.getRobotInfo(roomId, roomInfo.getToken());
		robotApiController.updateTokenToMemory(roomId, roomInfo.getToken());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("roomInfo", roomInfo);
		model.addAttribute("robotInfo", robotInfo);
		
		//4. Check is stop mode
		model.addAttribute("isStopMode", controlService.isStopMode(roomId));
		model.addAttribute("typePower", controlService.getTypePower(robotInfo.getBatteryPercentage(), robotInfo.getWifiSignal()));
		
		return "control";
	}
	
	@PostMapping("/updateWfAndBp")
	public String reloadInforRobot(@RequestParam String deviceId, @RequestParam String token,Model model) {

		RobotInfo robotInfo = robotApiController.getRobotInfo(Integer.parseInt(deviceId), token);
		
		model.addAttribute("typePower", controlService.getTypePower(robotInfo.getBatteryPercentage(), robotInfo.getWifiSignal()));
		model.addAttribute("robotInfo", robotInfo);
		model.addAttribute("isStopMode", controlService.isStopMode(Integer.parseInt(deviceId)));
		
		RoomInfo roomInfo = roomInfoService.findById(Integer.parseInt(deviceId)).get();
		model.addAttribute("isOverTime", controlService.isOverTime(1, LocalDateTime.now(), roomInfo.getUpdate_date()));
		
		// xu ly check pin robot va gui sms
		try {
			roomInfoService.checkPinRobotAndSenMsg(robotInfo);
		} catch (IOException e) {
			System.out.println("Send sms fail: " + e.getMessage());
		}
		
		return "control :: content2";
	}
	
	@PostMapping("/endControl")
	public String endControl(@RequestParam String deviceId) {
		//1. Update room
		roomInfoService.updateTokenRoom(Integer.parseInt(deviceId), "Page Control", LocalDateTime.now());
		List<RstListWattingUserDto> listUserWatting = usersService.listUserWattingByRoom(Integer.parseInt(deviceId));
		if(!listUserWatting.isEmpty()) {
			//updated user next
			listWattingUserService.updateFlagControl(listUserWatting.get(0).getId_watting());
			
		}
		
		return "redirect:/login";
	}

}

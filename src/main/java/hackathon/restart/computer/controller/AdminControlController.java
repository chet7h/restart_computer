package hackathon.restart.computer.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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
import hackathon.restart.computer.service.RoomInfoService;
import hackathon.restart.computer.service.UsersService;

@Controller
@RequestMapping("admin/control")
public class AdminControlController {

	@Autowired
	private RobotApiController robotApiController;
	@Autowired
	private RoomInfoService roomInfoService;
	@Autowired
	UsersService usersService;
	@Autowired
	private ControlService controlService;
	private static final String FLG_STOP = "1";
	private static final String FLG_UNSTOP = "0";

	@GetMapping({ "/", "" })
	public String index(Model model, Principal principal) {
		CustomUser loginedUser = (CustomUser) ((Authentication) principal).getPrincipal();
		Users userInfo = loginedUser.getUsers();

		//3. Get information
		int roomId = userInfo.getRoom_id();
		RoomInfo roomInfo = roomInfoService.findById(roomId).get();
		RobotInfo robotInfo = robotApiController.getRobotInfo(roomId, roomInfo.getToken());
		if(ObjectUtils.isEmpty(robotInfo)) {
			
		}
		robotApiController.updateTokenToMemory(roomId, roomInfo.getToken());

		// Get select
		List<RoomInfo> listRoomInfo = roomInfoService.findAllRoom();
		
		// List user waiting
		List<RstListWattingUserDto> listUserWatting = usersService.listUserWattingByRoom(roomId);
		
		model.addAttribute("listRoomInfo", listRoomInfo);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("roomInfo", roomInfo);
		model.addAttribute("robotInfo", robotInfo);
		model.addAttribute("listUserWatting", listUserWatting);
		model.addAttribute("isStopMode", roomInfo.getStopModeFlg());
		//4. Check is stop mode
		return "tabs";
	}
	
	@PostMapping("/updateWfAndBp")
	public String reloadInforRobot(@RequestParam String deviceId, @RequestParam String token,Model model) {

		RobotInfo robotInfo = robotApiController.getRobotInfo(Integer.parseInt(deviceId), token);
		
		model.addAttribute("typePower", controlService.getTypePower(robotInfo.getBatteryPercentage(), robotInfo.getWifiSignal()));
		model.addAttribute("robotInfo", robotInfo);
		model.addAttribute("isStopMode", controlService.isStopMode(Integer.parseInt(deviceId)));
		
		RoomInfo roomInfo = roomInfoService.findById(Integer.parseInt(deviceId)).get();
		model.addAttribute("isOverTime", controlService.isOverTime(10, LocalDateTime.now(), roomInfo.getUpdate_date()));
		return "tabs :: content2";
	}
	
	@PostMapping("/stopMode")
	public String stopMode(@RequestParam String deviceId, Model model) {

		roomInfoService.updateFlgMode(FLG_STOP, Integer.parseInt(deviceId), "Page Admin", LocalDateTime.now());
		model.addAttribute("isStopMode", FLG_STOP);
		return "tabs::settingFlgMode";
	}
	
	@PostMapping("/turnOnMode")
	public String turnOnMode(@RequestParam String deviceId, Model model) {

		roomInfoService.updateFlgMode(FLG_UNSTOP,Integer.parseInt(deviceId), "Page Admin", LocalDateTime.now());
		model.addAttribute("isStopMode", FLG_UNSTOP);
		return "tabs::settingFlgMode";
	}
	
	@PostMapping("/selectRoom")
	public String selectRoom(@RequestParam String roomId, Model model) {
		int idRoom = Integer.parseInt(roomId);
		RoomInfo roomInfo = roomInfoService.findById(idRoom).get();
		RobotInfo robotInfo = robotApiController.getRobotInfo(idRoom, roomInfo.getToken());
		
		robotApiController.updateTokenToMemory(idRoom, roomInfo.getToken());
		
		// List user waiting
		List<RstListWattingUserDto> listUserWatting = usersService.listUserWattingByRoom(idRoom);
		
		model.addAttribute("listUserWatting", listUserWatting);
		model.addAttribute("roomInfo", roomInfo);
		model.addAttribute("robotInfo", robotInfo);
		model.addAttribute("isStopMode", roomInfo.getStopModeFlg());
		return "tabs :: monitor";
	}
}

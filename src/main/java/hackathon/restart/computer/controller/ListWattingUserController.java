package hackathon.restart.computer.controller;

import java.security.Principal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hackathon.restart.computer.dto.RstListWattingUserDto;
import hackathon.restart.computer.entity.RoomInfo;
import hackathon.restart.computer.entity.Users;
import hackathon.restart.computer.service.CustomUser;
import hackathon.restart.computer.service.ListWattingUserService;
import hackathon.restart.computer.service.RoomInfoService;
import hackathon.restart.computer.service.UsersService;

@Controller
@RequestMapping("listWatingUser")
public class ListWattingUserController {
	private static final int TIMEWATTINGCONTROLL = 10;
	@Autowired
	UsersService usersService;
	@Autowired
	private RoomInfoService roomInfoService;
	
	@Autowired
	ListWattingUserService listWattingUserService;
	
	
	@RequestMapping(value = { "/", ""})
	public String listWatingUser(Model model, Principal principal) {
		CustomUser loginedUser = (CustomUser) ((Authentication) principal).getPrincipal();
		Users userInfo = loginedUser.getUsers();
		int roomId = userInfo.getRoom_id();
		RoomInfo roomInfo = roomInfoService.findById(roomId).get();
		// get list user watting
		List<RstListWattingUserDto> listUserWatting = usersService.listUserWattingByRoom(roomId);
		// time watting
		model.addAttribute("timeWatting", convertSecondTime(getTimeWatting(listUserWatting, userInfo)*TIMEWATTINGCONTROLL*60));
		model.addAttribute("flagControl", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listUserWatting", listUserWatting);
		model.addAttribute("roomInfo", roomInfo);
		// Check is stop mode
		model.addAttribute("isStopMode", isStopMode(roomId));
		return "listWatingUser";
	}
	@PostMapping("/updatedListWattingUser")
	public String updatedListWattingUser(Model model,Principal principal,@RequestParam String timeWatting) throws ParseException {
		//this.listWatingUser(model,principal);
		boolean flagControl =false;
		CustomUser loginedUser = (CustomUser) ((Authentication) principal).getPrincipal();
		Users userInfo = loginedUser.getUsers();
		int roomId = userInfo.getRoom_id();
		//List<Integer> listWattingUserByRoom = listWattingUserRepository.listWattingUserByRoom(roomId);
		RoomInfo roomInfo = roomInfoService.findById(roomId).get();
		List<RstListWattingUserDto> listUserWatting = usersService.listUserWattingByRoom(roomId);
		if(!listUserWatting.isEmpty()) {
			if(listUserWatting.get(0).getId().equals(userInfo.getId()) && listUserWatting.get(0).isFlagcontrol()){
				//deleted user này ra khỏi danh sách đợi
				listWattingUserService.deleteWattingUser(listUserWatting.get(0).getId_watting());
				
				//updated token of roominfo
				roomInfoService.updateTokenRoom2(UUID.randomUUID().toString(), userInfo.getUsername(), LocalDateTime.now(), roomId);
				flagControl = listUserWatting.get(0).isFlagcontrol();
			}
		}
		model.addAttribute("timeWatting", convertSecondTime(convertTime(timeWatting)));
		model.addAttribute("flagControl", flagControl);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listUserWatting", listUserWatting);
		model.addAttribute("roomInfo", roomInfo);
		// Check is stop mode
		model.addAttribute("isStopMode", isStopMode(roomId));
		return "listWatingUser :: content2";
	}
	private boolean isStopMode(int deviceId) {
		RoomInfo roomInfo = roomInfoService.findById(deviceId).get();
		if(roomInfo.getStopModeFlg().name().equals("OFF")) {
			return false;
		}
		return true;
	}
	private int getTimeWatting(List<RstListWattingUserDto> listUserWatting, Users userInfo) {
		int k= 0;
		for(int i = 0;i<listUserWatting.size();i++) {
			if(listUserWatting.get(i).getId() == userInfo.getId()) {
				k = i+1;
			}
		}
		return k;
	}
	private String convertSecondTime(int second) {
		Date d = new Date(second * 1000L);
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(d);
	}
	private int convertTime(String timeWatting) {
		if(!"00:00:00".equals(timeWatting)) {
			int hh= Integer.parseInt(timeWatting.split(":")[0]);
			int mm= Integer.parseInt(timeWatting.split(":")[1]);
			int ss= Integer.parseInt(timeWatting.split(":")[2]);
			return (hh*60*60 + mm*60 + ss) -3;
		}
		return 0;
		
	}

}

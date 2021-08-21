package hackathon.restart.computer.controller;

import java.security.Principal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.thymeleaf.util.StringUtils;

import hackathon.restart.computer.dto.RstListWattingUserDto;
import hackathon.restart.computer.entity.ListWattingUser;
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
		boolean flagControl = false;
		CustomUser loginedUser = (CustomUser) ((Authentication) principal).getPrincipal();
		Users userInfo = loginedUser.getUsers();
		int roomId = userInfo.getRoom_id();
		RoomInfo roomInfo = roomInfoService.findById(roomId).get();
		// get list user watting
		List<RstListWattingUserDto> listUserWatting = usersService.listUserWattingByRoom(roomId);
		if(listUserWatting.isEmpty() && StringUtils.isEmpty(roomInfo.getToken())) {
			//không có ai điều khiển
			//updated token of roominfo
			roomInfoService.updateTokenRoom2(UUID.randomUUID().toString(),userInfo.getUsername(), userInfo.getUsername(), LocalDateTime.now(), roomId);
			return "redirect:/control";
		}else {
			// check userLogin in list watting.
			if(!checkUserExitListWating(listUserWatting,userInfo)){
				ListWattingUser userWatting = new ListWattingUser();
				userWatting.setFlagcontrol(false);
				userWatting.setUser_id(userInfo.getId());
				userWatting.setCreate_by_user(userInfo.getUsername());
				userWatting.setCreate_date(LocalDate.now());
				userWatting.setRoom_id(roomId);
				// Add user in ListWatting
				listWattingUserService.insertWattingUser(userWatting);
			}
		}
		// re-get list watting 
		listUserWatting = usersService.listUserWattingByRoom(roomId);
		// time watting
		model.addAttribute("timeWatting", convertSecondTime(getTimeWatting(listUserWatting, userInfo)*TIMEWATTINGCONTROLL*60));
		model.addAttribute("flagControl", flagControl);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listUserWatting", listUserWatting);
		model.addAttribute("roomInfo", roomInfo);
		// Check is stop mode
		model.addAttribute("isStopMode", isStopMode(roomId));
		return "listWatingUser";
	}
	@PostMapping("/updatedListWattingUser")
	public String updatedListWattingUser(Model model,Principal principal,@RequestParam String timeWatting, boolean flagControl) throws ParseException {
		//this.listWatingUser(model,principal);
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
				roomInfoService.updateTokenRoom2(UUID.randomUUID().toString(),userInfo.getUsername(), userInfo.getUsername(), LocalDateTime.now(), roomId);
				flagControl = listUserWatting.get(0).isFlagcontrol();
			}
		} else {
			//roomInfoService.updateTokenRoom2(null, userInfo.getUsername(), LocalDateTime.now(), roomId);
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
	@PostMapping("/closeRoomWatting")
	public String closeRoomWatting(Model model,Principal principal) {
		//1. Remove userLogin ra khoi danh sach doi
		CustomUser loginedUser = (CustomUser) ((Authentication) principal).getPrincipal();
		Users userInfo = loginedUser.getUsers();
		listWattingUserService.deleteWattingUserLogin(userInfo.getId());
		return "redirect:/login";
	}
	private boolean checkUserExitListWating(List<RstListWattingUserDto> listUserWatting, Users userInfo) {
		List<Integer> listIdUserWatting = new ArrayList<>();
		for(RstListWattingUserDto listWattingUserDto: listUserWatting) {
			listIdUserWatting.add(listWattingUserDto.getId());
		}
		if(listIdUserWatting.contains(userInfo.getId())) {
			return true;
		}
		return false;
	}
	private boolean isStopMode(int deviceId) {
		RoomInfo roomInfo = roomInfoService.findById(deviceId).get();
		if("0".equals(roomInfo.getStopModeFlg())) {
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

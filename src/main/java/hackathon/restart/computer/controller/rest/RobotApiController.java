package hackathon.restart.computer.controller.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hackathon.restart.computer.entity.RobotInfo;
import hackathon.restart.computer.form.NavigateForm;

@RestController
@RequestMapping("robot")
public class RobotApiController {

	// biến toàn cục, dùng như table, lưu vào memory tăng tốc đọ truy cập
	private Map<Integer, RobotInfo> deviceStatus = new HashMap<>();
	
	private Map<Integer, String> tokenInfo = new HashMap<>();

	@PostConstruct
	private void initDeviceStatus() {
		deviceStatus.put(1, new RobotInfo(1, "1.0.0", 100, 100, 0, 0, "1111111111111111111"));// room 3.6
		deviceStatus.put(2, new RobotInfo(2, "1.0.0", 100, 100, 0, 0, "2222222222222222222"));// room 3.6
	}

	/**
	 * 
	 * return data: NavigateControl-ArmControl
	 * 
	 * 0: stop | 1: trái | 2: phải | 3: lùi | 4: tiến.
	 * 
	 * 0: đứng yên | 1: lên | 2: xuống
	 */
	@GetMapping({ "/", "" })
	public String navigate(@RequestParam int deviceId) {
		return deviceStatus.get(deviceId).getNavigateControl() + "-" + deviceStatus.get(deviceId).getArmControl();
	}

	/**
	 * 
	 * gửi từ page [control] qua bằng ajax.
	 * 
	 * deviceId = [room_info#id]
	 * 
	 * status = 0: stop | 1: trái | 2: phải | 3: lùi | 4: tiến.
	 * 
	 */
	@PostMapping("/setNavigate")
	public void setNavigate(@RequestBody NavigateForm navigateForm) {
		if (tokenInfo.get(navigateForm.getDeviceId()).equals(navigateForm.getToken())) {
			int navigateControl = navigateForm.getNavigateControl();
			Integer trueValue[] = { 0, 1, 2, 3, 4 };
			if (!Arrays.asList(trueValue).contains(navigateControl)) {
				navigateControl = 0;
			}

			int armControl = navigateForm.getArmControl();
			Integer trueValueArm[] = { 0, 1, 2 };
			if (!Arrays.asList(trueValueArm).contains(armControl)) {
				armControl = 0;
			}

			RobotInfo robotInfo = deviceStatus.get(navigateForm.getDeviceId());
			robotInfo.setNavigateControl(navigateControl);
			robotInfo.setArmControl(armControl);
		}
	}

	@GetMapping("/updateWfAndBp")
	public String updateWifiSignalBatteryPercentage(@RequestParam int deviceId, @RequestParam String token,
			@RequestParam int wifiSignal, @RequestParam int batteryPercentage) {
		if (deviceStatus.get(deviceId).getSecrect().equals(token)) {
			RobotInfo robotInfo = deviceStatus.get(deviceId);
			robotInfo.setWifiSignal(wifiSignal);
			robotInfo.setBatteryPercentage(batteryPercentage);
			return deviceStatus.get(deviceId).getNavigateControl() + "-" + deviceStatus.get(deviceId).getArmControl();
		}
		return "500";
	}
	
	public RobotInfo getRobotInfo(int deviceId, String token) {
		//if (deviceStatus.get(deviceId).getSecrect().equals(token)) {  --> gọi trược tiếp method nên không cần check token
			return deviceStatus.get(deviceId);
		//}
		//return null;
	}
	
	public String updateTokenToMemory(int roomId, String token) {
		return tokenInfo.put(roomId, token);
	}

}

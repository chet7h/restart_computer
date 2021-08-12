package hackathon.restart.computer.controller.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("robot")
public class RobotApiController {
	private Map<String, String> deviceStatus = new HashMap<>();

	@PostConstruct
	private void initDeviceStatus() {
		deviceStatus.put("1", "0");// room 3.6
		deviceStatus.put("2", "0");// room 3.7
	}

	/**
	 * 
	 * return data: 0: stop | 1: trái | 2: phải | 3: lùi | 4: tiến.
	 * 
	 */
	@GetMapping({ "/", "" })
	public String navigate(@RequestParam String deviceId) {
		return deviceStatus.get(deviceId);
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
	@GetMapping("/setNavigate")
	public String setNavigate(@RequestParam String deviceId, @RequestParam String status) {

		String trueValue[] = { "0", "1", "2", "3", "4" };
		if (!Arrays.asList(trueValue).contains(status)) {
			status = "0";
		}

		return deviceStatus.put(deviceId, status);
	}

}

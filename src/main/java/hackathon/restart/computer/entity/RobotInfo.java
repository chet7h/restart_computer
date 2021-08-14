package hackathon.restart.computer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RobotInfo {

	// nó tương ứng với table room_info#id
	private int deviceId;

	// version 1.0.0 dùng để update OTA, chỉ có ý nghĩa bên robot
	private String version;

	// đơn vị dBm
	private int WifiSignal;

	// đơn vị %
	private int batteryPercentage;

	// navigateControl = 0: stop | 1: trái | 2: phải | 3: lùi | 4: tiến.
	private int navigateControl;

	// armControl = 0: đứng yên | 1: lên | 2: xuống
	private int armControl;

	// token gắn trên robot
	private String token;

}

package hackathon.restart.computer.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "room_info")
@Data
public class RoomInfo {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "room_name")
	private String roomName;

	@Column(name = "link_camera")
	private String linkCamera;

	@Column(name = "token")
	private String token;

	@Column(name = "batery_percent")
	private String bateryPercent;
	
	@Column(name = "stop_mode_flg")
	private String stopModeFlg;
	
	@Column(name = "create_by_user")
	private String create_by_user;

	@Column(name = "update_by_user")
	private String update_by_user;

	@Column(name = "update_date")
	private LocalDateTime update_date;

	@Column(name = "create_date")
	private LocalDateTime create_date;
}

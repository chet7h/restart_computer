package hackathon.restart.computer.entity;

import lombok.Data;

@Data
public class UserInfo {
	private String id;
	private String username;
	private String email;
	private String phone;
	private String password;
	private Integer room_id;
	private String create_by_user;
	private String update_by_user;
	private String update_date;
	private String create_date;
	private String room_token;
	public UserInfo(String id, String username, Integer room_id, String room_token) {
		super();
		this.id = id;
		this.username = username;
		this.room_id = room_id;
		this.room_token = room_token;
	}
	
	
}

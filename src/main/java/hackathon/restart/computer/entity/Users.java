package hackathon.restart.computer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Users {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "room_id")
	private int room_id;

	@Column(name = "phone")
	private String phone;
	
	@Column(name = "avatar")
	private String avatar;
}

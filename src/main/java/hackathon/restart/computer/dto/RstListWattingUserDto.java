package hackathon.restart.computer.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import hackathon.restart.computer.entity.EnityCommon;
import lombok.Data;

@Entity
@Data
@lombok.EqualsAndHashCode(callSuper = false)
public class RstListWattingUserDto extends EnityCommon{
	
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
	
	@Column(name = "token_room")
	private String token_room;
	
	@Column(name = "flagcontrol")
	private boolean flagcontrol;
	
	@Column(name = "id_watting")
	private int id_watting;


	public RstListWattingUserDto(int id, String username, String email,String phone, int room_id, boolean flagcontrol,
			String token_room,int id_watting) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.room_id = room_id;
		this.flagcontrol = flagcontrol;
		this.token_room = token_room;
		this.id_watting = id_watting;
	}
	
	
	
}

package hackathon.restart.computer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "listwattinguser")
@Data
public class ListWattingUser extends EnityCommon{
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_id")
	private int user_id;

	@Column(name = "flagcontrol")
	private boolean flagcontrol;
	
	@Column(name = "room_id")
	private int room_id;
	
	@Column(name = "token_room")
	private String token_room;
}

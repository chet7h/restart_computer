package hackathon.restart.computer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user_role")
@Data
public class User_Role {
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "user_id")
	private Integer user_id;
	
	@Column(name = "role_id")
	private Integer role_id;
	
}

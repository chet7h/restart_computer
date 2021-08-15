package hackathon.restart.computer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class Role {
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "role_name")
	private String role_name;

}

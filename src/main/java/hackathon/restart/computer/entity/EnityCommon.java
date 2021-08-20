
package hackathon.restart.computer.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@MappedSuperclass
@Data
public class EnityCommon {

	@Column(name = "create_by_user")
	private String create_by_user;

	@Column(name = "update_by_user")
	private String update_by_user;

	@Column(name = "update_date")
	@UpdateTimestamp
	private LocalDate update_date;

	@Column(name = "create_date")
	@CreationTimestamp
	private LocalDate create_date;
}

package hackathon.restart.computer.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import hackathon.restart.computer.batch.enums.Flag;
import lombok.Data;

@Entity
@Table(name = "batch_master")
@Data
public class BatchMaster {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "batch_name")
	private String batchName;
	
	@Column(name = "stop_flg")
	private Flag stopFlg;
	
	@Column(name = "create_by_user")
	private String createByUser;

	@Column(name = "update_by_user")
	private String updateByUser;

	@Column(name = "update_date")
	private LocalDateTime updateDate;

	@Column(name = "create_date")
	private LocalDateTime createDate;
}

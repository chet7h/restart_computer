package hackathon.restart.computer.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hackathon.restart.computer.batch.enums.Flag;
import hackathon.restart.computer.entity.BatchMaster;


public interface BatchMasterTblReponsitory extends JpaRepository<BatchMaster, Integer>  {
	
	Optional<BatchMaster> findByBatchName(String batchName);
	
	@Query("Select b.stopFlg from BatchMaster b where b.batchName = ?1")
	Flag getFlgStopByBatchName(String batchName);
}

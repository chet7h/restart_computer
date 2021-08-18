package hackathon.restart.computer.batch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Controller;

import hackathon.restart.computer.batch.enums.ExitCode;
import hackathon.restart.computer.batch.enums.Flag;
import hackathon.restart.computer.dao.BatchMasterTblReponsitory;
import hackathon.restart.computer.service.RoomInfoService;

@Controller("batchRestartComputer")
public class BatchRestartComputerController implements BatchController{

	@Autowired
	private RoomInfoService roomInfoService;
	@Autowired
	private BatchMasterTblReponsitory batchMasterTblReponsitory;
	
	@Override
	public ExitCode run(SimpleCommandLinePropertySource commandLineArgs) throws Exception {
		while(true) {
			if(batchMasterTblReponsitory.getFlgStopByBatchName("batchRestartComputer") == Flag.ON) {
				break;
			}
			roomInfoService.checkPinRobotAndSenMsg();
			Thread.sleep(1000);
		}
		return ExitCode.NORMAL;
	}

	
}

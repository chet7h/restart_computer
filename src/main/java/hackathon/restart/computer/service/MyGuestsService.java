package hackathon.restart.computer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.dao.MyGuestsRepository;
import hackathon.restart.computer.entity.MyGuests;

@Service
public class MyGuestsService {

	@Autowired
	private MyGuestsRepository myGuestsRepository;

	public Optional<MyGuests> getInfo() {
		return myGuestsRepository.findById(1);
	}

}

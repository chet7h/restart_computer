package hackathon.restart.computer.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hackathon.restart.computer.entity.MyGuests;

public interface MyGuestsRepository extends JpaRepository<MyGuests, Integer> {
	Optional<MyGuests> findById(Integer id);

}

package hackathon.restart.computer.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hackathon.restart.computer.entity.MyGuests;

@Repository
@Transactional
public class MyGuestsDao {

	@Autowired
    private SessionFactory sessionFactory;

	public MyGuestsDao() {
	}

	public MyGuests findById(Integer id) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.get(MyGuests.class, id);
	}

}

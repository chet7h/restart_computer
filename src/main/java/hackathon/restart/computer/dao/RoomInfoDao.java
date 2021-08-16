package hackathon.restart.computer.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hackathon.restart.computer.entity.RoomInfo;

@Repository
@Transactional
public class RoomInfoDao {
	@Autowired
    private SessionFactory sessionFactory;

	public RoomInfoDao() {
	}

	public RoomInfo findByToken(String token) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.get(RoomInfo.class, token);
	}
}

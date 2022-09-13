package Siirex.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Siirex.entity.AppUser;

/** Hoặc sử dụng implemention của <JpaRepository> như ở Class AppUserRepoInterface?

	@Repository 
	public interface AppUserRepo extends JpaRepository<AppUser, Long> { 
	   Optional<AppUser> findUserByUsername(String username); 
	}
	
	// Thao tác này sẽ trả về thông tin chi tiết người dùng bao gồm userId, userName, 
	encryptPassword - như cách dùng với EntityManager ở dưới!!!

*/

@Repository
@Transactional
public class AppUserRepo {

	@Autowired
	private EntityManager entityManager;
	
	public AppUser findUserByUsernameOnDatabase(String username)
	{
		try {
			String hql = "SELECT e FROM " + AppUser.class.getName() + " AS e " 
			+ "WHERE e.userName = :name";
			
			Query query = this.entityManager.createQuery(hql, AppUser.class);
			query.setParameter("name", username);
			
			return (AppUser) query.getSingleResult();
			
		} catch (Exception e) {
			return null;
		}
	}
}

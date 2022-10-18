package Siirex.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Siirex.entity.GrantRole;
import Siirex.entity.UserAccounts;

@Repository
@Transactional
public class UserAccountsRepo {

	@Autowired
	private EntityManager entityManager;
	
	public List<UserAccounts> listUserAccount(Long roleId) {
		
		try {
			String hql = 
				"SELECT " 
				+ "NEW " + UserAccounts.class.getName() + "(e.appUser.userId, e.appUser.userName, e.appUser.encrytedPassword) "
				//+ "e.appUser.userId AS userId, "
				//+ "e.appUser.userName AS userName, "
				//+ "e.appUser.encrytedPassword AS encrytedPassword "
				+ "FROM " + GrantRole.class.getName() + " AS e " 
				+ "WHERE e.appRole.roleId = :id";
			
			//Query query = this.entityManager.createQuery(hql, UserAccounts.class);
			//query.setParameter("id", roleId);
			
			TypedQuery<UserAccounts> query2 = this.entityManager.createQuery(hql, UserAccounts.class);
			query2.setParameter("id", roleId);
			
			List<UserAccounts> data = query2.getResultList();
			
			System.out.println("QUERRY SELECT USER'S DATA BY ROLE_ID: OK!!!");
			return data;
			
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	// custom save()
	/*
	public void save(AppUser user) {
		if (user != null) {
			entityManager.merge(user);
		} else {
			entityManager.persist(user);
		}
	}
	*/
}

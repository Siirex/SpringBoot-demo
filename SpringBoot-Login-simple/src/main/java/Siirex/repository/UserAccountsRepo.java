package Siirex.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Siirex.entity.UserAccounts;
import Siirex.entity.UserRole;

@Repository
@Transactional
public class UserAccountsRepo {

	@Autowired
	private EntityManager entityManager;
	
	public List<UserAccounts> listUserAccount(Long roleUserId) {
		
		String hql = 
		"SELECT " 
		//+ "e.appUser.userId AS userId, "
		//+ "e.appRole.roleId AS roleId, "
		//+ "e.appRole.roleName AS roleName, "
		+ "e.appUser.userName AS userName, "
		+ "e.appUser.encrytedPassword AS encrytedPassword "
		+ "FROM " + UserRole.class.getName() + " AS e " 
		+ "WHERE e.appRole.roleId = :id";
		
		Query query = this.entityManager.createQuery(hql, UserAccounts.class);
		query.setParameter("id", roleUserId);
		
		@SuppressWarnings("unchecked")
		List<UserAccounts> data = query.getResultList();
		
		return data;
	}
}

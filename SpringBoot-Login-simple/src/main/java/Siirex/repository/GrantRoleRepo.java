package Siirex.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Siirex.entity.GrantRole;

@Repository
@Transactional
public class GrantRoleRepo {

	@Autowired
	EntityManager entityManager;
	
	public Long getMaxIdOfGrantRole() {
		
		try {
			
			String hql = "SELECT coalesce(max(g.id), 0) FROM " + GrantRole.class.getName() + " AS g";
			Query query = this.entityManager.createQuery(hql, Long.class);
			
			System.out.println("QUERRY SELECT GRANTROLE'S DATA: OK!!!");
			return (Long) query.getSingleResult();
			
		} catch (NoResultException e) {
			return 0L;
		}
	}
	
	public void SaveCustom(GrantRole role) {
		if (role != null) {
			entityManager.merge(role);
		} else {
			entityManager.persist(role);
		}
	}
}

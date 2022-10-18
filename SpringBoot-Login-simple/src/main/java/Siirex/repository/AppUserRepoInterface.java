package Siirex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import Siirex.entity.AppUser;

public interface AppUserRepoInterface extends JpaRepository<AppUser, Long> {

	@Query("SELECT coalesce(max(e.userId), 0) FROM AppUser AS e")
    Long getMaxIdOfAppUser();
	
	// Optional<AppUser> findUserByUsername(String username);
	
	/**
		- Optional được sử dụng để kiểm tra xem một biến có giá trị tồn tại giá trị hay không.
		- Giống như Collection và Array, Optional cũng là một vùng chứa chứa nhiều nhất một giá trị.
		- https://gpcoder.com/3945-optional-trong-java-8/
	*/
}

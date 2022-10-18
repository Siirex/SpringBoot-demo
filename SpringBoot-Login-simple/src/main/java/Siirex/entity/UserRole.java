package Siirex.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
	- Thằng này làm việc với User có thể chịu ủy quyền từ nhiều Role!
*/

@Data
@Entity
@Table(name = "USER_ROLE", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"USER_ID", "ROLE_ID"}, name = "USER_ROLE_UNIQUE")
})
public class UserRole {

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	private Long id;
	
	// Mỗi User có thể sẽ chịu nhiều nhiều Role
	// Giả sử, User có ROLE_ADMIN cũng có thể có quyền như ROLE_USER
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private AppUser appUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID", nullable = false)
	private AppRole appRole;
}

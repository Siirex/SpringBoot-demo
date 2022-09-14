package Siirex.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
	- Thằng này làm việc với User theo Role cụ thể!
*/

@Data
@Entity
@Table(name = "GRANT_ROLE", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"USER_ID", "ROLE_ID"}, name = "USER_ROLE_UNIQUE")
})
public class GrantRole {

	@Id
	// @GeneratedValue
	@Column(name = "ID", nullable = false)
	private Long id;
	
	// Mỗi User chỉ có duy nhất một 1 Role:
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private AppUser appUser;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID", nullable = false)
	private AppRole appRole;
}

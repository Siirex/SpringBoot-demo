package Siirex.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ACCOUNTS")
public class UserAccounts {

	@Id
	@GeneratedValue
	private Long id;
	
	// private Long userId;
	// private Long roleId;
	
	@Column(name = "ROLE_NAME", nullable = false)
	private Long roleName;
	
	@Column(name = "USER_NAME", length = 20, nullable = false)
	private String userName;
	
	@Column(name = "ENCRYTED_PASSWORD", length = 128, nullable = false)
	private String encrytedPassword;
}

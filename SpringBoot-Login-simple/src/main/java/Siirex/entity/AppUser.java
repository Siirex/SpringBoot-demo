package Siirex.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@Table(name = "APP_USER", uniqueConstraints = {
		// Đảm bảo rằng tất cả các giá trị trong Column 'USER_NAME' là khác nhau
		@UniqueConstraint(columnNames = "USER_NAME", name = "APP_USER_UNIQUE")
})
public class AppUser {

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", nullable = false)
	private Long userId;
	
	@Column(name = "USER_NAME", length = 20, nullable = false)
	private String userName;
	
	@Column(name = "ENCRYTED_PASSWORD", length = 128, nullable = false)
	private String encrytedPassword;
}

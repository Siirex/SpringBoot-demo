package Siirex.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@Table(name = "APP_ROLE", uniqueConstraints = {
		// Đảm bảo rằng tất cả các giá trị trong Column 'ROLE_NAME' là khác nhau
		@UniqueConstraint(columnNames = "ROLE_NAME", name = "APP_ROLE_UNIQUE")
})
public class AppRole {

	@Id
	@Column(name = "ROLE_ID", nullable = false)
	private Long roleId;
	
	@Column(name = "ROLE_NAME", length = 20, nullable = false)
	private String roleName;
}

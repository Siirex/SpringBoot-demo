package Siirex.utils;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class WebappUtils {

	/* Princial: admin1 (ROLE_USER, ROLE_ADMIN) */
	/* Princial: user1 (ROLE_USER) */
	
	public static String toString(User user) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Princial: ").append(user.getUsername());
		
		Collection<GrantedAuthority> authorities = user.getAuthorities();
		
		if (authorities != null && !authorities.isEmpty()) {
			sb.append(" (");
			boolean first = true;
			for(GrantedAuthority x : authorities) {
				if (first) {
					sb.append(x.getAuthority());
					first = false;
				} else {
					sb.append(", ").append(x.getAuthority());
				}
			}
			sb.append(")");
		}
		return sb.toString(); 
	}
}

package Siirex.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Siirex.entity.AppUser;
import Siirex.entity.UserAccounts;
import Siirex.repository.AppRoleRepo;
import Siirex.repository.AppUserRepo;
import Siirex.repository.AppUserRepoInterface;
import Siirex.repository.UserAccountsRepo;

/**
- Ngoài cách truy xuất user bằng username với cách @default spring security ở dưới, 
đó là sử dụng một implemention của <UserDetailsService>.
- Ta cũng có thể sử dụng ...?
*/

@Service
public class UserAccountsService implements UserDetailsService {

	/* ------------------------------------------- */
	/* Authentication & Authortization for Webapp */
	/* ----------------------------------------- */
	
	@Autowired
	private AppUserRepo userRepo;
	
	@Autowired
	private AppRoleRepo roleRepo;
	
	// @Autowired
	// private AppUserRepoInterface repositoryInterface; // same AppUserRepo
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AppUser user = this.userRepo.findUserByUsernameOnDatabase(username);
		// AppUser user = this.repositoryInterface.findUserByUsername(username);
		
		/**
		AppUser user = this.repositoryInterface.findUserByUsername(username)
			.orElseThrow(() -< new UsernameNotFoundException("User not present!"));
		*/
		if (user == null) {
            System.out.println("User '" + username + "' not found!");
            throw new UsernameNotFoundException("User " + username + " was not found in the database!");
		}
		
		// Show on console:
		// AppUser(userId=1, userName=siirex, encrytedPassword=$2a$10$.SpVyirwnaZjhlXB6OatuOG36JQMBTNnIwPZ7X1tgDkDVZzZm13Um, enabled=true)
		System.out.println("Found User: " + user);
		
		List<String> roleNameList = this.roleRepo.getRoleNameWithUserIdOnDatabase(user.getUserId());
		
		// Khai báo ArrayList chứa các Role (mà User được chỉ định có)
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>(); // [ROLE_USER, ROLE_ADMIN,..]
		
		if (roleNameList != null) {
			for (String x : roleNameList) {
				GrantedAuthority authority = new SimpleGrantedAuthority(x);
				grantList.add(authority);
			}
		}
		
		/**
		 * - Thông tin User được ủy quyền có trong DB - được truyền vào <UserDetails>
		 * - Từ đó Sping Security sẽ sử dụng đối tượng <UserDetails> này đối chiếu với thông 
		 * tin Client nhập vào khi Login - để hoàn thành việc xác thực (Authentication), 
		 * cũng như tiếp nhận luôn việc phân quyền (Authorization) thông qua các Role được
		 * truyền vào trong <User> bởi tham số 'grantList'!
		 */
		User userNeedToFind = new User(user.getUserName(), user.getEncrytedPassword(), grantList);
		UserDetails userDetails = userNeedToFind;
		return userDetails;
	}
	
	/* --------------------------- */
	/* List & Create new Accounts */
	/* ------------------------- */
	
	@Autowired
	private AppUserRepoInterface repositoryInterface;
	
	@Autowired
	private UserAccountsRepo userAccountRepo;
	
	// Get ID user max
	public Long getMaxIdOfAppUser() {
		Long idmax = repositoryInterface.getMaxIdOfAppUser();
		return idmax;
	}
	
	// List all accounts
	public List<AppUser> ListAllUserAccounts() {
		
		List<AppUser> listAllAcc = repositoryInterface.findAll();
		return listAllAcc;
	}
	
	// List accounts by Role
	public List<UserAccounts> ListUserAccounts(Long roleId) {
		
		List<UserAccounts> listAcc = this.userAccountRepo.listUserAccount(roleId);	
		return listAcc;
	}
	
	// Create new User account
	public void createAppUser(UserDetails newUser) {
		
		this.repositoryInterface.save((AppUser) newUser);
	}
	
	// Create new Role for new User account
}

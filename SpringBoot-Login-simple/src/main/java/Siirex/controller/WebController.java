package Siirex.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Siirex.entity.AppRole;
import Siirex.entity.AppUser;
import Siirex.entity.GrantRole;
import Siirex.entity.UserAccounts;
import Siirex.service.UserAccountsService;
import Siirex.utils.WebappUtils;

@Controller
public class WebController {

	String ower = "Siirex";

	@Autowired
	private UserAccountsService service;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	/* --------------------------------- */
	/* Controller request for Home page */
	/* ------------------------------- */
	
	@GetMapping(value = {"/"})
	public String Home(Model model) {
		model.addAttribute("message", "This is welcome page of " + ower + "!");
		
		return "welcomePage";
	}
	
	@GetMapping(value = "/login")
	public String loginPage(Model model) {
		
		AppUser defaultUser = service.getAppUserDefault("user1");
		System.out.println("Default user: " + defaultUser);
		model.addAttribute("defaultUsername", defaultUser.getUserName());
		model.addAttribute("defaultPassword", 123456);
		
		return "loginPage";
	}
	
	@GetMapping(value = "/logoutSuccessful")
	public String logoutSuccessfulPage() {
		
		return "logoutSuccessfulPage";
	}
	
	/* --------------------------------- */
	/* Controller request for user page */
	/* ------------------------------- */
	
	@GetMapping(value = "/userInfo")
	public String userInfoPage(Model model, Principal principal) {

		// List accounts with ROLE_USER (has id: 2L)
		List<UserAccounts> listUser = service.ListUserAccounts(2L);
		
		if (listUser != null) {
			model.addAttribute("members", listUser);

		} else {
			// List all accounts
			List<AppUser> listAllUser = service.ListAllUserAccounts();
			model.addAttribute("members", listAllUser);
		}
		
		// Lấy đối tượng User vừa Login thành công (thông qua đối tượng Principal đó)
		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		// Show thông tin User đó ra Page thông qua cấu hình WebUtils
		String userInfomations = WebappUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfomations);
		
		return "userInfoPage";
	}
	
	/* --------------------------------------- */
	/* Controller request for user admin page */
	/* ------------------------------------- */
	
	@GetMapping(value = "/adminInfo")
	public String adminInfoPage(Model model, Principal principal) {
		
		// List accounts with ROLE_ADMIN (has id: 1L)
		List<UserAccounts> listUser = service.ListUserAccounts(1L);
		
		if (listUser != null) {
			model.addAttribute("members", listUser);

		} else {
			// List all accounts
			List<AppUser> listAllUser = service.ListAllUserAccounts();
			model.addAttribute("members", listAllUser);
		}
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfomations = WebappUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfomations);
		
		return "adminInfoPage";
	}
	
	/* ------------------------------------------------- */
	/* Controller request for error page (acess denied) */
	/* ----------------------------------------------- */
	
	@GetMapping(value = "/403")
	public String accessDenied(Model model, Principal principal) {
		
		if (principal != null) {
			
			User loginedUser = (User) ((Authentication) principal).getPrincipal();
			
			String userInfomations = WebappUtils.toString(loginedUser);
			model.addAttribute("userInfo", userInfomations);
			
			// Message alarm
			String message = "Hi " + principal.getName() + "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);
		}
		
		return "403Page";
	}
	
	/* ---------------------------------------- */
	/* Controller request for create user page */
	/* -------------------------------------- */
	
	@GetMapping(value = "/createUserAccount")
	public String createAccount(Model mode, @RequestParam(name = "admin") Boolean admin) {
		
		mode.addAttribute("firstObject", new AppUser());
		
		if (admin == false) {
			return "createUserAccount";
		} else {
			return "createAdminAccount";
		}
	}

	@PostMapping(value = "/createUserAccount")
	public String addUserAccount(@ModelAttribute AppUser lastObject, @RequestParam(name = "admin") Boolean admin) {

		// Create new user
		AppUser newUser = new AppUser();
		newUser.setUserId(service.getMaxIdOfAppUser() + 1L);
		newUser.setUserName(lastObject.getUserName());
		newUser.setEncrytedPassword(passEncoder.encode(lastObject.getEncrytedPassword()));
		System.out.println("New user: " + newUser);
		
		// New record for AppUser entity / APP_USER table
		service.Save(newUser);
				
		// Grant fole for new User
		AppRole roleOfUser = null;
		
		if (admin == false) {
			roleOfUser = service.getRoleObjectByRoleName("ROLE_USER");
		} else {
			roleOfUser = service.getRoleObjectByRoleName("ROLE_ADMIN");
		}
		
		GrantRole role = new GrantRole();
		role.setId(service.getMaxIdOfGrantRole() + 1L);
		role.setAppUser(newUser);
		role.setAppRole(roleOfUser);
		System.out.println("Grant fole for new User: " + role);
		
		// New record for GrantRole entity / GRANT_ROLE table
		service.SaveRoleForNewUser(role);
		
		if (admin == false) {
			return "redirect:/userInfo";
		} else {
			return "redirect:/adminInfo";
		}
	}
}

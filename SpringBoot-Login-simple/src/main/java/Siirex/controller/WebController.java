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

import Siirex.entity.AppUser;
import Siirex.service.UserAccountsService;
import Siirex.utils.WebappUtils;

@Controller
public class WebController {

	String ower = "Siirex";

	@Autowired
	private UserAccountsService service;
	
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

		// List all accounts
		List<AppUser> listUser = service.ListAllUserAccounts();
		model.addAttribute("members", listUser);
		
		/** List accounts with ROLE_USER
		List<UserAccounts> listUser = userAccountsService.ListUserAccounts(2L); // roleId for user-account is 2L
		System.out.println("List User: " + listUser);
		model.addAttribute("members", listUser);
		*/
		
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
		
		/** List accounts with ROLE_ADMIN
		*/
		
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
	public String createAccount(Model mode) {
		
		mode.addAttribute("firstObject", new AppUser());
		return "createAccount";
	}
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	@PostMapping(value = "/createUserAccount")
	public void addUserAccount(@ModelAttribute AppUser lastObject) {
		AppUser newUser = new AppUser();
		newUser.setUserName(lastObject.getUserName());
		newUser.setEncrytedPassword(passEncoder.encode(lastObject.getEncrytedPassword()));
	}
}

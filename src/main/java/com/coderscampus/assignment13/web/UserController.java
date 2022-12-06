package com.coderscampus.assignment13.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	private Set<Account> accounts = new HashSet<Account>();

	@GetMapping("/register")
	public String getCreateUser(ModelMap model) {

		model.put("user", new User());

		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser(User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}

	@GetMapping("/users")
	public String getAllUsers(ModelMap model) {
		Set<User> users = userService.findAll();
		List<Account> allAccounts = userService.findAllAccounts();
		model.put("users", users);
		model.put("allAccounts", allAccounts);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}

		return "users";
	}

	@GetMapping("/users/{userId}")
	public String getOneUser(ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);

		Address address = userService.findAddressById(user);
		System.out.println(address);

		model.put("users", Arrays.asList(user));
		model.put("user", user);

		model.put("address", address);
		return "users";
	}

	@PostMapping("/users/{userId}")
	public String postOneUser(User user, Address address) {

		userService.saveUser(user, address);

		return "redirect:/users/" + user.getUserId();
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser(@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	// Account page
	
	
	@GetMapping("/users/{userId}/account")
	public String getCreateAccount(ModelMap model) {
		
		model.put("accounts", new Account());
		
		
		return "account";
	}

	
	
	@PostMapping("/users/{userId}/account")
	public String createAccount(Account account, @PathVariable Long userId) {
		User user = userService.findById(userId);
		Account savedAccount = userService.saveAccount(account);
		
		
		if(accounts == null) {
			
			accounts.add(account);
			user.setAccounts(accounts);
		}
		
		
			Set<Account> userAccounts = user.getAccounts();
			userAccounts.add(account);
		
			
//			user.getAccounts().stream().forEach(item -> System.out.println(item.getAccountName()));
			
			
			user.setAccounts(userAccounts);
	
		
		
		
			
		
		
		
		List<User> users = new ArrayList<>();
		users.add(user);
		
		
		account.setUsers(users);
//		savedAccount.getUsers().stream().forEach(item -> System.out.println(item.getName()));
		System.out.println(user);
	
		userService.saveUserAccount(user);
		
		
		return "redirect:/users/{userId}/accounts/{accountId}";
	}
	
	
	
	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getAccounts(ModelMap map, @PathVariable Long userId, @PathVariable Long accountId) {

		Account account = userService.findAccountByUserId(accountId);
		map.put("accounts", account);
		return "account";
	}
//
	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String createAccounts(@PathVariable Long userId, @PathVariable Long accountId, Account account) {

		User user = userService.findById(userId);
		Account savedAccount = userService.saveAccount(account);
		
		
		
		
			Set<Account> userAccounts = user.getAccounts();
			userAccounts.add(account);
		
			
//			user.getAccounts().stream().forEach(item -> System.out.println(item.getAccountName()));
			
			
			user.setAccounts(userAccounts);
	
		
		
		
			
		
		
		
		List<User> users = new ArrayList<>();
		users.add(user);
		
		
		account.setUsers(users);
//		savedAccount.getUsers().stream().forEach(item -> System.out.println(item.getName()));
		System.out.println(user);
	
		userService.saveUserAccount(user);

		return "redirect:/users/{userId}/accounts/{accountId}";
	}

}

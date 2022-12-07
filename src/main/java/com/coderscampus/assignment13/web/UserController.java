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

	private List<Account> accounts = new ArrayList<Account>();

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
		User foundUser = userService.findById(user.getUserId());
		user = foundUser;
		List<Account> accounts = foundUser.getAccounts();
		userService.saveUser(user, address);
		System.out.println(user);

		return "redirect:/users/" + user.getUserId();
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser(@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	// Account page

	@GetMapping("/users/{userId}/accounts")
	public String getCreateAccount(ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		List<Account> allAccounts = userService.findAllAccounts();

		Integer counter = 0;
		counter = user.getAccounts().size()+1;
		model.put("counter", counter);
		
		
		if(allAccounts.size() == 0) {
			
			model.put("accounts",new Account());
		} else {
			model.put("accounts", allAccounts.get(allAccounts.size()-1));
		}
		
		

		return "account";
	}
	//Create account
	@PostMapping("/users/{userId}/accounts")
	public String createAccount(Account account, @PathVariable Long userId) {
		User user = userService.findById(userId);
		Account savedAccount = userService.saveAccount(account);
		if (accounts == null) {
			accounts.add(account);
			user.setAccounts(accounts);
		}

		List<Account> userAccounts = user.getAccounts();
		
		userAccounts.add(account);

//			user.getAccounts().stream().forEach(item -> System.out.println(item.getAccountName()));

		user.setAccounts(userAccounts);

		List<User> users = new ArrayList<>();
		users.add(user);

		account.setUsers(users);
//		savedAccount.getUsers().stream().forEach(item -> System.out.println(item.getName()));
		System.out.println(user);
		userService.saveUserAccount(user);

		return "redirect:/users/{userId}/accounts";
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getAccounts(ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
		User user = userService.findById(userId);
		Integer counter = 0;
		counter = user.getAccounts().size();
		model.put("counter", counter);
		Account account = userService.findAccountByUserId(accountId);
		model.put("accounts", account);
	
		return "account";
	}

	// Modify Accounts
	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String createAccounts(ModelMap map, @PathVariable Long userId, @PathVariable Long accountId,
			Account account, Integer counter) {

		User user = userService.findById(userId);
		Account savedAccount = userService.saveAccount(account);
		List<Account> userAccounts = user.getAccounts();
		
		
//		userAccounts.add(account);

		user.setAccounts(userAccounts);

		List<User> users = new ArrayList<>();
		users.add(user);

		account.setUsers(users);

		userService.saveUserAccount(user);

		return "redirect:/users/{userId}/accounts/{accountId}";
	}

}

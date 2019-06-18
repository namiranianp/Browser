package com.web.FileExplorer.Controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.FileExplorer.dao.ViewingObjectHolderDAO;
import com.web.FileExplorer.dto.ViewingObjectHolder;
import com.web.FileExplorer.services.EncryptionService;

@Controller
public class AccountController {

	@Autowired
	private EncryptionService crypt;

	@Autowired
	private HashMap<String, ViewingObjectHolder> holders;

	@Autowired
	private HashMap<String, String> allUsers;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ViewingObjectHolderDAO VOHDAO;

	@PostMapping("/accountsettings")
	public String updateUser(Model model, @AuthenticationPrincipal User user,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "newName", required = false) String newName,
			@RequestParam(value = "newPass", required = false) String newPass,
			@RequestParam(value = "confNewPass", required = false) String confNewPass,
			@RequestParam(value = "encryptSave", required = true) boolean encryptSave,
			@RequestParam(value = "postLinks", required = true) boolean postLinks,
			@RequestParam(value = "encryptLinks", required = true) boolean encryptLinks) {

		String username = "base";
		
		if (user != null) {
			username = user.getUsername();
			// TODO check to see if boolean values are true and update them
		}
		
		username = username.substring(0, 1).toUpperCase() + username.substring(1);
		
		model.addAttribute("userName", username);
		model.addAttribute("userName", username);
		model.addAttribute("encryptSave", encryptSave);
		model.addAttribute("postLinks", postLinks);
		model.addAttribute("encryptLinks", encryptLinks);
		
		System.out.println();
		System.out.println();
		System.out.println(":" + password);
		System.out.println(":" + newName);
		System.out.println(":" + newPass);
		System.out.println(":" + confNewPass);
		System.out.println(":" + encryptSave);
		System.out.println(":" + postLinks);
		System.out.println(":" + encryptLinks);
		System.out.println();
		System.out.println();
		return "usersettings";
	}

	@GetMapping("/accountsettings")
	public String userSettings(Model model, @AuthenticationPrincipal User user) {
		String username = "base";
		boolean encryptSave = false;
		boolean postLinks = false;
		boolean encryptLinks = false;

		if (user != null) {
			username = user.getUsername();
			// TODO check to see if boolean values are true and update them
		}

		username = username.substring(0, 1).toUpperCase() + username.substring(1);
		
		model.addAttribute("userName", username);
		model.addAttribute("encryptSave", encryptSave);
		model.addAttribute("postLinks", postLinks);
		model.addAttribute("encryptLinks", encryptLinks);

		return "usersettings";
	}

	@RequestMapping("/loginsuccess")
	public String loadUser(Model model, @AuthenticationPrincipal User user) {
		if (user == null) {
			return "redirect:/?error";
		}

		if (!holders.containsKey(user.getUsername())) {
			String fileName = "./src/main/resources/users/" + user.getUsername() + ".txt";
			ViewingObjectHolder holder = VOHDAO.getHolderFromFormattedFile(fileName);
			holders.put(user.getUsername(), holder);
		}

		return "redirect:/";
	}

	@GetMapping(value = "/login")
	public String login(Model model) {
		return "login";
	}

	@PostMapping("/createaccount/**")
	public String createAccount(Model model, @RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password) {

		if (allUsers.containsKey(username)) {
			return "redirect:/login?same";
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		UserDetails user = new User(username, passwordEncoder.encode(password), authorities);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String fileString = username + ":" + user.getPassword() + "\n";
		allUsers.put(username, password);
		holders.put(username, new ViewingObjectHolder());
		try {
			Files.write(Paths.get("./src/main/resources/users/authentications.txt"), fileString.getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/saveUser/**")
	public String saveUser(Model model, @AuthenticationPrincipal User user) {
		String username = "base";
		String fileString;
		String fileName = "./src/main/resources/users/";
		ByteArrayOutputStream fileBytes = new ByteArrayOutputStream();

		if (user != null) {
			username = user.getUsername();
		}

		fileString = holders.get(username).toString();

		fileName += username + ".txt";

		try {
			fileBytes.writeBytes(fileString.getBytes(StandardCharsets.UTF_8));

			Files.deleteIfExists(Paths.get(fileName));
			Files.write(Paths.get(fileName), fileBytes.toByteArray(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "redirect:/?error";
		}

		return "redirect:/?saved";
	}
}

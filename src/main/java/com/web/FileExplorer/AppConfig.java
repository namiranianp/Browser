package com.web.FileExplorer;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import com.web.FileExplorer.dao.ViewingObjectHolderDAO;
import com.web.FileExplorer.dao.impl.ViewingObjectHolderDAOImpl;
import com.web.FileExplorer.dto.ViewingObjectHolder;

@Configuration
public class AppConfig {
	@Bean
	public ViewingObjectHolderDAO holderDAO() {
		return new ViewingObjectHolderDAOImpl();
	}

	@Bean
	@Autowired
	public HashMap<String, ViewingObjectHolder> getHolders(ViewingObjectHolderDAO holdMaker) {
		HashMap<String, ViewingObjectHolder> holders = new HashMap<>();
		ViewingObjectHolder holder = holdMaker.getHolderFromFormattedFile("./src/main/resources/users/base.txt");
		holders.put("base", holder);
		return holders;
	}

	@Bean
	public ArrayList<String> springUsers() {
		ArrayList<String> mutable = new ArrayList<String>();
		return mutable;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}
}

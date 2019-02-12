package com.web.FileExplorer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private HashMap<String, String> allUsers;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/seed/**", "/resources/**").permitAll().anyRequest().permitAll()
				.and().formLogin().loginPage("/login").permitAll().and().formLogin()
				.defaultSuccessUrl("/loginsuccess", true).and().logout().permitAll();

		http.csrf().disable();
	}

	@Override
	public void configure(AuthenticationManagerBuilder builder) throws Exception {
		String line;
		String[] pair;
		
		// TODO fix this monstrosity
		try (FileReader file = new FileReader("./src/main/resources/users/authentications.txt");
				BufferedReader buf = new BufferedReader(file);) {

			while ((line = buf.readLine()) != null) {
				pair = line.split(":");
				builder.inMemoryAuthentication().passwordEncoder(passwordEncoder).withUser(pair[0])
					.password(pair[1]).roles("USER");
				allUsers.put(pair[0], pair[1]);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
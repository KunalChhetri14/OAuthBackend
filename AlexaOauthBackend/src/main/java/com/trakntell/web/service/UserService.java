package com.trakntell.web.service;

import com.trakntell.web.models.MobileOrgVehicle;
import com.trakntell.web.repositories.MobileOrgVehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service(value = "userService")
public class UserService implements UserDetailsService {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private MobileOrgVehicleRepository mobileOrgVehicleRepo;

	//This method is internally called by userService to get Princripal and password
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOG.debug("loadUserByUsername, username = "+username);

		Optional<MobileOrgVehicle> optionalMobileOrgVehicle = mobileOrgVehicleRepo.findByMobileNum(username);

		if(optionalMobileOrgVehicle.isPresent()){
			return new org.springframework.security.core.userdetails.User(username, "", Collections.emptyList());
		}
		else {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
	}
}
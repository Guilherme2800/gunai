package com.gunai.service;

import org.springframework.stereotype.Service;

import com.gunai.model.User;
import com.gunai.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}
	
}

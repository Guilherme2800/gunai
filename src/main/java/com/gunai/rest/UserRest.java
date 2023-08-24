package com.gunai.rest;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.gunai.model.User;
import com.gunai.service.UserService;

import jakarta.validation.Valid;

@RestController("/user")
public class UserRest {

	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<?> create(@Valid User user, UriComponentsBuilder uriBuilder) {
		
		User userSave = userService.save(user);
		
		URI uri = uriBuilder.path("/user/{id}").buildAndExpand(userSave.getId()).toUri();
		
		return ResponseEntity.created(uri).body(null);
	}
	
}

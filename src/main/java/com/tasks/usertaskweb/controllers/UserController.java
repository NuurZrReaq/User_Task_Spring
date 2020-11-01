package com.tasks.usertaskweb.controllers;

import java.util.List;

import com.tasks.usertaskweb.exceptions.NotAuthorizedException;
import com.tasks.usertaskweb.services.JwtUtil;
import com.tasks.usertaskweb.services.MyUserDetailsService;
import com.tasks.usertaskweb.exceptions.UserControllerException;
import com.tasks.usertaskweb.exceptions.UserDeleteException;
import com.tasks.usertaskweb.exceptions.UserUpdateException;
import com.tasks.usertaskweb.models.AuthenticationRequest;
import com.tasks.usertaskweb.models.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.tasks.usertaskweb.models.User;
import com.tasks.usertaskweb.repositories.UserRepository;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

	@Qualifier("authenticationManagerBean")
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	MyUserDetailsService userDetailsService;

	@Qualifier("jwtUtilBean")
	@Autowired
	JwtUtil jwtToken;

	@Autowired
	UserRepository userRepository;

	static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<AuthenticationResponse> createAuthToken(
			@RequestBody AuthenticationRequest request ) throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
					(request.getId(), request.getPassword()));
			LOGGER.info("User with id = " +request.getId()+ " is authenticated successfully");

		} catch (BadCredentialsException e) {
			LOGGER.error("Bad credentials, couldn't authenticate user with id = "+request.getId());
			throw new Exception("Bad Credentials");
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(request.getId()));

		final String jwt = jwtToken.generateToken(userDetails);
		LOGGER.info("Generating JWT to be sent to the authenticated user");
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	@RequestMapping(method=RequestMethod.GET,value="/Users")
	@ResponseBody
	public List<User> getUsers (HttpServletResponse response) throws Exception {
		List<User> users = null;
		try {
			users = userRepository.getAllUsers();
			response.setStatus(HttpStatus.OK.value());
			response.setHeader("LOCATION","http://localhost/Users");
		} catch (Exception exception) {
			LOGGER.error("Can not get all users from database");
			throw new UserControllerException("Cannot get users");
		}
		LOGGER.info("List of user have been extracted from the database and sent back");
		return users;
	}

	@RequestMapping(method=RequestMethod.GET,value="/Users/{id}")
	@ResponseBody
	public User getUserById(@PathVariable int id,HttpServletResponse response,
							@RequestHeader("Authorization") String header) throws NotAuthorizedException, UserControllerException {
		User user = null;
		String jwt = header.substring(7);
		int userId = Integer.parseInt(jwtToken.extractUserId(jwt));
		LOGGER.info("userId extracted from the JWT in header to authorize the fetch process");
		try {
			if(userId != id){
				throw new NotAuthorizedException("");
			}
			user = userRepository.findById(id).get();
			response.setStatus(HttpStatus.OK.value());
			response.setHeader("LOCATION","http://localhost/Users/"+id);
		} catch (NotAuthorizedException exception){
			LOGGER.error("Unauthorized Process");
			throw new NotAuthorizedException("Unauthorized user");

		} catch (Exception exception){
			LOGGER.error("Can not get user with id = "+ id +" from database");
			throw new UserControllerException("Cannot find the user with id = " + id +" in database");
		}
		LOGGER.info(" User with id = "+ id +" got extracted from the Database");
		return user;
	}

	@RequestMapping(method=RequestMethod.POST, value="/Users", produces = "application/json")
	@ResponseBody
	public void insert(@RequestBody User user,HttpServletResponse response) throws UserUpdateException {

		try{
			if(user.getName().isBlank() || user.getName().isEmpty()){
				LOGGER.error("Username is either null or blank ");
				throw new Exception("Username is either blank or empty ");
			}
			else if(user.getEmail().isEmpty() || user.getEmail().isBlank()){
				LOGGER.error("User email is either null or blank ");
				throw new Exception("User email is either blank or empty ");
			}
			else if(user.getPassword().isBlank() || user.getPassword().isEmpty()){
				LOGGER.error("Password is either null or blank ");
				throw new Exception("Password is either blank or empty ");
			}
			else if(user.getAge() < 0){
				LOGGER.error("User age is invalid ");
				throw new Exception("User age is invalid///");
			}
			userRepository.save(user);
			response.setStatus(HttpStatus.CREATED.value());
			response.setHeader("LOCATION","http://localhost/Users");


		} catch (Exception exception){
			LOGGER.error("Insertion of the new user failed");
			throw new UserUpdateException(" Cannot insert user with id = "+
					user.getId()+ " to the database, "+ exception.getMessage() );
		}
		LOGGER.info("User with id = " + user.getId() + " is inserted successfully");
	}

	@RequestMapping(method=RequestMethod.PUT, value="/Users/{id}", produces = "application/json")
	@ResponseBody
	public void update(@RequestBody User user,@PathVariable int id,HttpServletResponse response,
					   @RequestHeader("Authorization") String header) throws UserControllerException, UserUpdateException {

		String jwt = header.substring(7);
		int userId = Integer.valueOf(jwtToken.extractUserId(jwt));
		User user_original = null ;
		try {
			if(userId != id ){
				throw new NotAuthorizedException("");
			}
			user_original = userRepository.findById(id).get();
			response.setStatus(HttpStatus.CREATED.value());
			response.setHeader("LOCATION","http://localhost/Users/"+id);

		} catch(NotAuthorizedException exception) {
			LOGGER.error("Update process is not authorized to user with id ="+ userId);

		}catch (Exception exception) {
			LOGGER.error("Can not find the user = "+id+" in the database");
			throw new UserControllerException("Cannot find the user with id = " + id +" in database");
		}
		LOGGER.info("User with Id = " + id+" is found in the database and is ready to be updated");
		user_original.setAge(user.getAge());
		user_original.setEmail(user.getEmail());
		user_original.setPassword(user.getPassword());
		user_original.setName(user.getName());
		try {
			if(user_original.getName().isBlank() || user_original.getName().isEmpty()){
				LOGGER.error("Username is either null or blank ");
				throw new Exception("Username is either blank or empty ");
			}
			else if(user_original.getEmail().isEmpty() || user_original.getEmail().isBlank()){
				LOGGER.error("User email is either null or blank ");
				throw new Exception("User email is either blank or empty ");
			}
			else if(user_original.getPassword().isBlank() || user_original.getPassword().isEmpty()){
				LOGGER.error("Password is either null or blank ");
				throw new Exception("Password is either blank or empty ");
			}
			else if(user_original.getAge() < 0){
				LOGGER.error("User age is invalid ");
				throw new Exception("User age is invalid///");
			}
			userRepository.save(user_original);
		} catch (Exception exception) {
			LOGGER.error("can not update the user "+id+" to the database");
			throw new UserUpdateException("Cannot update user with name = "+ user.getName() +" and id = "+user.getId()+" to the database");
		}
		LOGGER.info("User with id = "+id +" has been updated to the database successfully");
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/Users/{id}", produces = "application/json")
	@ResponseBody
	public void delete(@PathVariable int id,HttpServletResponse response,
					   @RequestHeader("Authorization") String header) throws NotAuthorizedException, UserDeleteException {

		String jwt = header.substring(7);
		int userId = Integer.valueOf(jwtToken.extractUserId(jwt));
		try {
			if (userId != id){
				LOGGER.error("User with id = "+userId+" is not authorized to delete user with id = "+id);
				throw new NotAuthorizedException("");
			}
			userRepository.deleteById(id);
			response.setStatus(HttpStatus.NO_CONTENT.value());
			response.setHeader("LOCATION","http://localhost/Users/"+id);

		} catch (NotAuthorizedException exception){
			throw new NotAuthorizedException("Unauthorized deletion process");

		} catch(Exception exception){
			LOGGER.error("Can not delete user with id = "+ id +" from the database");
			throw new UserDeleteException("Cannot Delete user with id= "+ id +" from the database") ;
		}
		LOGGER.info("User with id = "+id+" is deleted from the database successfully");
	}

}

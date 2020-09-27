package com.tasks.usertaskweb.Controllers;


import java.util.List;

import com.tasks.usertaskweb.Exceptions.UserControllerException;
import com.tasks.usertaskweb.Exceptions.UserDeleteException;
import com.tasks.usertaskweb.Exceptions.UserUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.tasks.usertaskweb.entities.User;
import com.tasks.usertaskweb.repos.UserRepository;

@RestController
public class UserController {
	
	@Autowired
	UserRepository userRepo;
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(method=RequestMethod.GET,value="/Users")

	public List<User> getUsers () throws Exception {
		List<User> users = null;
		try {
			users = userRepo.getAllUsers();
		} catch (Exception exception) {
			logger.error("Can not get all users from database");
			throw new UserControllerException("Cannot get users");
		}
		logger.info("Getting all users from Database");
		return users;

		

	}
	@RequestMapping(method=RequestMethod.GET,value="/Users/{id}")

	public User getUserById(@PathVariable int id) throws UserControllerException {
		User user = null;
		try {
			user = userRepo.findById(id).get();
		} catch (Exception exception){
			logger.error("Can not get user with id = "+ id +" from database");
			throw new UserControllerException("Cannot find the user with id = " + id +" in database");
		}
		logger.info("Getting task with id = "+ id +" from Database");
		return user;



		
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/Users", produces = "application/json")
	public void insert(@RequestBody User user) throws UserUpdateException {
		try{
			userRepo.save(user);
		} catch (Exception exception){
			logger.error("Insertion of the new user failed");
			throw new UserUpdateException("Cannot update user with name = "+ user.getName() +" and id = "+user.getId()+" to the database");
		}
		logger.info("User with id = " + user.getId() + " is inserted successfully");

	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/Users/{id}", produces = "application/json")
	public void update(@RequestBody User user,@PathVariable int id) throws UserControllerException, UserUpdateException {
		
		User user_original = null ;
		try {
			user_original = userRepo.findById(id).get();

		} catch (Exception exception) {
			logger.error("Can not find the user = "+id+" in the database");
			throw new UserControllerException("Cannot find the user with id = " + id +" in database");
		}
		logger.info("User with Id = " + id+" is found in the database and is ready to be updated");
		user_original.setAge(user.getAge());
		user_original.setEmail(user.getEmail());
		user_original.setPassword(user.getPassword());
		user_original.setName(user.getName());
		try {
			userRepo.save(user_original);
		} catch (Exception exception) {
			logger.error("can not update the user "+id+" to the database");
			throw new UserUpdateException("Cannot update user with name = "+ user.getName() +" and id = "+user.getId()+" to the database");
		}
		logger.info("User with id = "+id +" has been updated to the database successfully");
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/Users/{id}", produces = "application/json")
	public void delete(@PathVariable int id) throws UserDeleteException {

		try {
			userRepo.deleteById(id);
		} catch(Exception exception){
			logger.error("Can not delete user with id = "+ id +" from the database");
			throw new UserDeleteException("Cannot Delete user with id= "+ id +" from the database") ;
		}
		logger.info("User with id = "+id+" is deleted from the database successfully");
	}

}

package com.tasks.usertaskweb.controllers;


import java.util.*;
import services.JwtUtil;
import com.tasks.usertaskweb.exceptions.NotAuthorizedException;
import com.tasks.usertaskweb.exceptions.TaskDeleteException;
import com.tasks.usertaskweb.exceptions.TaskGettingException;
import com.tasks.usertaskweb.exceptions.TaskUpdateException;
import com.tasks.usertaskweb.models.User;
import com.tasks.usertaskweb.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.tasks.usertaskweb.models.Task;
import com.tasks.usertaskweb.repositories.TaskRepository;
import javax.servlet.http.HttpServletResponse;


@RestController
public class TaskController {

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserRepository userRepository;

	Logger logger = LoggerFactory.getLogger(TaskController.class);


	@RequestMapping(method=RequestMethod.GET,value="/Tasks")
	@ResponseBody
	public List<Task> getTasks (HttpServletResponse response,@RequestHeader("Authorization" )String header)
			throws TaskGettingException {

		List<Task> tasks = null;
		String jwt =header.substring(7);
		int userId = Integer.valueOf(jwtUtil.extractUserId(jwt));
		try{
			User user = userRepository.findById(userId).get();
			tasks = user.retrieveTasks();
			response.setStatus(HttpStatus.OK.value());
			response.setHeader("LOCATION","http://localhost/Tasks");
		} catch (Exception exception) {
			logger.error("Can not get all tasks from database");
			throw new TaskGettingException("Cannot read tasks");
		}
		logger.info("All tasks was received successfully");
		return tasks;
	}

	@RequestMapping(method=RequestMethod.GET,value="/Tasks/{id}")
	@ResponseBody
	public Task getTaskById(@PathVariable("id")  int id, HttpServletResponse response,
							@RequestHeader("Authorization") String header)
			throws TaskGettingException,NotAuthorizedException {

		Task task = null;
		String jwt = header.substring(7);
		int userId = Integer.valueOf(jwtUtil.extractUserId(jwt));


		try {
			task = taskRepository.getTaskById(id);
			if(task.getUser() == null) {
				throw new NotAuthorizedException("");
			}
			if(task != null) {
				if(userId != task.getUser().getId()){
					throw new NotAuthorizedException("");
				}
			}
			response.setHeader("LOCATION","http://localhost/Tasks/"+id);
			response.setStatus(HttpStatus.OK.value());

		}catch (NotAuthorizedException exception){
			logger.error("Unauthorized access");
				throw new NotAuthorizedException("Unauthorized Access");
		}
		catch(Exception exception) {
			logger.error("Can not get task with id = "+ id +" from database ");
			throw new TaskGettingException("Cannot get task with id "+ id +" from database because "
					+ exception.getMessage());
		}

		logger.info(" Task with id = "+ id +" got extracted from the Database");
		return task;
	}

	@RequestMapping(method=RequestMethod.POST, value="/Tasks", produces = "application/json")
	@ResponseBody
	public void insert(@RequestBody Task task,HttpServletResponse response,
					   @RequestHeader("Authorization") String header) throws TaskUpdateException,NotAuthorizedException {
		User user = null;
		String jwt = header.substring(7);
		int userId = Integer.valueOf(jwtUtil.extractUserId(jwt));
		try{

			user =userRepository.findById(userId).get();
			task.setUser(user);
			taskRepository.save(task);
			response.setHeader("LOCATION","http://localhost/Tasks");
			response.setStatus(HttpStatus.CREATED.value());
		} catch (Exception exception){
			logger.error("Insertion of the new task failed");
			throw new TaskUpdateException("can not insert task with id= " + task.getId()+ " into the database " +
					exception.getMessage());
		}
		logger.info("Task with id = " + task.getId() + " is inserted successfully");

	}

	@RequestMapping(method=RequestMethod.PUT, value="/Tasks/{id}", produces = "application/json")
	@ResponseBody
	public void update(@RequestBody Task task,@PathVariable ("id") int id,HttpServletResponse response,
					   @RequestHeader("Authorization") String header)
			throws TaskGettingException, TaskUpdateException, NotAuthorizedException {

		Task original_Task = null;
		try {
			original_Task = taskRepository.getTaskById(id);
			response.setHeader("LOCATION","http://localhost/Tasks/"+id);
			response.setStatus(HttpStatus.CREATED.value());

		} catch (Exception exception) {
			logger.error("Can not find the task = "+id+" in the database");
			throw new TaskGettingException("Cannot find task with id " + id + " in the database");
		}
		String jwt = header.substring(7);
		int userId = Integer.valueOf(jwtUtil.extractUserId(jwt));
		logger.info("Task with Id = " + id+" is found in the database and is ready to be updated");
		original_Task.setCompleted(task.isCompleted());
		original_Task.setDescription(task.getDescription());

		try{
			if(original_Task != null){
				if(original_Task.getUser() == null || userId != original_Task.getUser().getId()) {
					throw new NotAuthorizedException("");
				}
			}

			taskRepository.save(original_Task);
		}catch (NotAuthorizedException exception){
			logger.error("Unauthorized Access");
			throw new NotAuthorizedException("Unauthorized Access");
		}
		catch (Exception exception) {
			logger.error("can not update the task "+id+" to the database");
			throw new TaskUpdateException("Cannot update task with id "+ id + " into the database because "+
					exception.getMessage());
		}
		logger.info("Task with id = "+id +" is updated to the database successfully");
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/Tasks/{id}", produces = "application/json")
	@ResponseBody
	public void delete(HttpServletResponse response,@PathVariable("id")  int id,
					   @RequestHeader("Authorization") String header)
			throws TaskDeleteException, NotAuthorizedException {

		String jwt = header.substring(7);
		int userId = Integer.valueOf(jwtUtil.extractUserId(jwt));
		Task task = null;
		try {
			task = taskRepository.getTaskById(id);
			if (task.getUser() == null) {
				throw new NotAuthorizedException("");
			}
			if (task != null) {
				if (userId != task.getUser().getId()) {
					throw new NotAuthorizedException("");
				}
			}
		} catch(NotAuthorizedException exception){
			logger.error("Unauthorized Access");
			throw new NotAuthorizedException("Unauthorized Access");

		}
		catch (Exception exception){
		throw new TaskDeleteException(" Cannot delete task with id " +id+ " from the database because "+
					exception.getMessage());
		}
		try{
			taskRepository.deleteTaskById(id);
			response.setHeader("LOCATION","http://localhost/Tasks/"+id);
			response.setStatus(HttpStatus.NO_CONTENT.value());
		} catch (Exception exception) {
			logger.error("Can not delete task with id = "+ id +" from the database");
			throw new TaskDeleteException(" Cannot delete task with id " +id+ " from the database");
		}

		logger.info("Task with id = "+id+" is deleted from the database successfully");
	}

}

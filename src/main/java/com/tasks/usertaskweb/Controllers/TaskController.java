package com.tasks.usertaskweb.Controllers;


import java.util.List;
import com.tasks.usertaskweb.Exceptions.TaskDeleteException;
import com.tasks.usertaskweb.Exceptions.TaskGettingException;
import com.tasks.usertaskweb.Exceptions.TaskUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tasks.usertaskweb.entities.Task;
import com.tasks.usertaskweb.repos.TaskRepository;


@RestController
public class TaskController {
	
	@Autowired
	TaskRepository taskRepo;
	Logger logger = LoggerFactory.getLogger(TaskController.class);
	
	@RequestMapping(method=RequestMethod.GET,value="/Tasks")
	public List<Task> getTasks  () throws TaskGettingException {
		List<Task> tasks = null;
		try{
			tasks = taskRepo.getAllTasks();
		} catch (Exception exception) {
			logger.error("Can not get all tasks from database");
			throw new TaskGettingException("Cannot read tasks");
		}
		logger.info("Getting All tasks from Database");
		return tasks;
		
	}
	@RequestMapping(method=RequestMethod.GET,value="/Tasks/{id}")
	public Task getTaskById( @PathVariable("id")  int id) throws TaskGettingException {
		Task task = null;
		try {
			task = taskRepo.findById(id).get();

		} catch(Exception exception) {
			logger.error("Can not get task with id = "+ id +" from database");
			throw new TaskGettingException("Cannot get task with id "+ id +" from database" );
		}

		logger.info("Getting task with id = "+ id +" from Database");
		return task;
	}
	@RequestMapping(method=RequestMethod.POST, value="/Tasks", produces = "application/json")
	public void insert(@RequestBody Task task) throws TaskUpdateException {

		try{
			taskRepo.save(task);
		} catch (Exception exception){
			logger.error("Insertion of the new task failed");
			throw new TaskUpdateException("can not insert task with id= " + task.getId()+ " into the database");
		}
			logger.info("Task with id = " + task.getId() + " is inserted successfully");

	}

	@RequestMapping(method=RequestMethod.PUT, value="/Tasks/{id}", produces = "application/json")
	public void update(@RequestBody Task task,@PathVariable ("id") int id) throws TaskGettingException, TaskUpdateException {
		Task original_Task = null;
		try {
			original_Task = taskRepo.findById(id).get();
		} catch (Exception exception) {
			logger.error("Can not find the task = "+id+" in the database");
			throw new TaskGettingException("Connot find task with id " + id + " in the database");
		}
		logger.info("Task with Id = " + id+" is found in the database and is ready to be updated");
		original_Task.setCompleted(task.isCompleted());
		original_Task.setDescription(task.getDescription());
		original_Task.setUser(task.getUser());
		try{
			taskRepo.save(original_Task);
		} catch (Exception exception) {
			logger.error("can not update the task "+id+" to the database");
			throw new TaskUpdateException("Cannot update task with id "+ id + " into the database");
		}
		logger.info("Task with id = "+id +" is updated to the database successfully");

		//I can not think of it

	}

	@RequestMapping(method=RequestMethod.DELETE, value="/Tasks/{id}", produces = "application/json")
	public void delete(@PathVariable("id")  int id) throws TaskDeleteException {

		try{
			taskRepo.deleteById(id);
		} catch (Exception exception) {
			logger.error("Can not delete task with id = "+ id +" from the database");
			throw new TaskDeleteException(" Cannot delete task with id " +id+ " from the database");
		}

		logger.info("Task with id = "+id+" is deleted from the database successfully");
	}
	
	

}

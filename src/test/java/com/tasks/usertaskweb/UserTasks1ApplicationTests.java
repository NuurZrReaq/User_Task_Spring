package com.tasks.usertaskweb;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasks.usertaskweb.controllers.TaskController;
import com.tasks.usertaskweb.controllers.UserController;
import com.tasks.usertaskweb.models.Task;
import com.tasks.usertaskweb.models.User;
import com.tasks.usertaskweb.repositories.TaskRepository;
import com.tasks.usertaskweb.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.*;

@ContextConfiguration(classes=UserTasks1Application.class)
//@SpringBootTest

@ExtendWith(SpringExtension.class)
@WebMvcTest({UserController.class,TaskController.class})
class UserTasks1ApplicationTests {
	@Autowired
	private MockMvc mockMvc;


	User mockUser1 = new User(1,"Noor","noor.zreaq@gmail.com","ada",18,null);
	User mockUser2 = new User(2,"nooraldeen","nnnnn@nn.com","adsdasdsa",12,null);
	User user = new User(1,"nooraldeen","nnnnn@nn.com","adsdasdsa",12,null);

	List<User> mockUsers = new ArrayList<User>();
	Task mockTask = new Task (1,"description1",true,null);
	Task mockTask2 = new Task (2,"description2",true,null);
	Task task = new Task(1,"description2",true,null);

	List<Task> mockTasks = new ArrayList<Task>();



	@MockBean
	private UserRepository userRepository;
	@MockBean
	private TaskRepository taskRepository;
	@Autowired
	ObjectMapper objectMapper ;
	@Test
	void getUsersTest() throws Exception {

		mockUsers.add(mockUser1);
		mockUsers.add(mockUser2);


		Mockito.when(userRepository.getAllUsers()).thenReturn(mockUsers);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/Users").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{id:1,name:Noor,email:noor.zreaq@gmail.com,password:ada,age:18,tasks:null},"+
				"{id:2,name:nooraldeen,email:nnnnn@nn.com,password:adsdasdsa,age:12,tasks:null}]";
		JSONAssert.assertEquals(result.getResponse().getContentAsString(),expected,false);
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
		assertEquals("http://localhost/Users", result.getResponse().getHeader(HttpHeaders.LOCATION));

	}

	@Test
	void getUserByIdTest() throws Exception {
		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockUser1));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/Users/1").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{id:1,name:Noor,email:noor.zreaq@gmail.com,password:ada,age:18,tasks:null}";
		JSONAssert.assertEquals(result.getResponse().getContentAsString(),expected,false);
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
		assertEquals("http://localhost/Users/"+mockUser1.getId(),result.getResponse().getHeader(HttpHeaders.LOCATION));
	}

	@Test
	void insertUserTest() throws Exception {
		User mockUser = new User(4,"NoorZreaq","noor@yahoo.com","noor",23, null);
		String body = objectMapper.writeValueAsString(user);
		System.out.println(body);
		Mockito.when(
				userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);
		RequestBuilder requestBuilder =MockMvcRequestBuilders.post("/Users").accept(MediaType.APPLICATION_JSON).
				contentType(MediaType.APPLICATION_JSON).content(body).characterEncoding("utf-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(),response.getStatus());
		assertEquals("http://localhost/Users",response.getHeader(HttpHeaders.LOCATION));


	}

	@Test
	public void deleteUserTest() throws Exception{
		Mockito.doNothing().when(userRepository).deleteById(Mockito.anyInt());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/Users/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatus());
		assertEquals("http://localhost/Users/1",response.getHeader(HttpHeaders.LOCATION));

	}
	@Test
	public void updateUserTest() throws Exception{

		String body = objectMapper.writeValueAsString(user);
		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockUser1));
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		RequestBuilder requestBuilder =MockMvcRequestBuilders.put("/Users/1").
				accept(MediaType.APPLICATION_JSON).content(body).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(),response.getStatus());
		assertEquals("http://localhost/Users/"+mockUser1.getId(),response.getHeader(HttpHeaders.LOCATION));
	}

	@Test
	public void getTasksTest() throws Exception{
		mockTasks.add(mockTask);
		mockTasks.add(mockTask2);
		Mockito.when(taskRepository.getAllTasks()).thenReturn(mockTasks);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/Tasks").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{id:1,description:description1,completed:true,user:null},"+
				          "{id:2,description:description2,completed:true,user:null}]";
		JSONAssert.assertEquals(result.getResponse().getContentAsString(),expected,false);
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
		assertEquals("http://localhost/Tasks",result.getResponse().getHeader(HttpHeaders.LOCATION));

	}
	@Test
	public void getTaskByIDTest() throws Exception {
		Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockTask));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/Tasks/1").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{id:1,description:description1,completed:true,user:null}";
		JSONAssert.assertEquals(result.getResponse().getContentAsString(),expected,false);
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
		assertEquals("http://localhost/Tasks/"+mockTask.getId(),result.getResponse().getHeader(HttpHeaders.LOCATION));
	}
	@Test
	public void deleteTaskTest () throws Exception{
		Mockito.doNothing().when(taskRepository).deleteById(Mockito.anyInt());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/Tasks/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(),result.getResponse().getStatus());
		assertEquals("http://localhost/Tasks/1",result.getResponse().getHeader(HttpHeaders.LOCATION));
	}
	@Test
	public void updateTaskTest () throws Exception {
		String body = objectMapper.writeValueAsString(task);
		Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockTask));
		Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
		RequestBuilder requestBuilder =MockMvcRequestBuilders.put("/Tasks/1").
				accept(MediaType.APPLICATION_JSON).content(body).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(),response.getStatus());
		assertEquals("http://localhost/Tasks/1",response.getHeader(HttpHeaders.LOCATION));
	}

	@Test
	public  void insertTaskTest() throws Exception{
		Task mocktask1 = new Task (4,"description4",true,null);
		String body = objectMapper.writeValueAsString(mocktask1);
		System.out.println(body);
		Mockito.when(
				taskRepository.save(Mockito.any(Task.class))).thenReturn(mockTask);
		RequestBuilder requestBuilder =MockMvcRequestBuilders.post("/Tasks").content(body).accept(MediaType.APPLICATION_JSON).
				contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(),response.getStatus());
		assertEquals("http://localhost/Tasks",response.getHeader(HttpHeaders.LOCATION));
	}


}

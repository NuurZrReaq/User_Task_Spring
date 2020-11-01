package com.tasks.usertaskweb.models;

import java.util.List;

import javax.persistence.*;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String email;
	private String password;
	private int age;

	//Applying the one to many relation between the database objects.
	@OneToMany(mappedBy = "user",cascade=CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Task> tasks;

	public User(){
	}

	public User(int id, String name, String email, String password, int age, List<Task> tasks) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.age = age;
		this.tasks = tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Task> retrieveTasks(){
		return this.tasks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}



}

package com.tasks.usertaskweb.Models;

import java.util.Set;

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
	@OneToMany(mappedBy = "user",cascade=CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Set<Task> tasks;

	public User(){
	}
	public User(int id, String name, String email, String password, int age, Set<Task> tasks) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.age = age;
		this.tasks = tasks;
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

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

}

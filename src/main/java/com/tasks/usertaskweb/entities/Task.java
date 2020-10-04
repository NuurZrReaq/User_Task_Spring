package com.tasks.usertaskweb.entities;

import javax.persistence.*;


@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String description;
	private boolean completed;
	@ManyToOne(optional = false,fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	public Task(int id,String description,  boolean completed, User user) {
		this.description = description;
		this.completed = completed;
		this.user = user;
		this.id = id;
	}

	public Task() {
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

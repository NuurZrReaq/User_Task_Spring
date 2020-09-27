package com.tasks.usertaskweb.entities;

import javax.persistence.*;


@Entity
public class Task {

	private String description;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private boolean completed;
	@ManyToOne(optional = false,fetch = FetchType.EAGER)
	//@JoinColumn(foreignKey = @ForeignKey(name = "user_id"))
	@JoinColumn(name = "user_id")
	private User user;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

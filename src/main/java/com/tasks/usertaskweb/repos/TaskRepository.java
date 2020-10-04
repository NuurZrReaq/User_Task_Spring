package com.tasks.usertaskweb.repos;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tasks.usertaskweb.entities.Task;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface TaskRepository extends CrudRepository <Task, Integer> {

    @Query("from Task")
    public List<Task> getAllTasks();
    @Query("from Task where id=:id")
    public Task getTaskByID(@Param("id") int id);

}

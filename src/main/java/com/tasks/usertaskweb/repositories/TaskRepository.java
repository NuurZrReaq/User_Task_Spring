package com.tasks.usertaskweb.repositories;

import com.tasks.usertaskweb.models.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task,Integer> {

    @Query("from Task")
    public List<Task> getAllTasks();

    @Query("from Task where id=:id")
    public Task getTaskById(@Param("id") int id );

    @Modifying
    @Transactional
    @Query("delete from Task where id =:id")
    public void deleteTaskById(@Param("id") int id);

}

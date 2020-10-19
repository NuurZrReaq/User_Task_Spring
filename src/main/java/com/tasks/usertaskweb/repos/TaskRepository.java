package com.tasks.usertaskweb.repos;
import com.tasks.usertaskweb.Models.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task,Integer> {
    @Query("from Task")
    public List<Task> getAllTasks();
    @Query("from Task where id=:id")
    public Task getTaskById(@Param("id") int id );



    //public Task findByDescription();


}

package com.tasks.usertaskweb.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.tasks.usertaskweb.models.User;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("from User")
    public List<User> getAllUsers();

}

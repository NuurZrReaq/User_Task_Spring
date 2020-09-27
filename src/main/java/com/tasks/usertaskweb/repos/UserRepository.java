package com.tasks.usertaskweb.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tasks.usertaskweb.entities.User;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("from User")
    public List<User> getAllUsers();

    @Query("from User where name =:name")
    public User getUserByName(@Param("name") String name);
}

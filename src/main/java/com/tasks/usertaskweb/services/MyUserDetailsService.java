package com.tasks.usertaskweb.services;

import com.tasks.usertaskweb.configuration.MyUserDetails;
import com.tasks.usertaskweb.models.User;
import com.tasks.usertaskweb.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findById(Integer.valueOf(userId));

        user.orElseThrow(()-> new UsernameNotFoundException(userId + " Not found"));
        return new MyUserDetails(user.get());
    }
}

package Services;

import com.tasks.usertaskweb.Configuration.MyUserDetails;
import com.tasks.usertaskweb.Models.User;
import com.tasks.usertaskweb.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;


public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findById(Integer.valueOf(username));

        user.orElseThrow(()-> new UsernameNotFoundException(username + " Not found"));
        return new MyUserDetails(user.get());
    }
}

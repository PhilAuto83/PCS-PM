package com.phildev.pcs.config;

import com.phildev.pcs.domain.User;
import com.phildev.pcs.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            logger.error("No user found with name {} in database when calling custom user details service", username);
            throw new UsernameNotFoundException("No user found with name "+username);
        }
        logger.info("User {} retrieved from database by custom user details service", username);
        return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
                .password(user.getPassword()).roles(user.getRole()).build();
    }

}

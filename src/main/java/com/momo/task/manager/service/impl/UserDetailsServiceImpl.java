package com.momo.task.manager.service.impl;

import com.momo.task.manager.repository.SuperAdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    SuperAdminRepository superAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = superAdminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not Found"));

        // from security.core.userdetails package
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}

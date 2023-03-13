package com.example.msi.service.impl;

import com.example.msi.domains.MyUserDetails;
import com.example.msi.domains.User;
import com.example.msi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private UserRepository repository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.repository = userRepository;
  }

  public UserDetailsServiceImpl() {
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    User user = repository.getUserByEmail(s);
    if (user == null) {
      throw new UsernameNotFoundException("Could not find user");
    }
    return new MyUserDetails(user);
  }
}

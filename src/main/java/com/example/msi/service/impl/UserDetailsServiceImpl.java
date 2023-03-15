package com.example.msi.service.impl;

import com.example.msi.domains.MyUserDetails;
import com.example.msi.domains.User;
import com.example.msi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    Optional<User> userOptional = Optional.ofNullable(repository.getUserByEmail(s));
    return userOptional.map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng này"));
  }
}

package com.example.msi.security;

import com.example.msi.security.jwt.JwtAuthenticationFilter;
import com.example.msi.service.impl.UserDetailsServiceImpl;
import com.example.msi.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@Import(SecurityProblemSupport.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private UserServiceImpl userService;
  private final SecurityProblemSupport problemSupport;
  private final PasswordEncoder passwordEncoder;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public WebSecurityConfig(UserServiceImpl userService, SecurityProblemSupport problemSupport, PasswordEncoder passwordEncoder, JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.userService = userService;
    this.problemSupport = problemSupport;
    this.passwordEncoder = passwordEncoder;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }


  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    // Get AuthenticationManager bean
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    auth.userDetailsService(userService) // Cung cáp userservice cho spring security
        .passwordEncoder(passwordEncoder); // cung cấp password encoder
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.antMatcher("/api/**")
        .cors().and()
        .csrf()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(problemSupport)
        .accessDeniedHandler(problemSupport)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/user/**").permitAll()
        .antMatchers(HttpMethod.POST, "*/swagger-ui.html/*").permitAll()
//        .antMatchers("/company/*").hasAnyAuthority(RoleEnum.TEACHER.name(), RoleEnum.ADMIN.name())
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        //        * Thêm một lớp Filter kiểm tra jwt
        .and()
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .antMatchers("/swagger-ui.html")
        .antMatchers("/user/**");
  }
}

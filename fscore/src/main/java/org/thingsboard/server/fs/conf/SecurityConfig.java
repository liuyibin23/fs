package org.thingsboard.server.fs.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.thingsboard.server.fs.service.security.*;
import org.thingsboard.server.fs.service.security.authtoken.AuthTokenFilter;
import org.thingsboard.server.fs.service.security.authtoken.AuthTokenProvider;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthTokenProvider authTokenProvider;

    @Bean
    public AuthTokenFilter authenticationTokenFilterBean() throws Exception {
        List<String> pathsToSkip = Arrays.asList("/login");
        List<String> processingPath = Arrays.asList("/api/files/test1");
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, processingPath);
//        RequestMatcher matcher = new AntPathRequestMatcher("");
        AuthTokenFilter filter = new AuthTokenFilter(matcher);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public CustomerFilter customerFilterBean(){
        CustomerFilter customerFilter = new CustomerFilter("/api/foo/**");
        customerFilter.setAuthenticationManager(authenticationManager);
        return customerFilter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/page/**").permitAll()
                .and().addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customerFilterBean(),UsernamePasswordAuthenticationFilter.class);
        http.logout().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authTokenProvider);
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    protected AuthenticationManager authenticationManager() throws Exception {
////        return super.authenticationManager();
//        return authenticationManager;
//    }

    //    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests().anyRequest().permitAll()
//                .and().addFilterBefore(new CustomerFilter("/api/files/**"), UsernamePasswordAuthenticationFilter.class);
////        http
////            .authorizeRequests()
////            .anyRequest()
////                .permitAll();
//////                .and()
////////                .antMatcher("/api/files/**")
//////                .addFilterBefore(new CustomerFilter("/api/files/**"),
//////                        UsernamePasswordAuthenticationFilter.class);
//////                .authorizeRequests()
//////                .anyRequest().hasRole("API");
//////                .authenticated();
////
//////            .and()
//////            .formLogin().and()
//////            .httpBasic();
////        http.csrf().disable();
//
////        http
////                .authorizeRequests()
////                .anyRequest()
////                .permitAll();
//////                .authenticated();
////
//////            .and()
//////            .formLogin().and()
//////            .httpBasic();
////        http.csrf().disable();
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth
////            .inMemoryAuthentication()
////            .withUser("root")
////            .password("root")
////            .roles("USER").authorities("USER")
////            .and()
////            .withUser("admin").password("admin")
////            .roles("ADMIN", "USER").authorities("ADMIN", "USER")
////            .and()
////            .withUser("user").password("user")
////            .roles("USER").authorities("USER");
//            auth.authenticationProvider(customAuthenticationProvider);
//    }

}

package com.example.jwttokensimplements.security.config;

//import com.example.jwttokensimplements.security.AuthenticationProviderCustom;
import com.example.jwttokensimplements.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAthFilter jwtAuthFilter;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppUserService appUserService;
//    private final AuthenticationProviderCustom provider;
//    private final static List<UserDetails> APPLICATION_USERS = Arrays.asList(
//          new User(
//               "admin@gmail.com",
//               "password",
//               Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
//          ),
//            new User(
//               "user@gmail.com",
//                       "password",
//               Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//            )
//    );
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http
//        .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/**/auth/**")
////                .authorizeHttpRequests((authorize) -> authorize
////                        .antMatchers("/resource/**").hasAuthorize("SCOPE_resource")
////
////                )
////                .antMatchers("/**/auth/**")
//                .permitAll()
//                .antMatchers("/**/auth/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()

                http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(
//                        (authorize) ->
//                                authorize.requestMatchers("/**/auth/**").permitAll().anyRequest().authenticated())

                        .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/**/auth/**").permitAll().anyRequest().authenticated().and();
                        })



                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();




    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//                return APPLICATION_USERS
//                        .stream()
//                        .filter(u -> u.getUsername().equals(email))
//                        .findFirst()
//                        .orElseThrow(()-> new UsernameNotFoundException("No user was found"));
//            }
//        };
//    }




}

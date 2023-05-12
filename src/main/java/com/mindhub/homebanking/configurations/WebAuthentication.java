package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {


    @Autowired
    ClientRepository clientRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Client client = clientRepository.findByEmail(inputName);
            if (client != null) {
                if(inputName.contains("@admin.com")){
                    return new  User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("ADMIN"));
                }
                if(client.isStatus()) {
                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("CLIENT"));
                }
                else return null;
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}
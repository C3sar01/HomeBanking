package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.authorizeRequests()//ORDENAR LOS ACCESOS Y HACERLOS BIEN.
                .antMatchers("/web/html/index.html").permitAll()
                .antMatchers("/web/html/clientList.html").hasAuthority("ADMIN")
                .antMatchers("/web/html/indexAdmin.html", "/web/html/loanAdmin.html", "/api/loansType").hasAuthority("ADMIN")
                .antMatchers("/web/css/**", "/web/img/**", "/web/js/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/**").permitAll()
                .antMatchers("/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/transactions").hasAuthority("CLIENT")
                .antMatchers("/api/loans").hasAuthority("CLIENT");



        http.formLogin().usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();


        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

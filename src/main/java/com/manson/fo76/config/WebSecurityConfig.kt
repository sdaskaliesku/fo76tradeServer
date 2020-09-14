package com.manson.fo76.config

import com.manson.fo76.service.NoopPasswordEncoder
import com.manson.fo76.service.UserService
import com.manson.fo76.web.JwtAuthenticationEntryPoint
import com.manson.fo76.web.JwtRequestFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityConfig @Autowired constructor(private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
                                               private val userService: UserService, private val jwtRequestFilter: JwtRequestFilter) : WebSecurityConfigurerAdapter() {
    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder())
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return NoopPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.csrf().disable() // all requests need to be authenticated
                .authorizeRequests()
                .antMatchers("/items/upload", "/items/delete", "/items/deleteAll", "/users/delete", "/users/forgot")
                .authenticated() // allow everything
                .antMatchers("/**").permitAll()
                .and() // make sure we use stateless session; session won't be used to
                // store user's state.
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}
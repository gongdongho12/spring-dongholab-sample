package com.dongholab.configure

import com.dongholab.configure.security.JwtAuthenticationFilter
import com.dongholab.configure.security.RestAuthenticationEntryPoint
import com.dongholab.provider.JwtTokenProvider
import com.dongholab.service.DongholabUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class WebSecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    lateinit var customUserDetailsService: DongholabUserDetailService

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean() = super.authenticationManagerBean()!!

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    override fun configure(http: HttpSecurity?) {
        http!!
            .cors() // cors 설정
            .and()
            .httpBasic().disable() // Rest API 형태로 개발하기 때문에 비활성 시킵니다.
            .csrf().disable() // 비활성
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션설정도 STATELESS로 해줍니다.
            .and()
            .exceptionHandling() // 상단에서 만들었던 RestAuthenticationEntryPoint 클래스 객체를 등록해줍니다.
            .authenticationEntryPoint(RestAuthenticationEntryPoint())
            .and()
            // matcher를 통해서 권한에 따른 제한을 두는 설정입니다.
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") // 어드민만 접근 가능
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER") // 유저만 접근 가능
                .antMatchers("/**").permitAll() // 모든 권한의 유저 접근 가능
                .anyRequest().authenticated() // 모든 리퀘스트는 인증과정을 거처야 함
            .and()
            // 상단에서 만들었던 Jwt 토큰 필터를 등록해줍니다.
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
    }

    // 인증의 저장 관리를 해줄 userDetailsService, passwordEncoder와 함께 등록해줍니다.
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!
            .userDetailsService<UserDetailsService>(customUserDetailsService)
            .passwordEncoder(passwordEncoder())
    }
}
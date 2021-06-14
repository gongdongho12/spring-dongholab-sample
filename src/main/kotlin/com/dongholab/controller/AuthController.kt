package com.dongholab.controller

import com.dongholab.domain.account.AuthReqModel
import com.dongholab.domain.account.RoleType
import com.dongholab.entity.User
import com.dongholab.provider.JwtTokenProvider
import com.dongholab.repository.jpa.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    @PostMapping("/sign-in")
    fun signIn(@RequestBody authReqModel: AuthReqModel): String =
    // 사용자 id와 password로 UsernamePasswordAuthenticationToken 객체를 생성하여
        // 인증을 시도합니다.
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authReqModel.id,
                authReqModel.password
            )
        )
            // 인증이 완료되면, 인증 객체를 저장해줍니다.
            // 토큰을 만들어서 리턴하여 줍니다.
            .let { authentication ->
                SecurityContextHolder.getContext().authentication = authentication
                return tokenProvider.createToken(authentication)
            }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody authReqModel: AuthReqModel): ResponseEntity<Any> {
        // 사용자 id와 password로 UsernamePasswordAuthenticationToken 객체를 생성하여
        // 인증을 시도합니다.
        if (authReqModel.roleType != null && authReqModel.name != null) {
            val password = passwordEncoder.encode(authReqModel.password)
            println("password ${password}")
            return ResponseEntity.ok(
                    userRepository.save(
                        User(
                            authReqModel.id,
                            authReqModel.name,
                            password,
                            RoleType.valueOf(authReqModel.roleType)
                        )
                    )
            )
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}
package com.dongholab.controller

import com.dongholab.domain.account.UserPrincipal
import com.dongholab.repository.jpa.UserRepository
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val userRepository: UserRepository) {

    @GetMapping
    fun getUser() =
    // Spring security 로부터 Principal을 가져옵니다.
        // 인증 필터에서 user 정보가 이미 세팅되었습니다.
        SecurityContextHolder.getContext().authentication.principal
            .let { principal ->
                when(principal) {
                    // 객체 타입이 UserPrincial인면 username을 받아옵니다.
                    is UserPrincipal -> principal.username
                    else -> throw InternalAuthenticationServiceException("Can not found matched User Principal")
                }.let { id -> userRepository.findById(id).get() }
            }

}
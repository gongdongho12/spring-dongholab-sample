package com.dongholab.service

import com.dongholab.domain.account.UserPrincipal
import com.dongholab.repository.jpa.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class DongholabUserDetailService: UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(id: String?): UserDetails {
        return (id?.let { it ->
            userRepository.findById(it).let { it ->
                it.get().let {
                    UserPrincipal(
                        it.id,
                        it.name,
                        it.password,
                        listOf<GrantedAuthority>(SimpleGrantedAuthority(it.roleType.name))
                    )
                }
            }
        }) ?: throw UsernameNotFoundException("Can not found username.")
    }
}

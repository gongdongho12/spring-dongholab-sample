package com.dongholab.domain.account

import com.dongholab.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    private val id: String,
    private val name: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
): UserDetails {
    companion object {
        private fun create(user: User) =
            UserPrincipal(
                user.id,
                user.name,
                user.password,
                listOf<GrantedAuthority>(SimpleGrantedAuthority(user.roleType.name))
            )
    }

    override fun getAuthorities() = this.authorities

    override fun getPassword() = this.password

    // as unique key
    override fun getUsername() = this.id

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
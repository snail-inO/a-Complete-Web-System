package com.multi.sys.security;

import com.multi.sys.bean.Role;
import com.multi.sys.bean.User;
import com.multi.sys.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
public class SysUserDetails implements UserDetails {
    private User user;
    private RoleMapper roleMapper;

    public SysUserDetails(User user, RoleMapper roleMapper) {
        this.user = user;
        this.roleMapper = roleMapper;
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = roleMapper.selectRoleByUserId(user.getUserId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            log.info(role.getName());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }
}

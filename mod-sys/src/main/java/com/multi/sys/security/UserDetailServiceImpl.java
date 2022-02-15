package com.multi.sys.security;

import com.multi.sys.bean.User;
import com.multi.sys.mapper.RoleMapper;
import com.multi.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {
    private UserMapper userMapper;
    private RoleMapper roleMapper;

    public UserDetailServiceImpl(UserMapper userMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectUserByName(username);
        if (user == null)
            throw new UsernameNotFoundException(username + " user not found");
        return new SysUserDetails(user, roleMapper);
    }
}

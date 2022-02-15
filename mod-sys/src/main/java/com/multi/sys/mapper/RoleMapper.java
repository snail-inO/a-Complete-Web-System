package com.multi.sys.mapper;

import com.multi.common.utils.SQLBuilder;
import com.multi.sys.bean.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Set;

@Mapper
public interface RoleMapper {
    @SelectProvider(type = SQLBuilder.class, method = "selectRoleByUserId")
    Set<Role> selectRoleByUserId(final long userId);
}

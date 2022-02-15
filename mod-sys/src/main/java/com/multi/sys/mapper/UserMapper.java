package com.multi.sys.mapper;

import com.multi.common.utils.SQLBuilder;
import com.multi.sys.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface UserMapper {
    @SelectProvider(type = SQLBuilder.class, method = "selectUserByName")
    User selectUserByName(final String userName);
}

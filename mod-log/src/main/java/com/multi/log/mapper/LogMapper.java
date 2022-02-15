package com.multi.log.mapper;

import com.multi.common.utils.SQLBuilder;
import com.multi.log.bean.Log;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    @InsertProvider(type = SQLBuilder.class, method = "insertLog")
    void insertLog(Log log);
}

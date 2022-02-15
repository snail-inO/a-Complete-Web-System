package com.multi.sys;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class AppTest
{
    @Autowired
    DataSource dataSource;
    @Autowired
    HashOperations<String, String, Object> redisHash;

    @Test
    public void contextLoads() throws SQLException
    {
        System.out.println(dataSource.getConnection());

        Map<String, Object> map = new HashMap<>();
        map.put("id", "10010");
        map.put("name", "redis_name");
        map.put("amount", 12.34D);
        map.put("age", 11);
        redisHash.putAll("hashKey", map);
        // HGET key field 获取存储在哈希表中指定字段的值
        String name = (String) redisHash.get("hashKey", "name");
        System.out.println(name);

    }
}

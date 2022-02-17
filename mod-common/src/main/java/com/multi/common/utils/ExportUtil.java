package com.multi.common.utils;

import com.multi.common.returns.exception.CustomException;
import com.multi.common.returns.exception.ExceptionEnum;
import com.multi.common.utils.sqlbuilder.DictionarySQLBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class ExportUtil {
    private static JdbcTemplate jdbcTemplate;
    public static final String PATH = "E:\\work_space\\export.csv";
    public static final String PATH_D = "'E:/work_space/export.csv'";

    public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        ExportUtil.jdbcTemplate = jdbcTemplate;
    }

    private static void doExport(String table) {
        File file = new File(PATH);
        if (file.exists())
            log.info("file exist");
        if (file.delete() == false)
            log.info("delete file failed");
        jdbcTemplate.execute("select * into outfile " + PATH_D + " fields terminated by ','  from " + table);
    }

    public static void export(HttpServletResponse resp, String table) throws IOException {
        Date time = new Date();
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment;fileName=" + time.toString() + ".csv");
        doExport(table);
        File file = new File(PATH);
        FileInputStream in = new FileInputStream(file);
        int len = (int) file.length();
        byte buffer[] = new byte[len];
        in.read(buffer, 0, len);
        in.close();

        ServletOutputStream out = resp.getOutputStream();
        out.write(buffer, 0, len);
        out.close();
    }
}

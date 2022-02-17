package com.multi.common.utils.sqlbuilder;

import org.apache.ibatis.jdbc.SQL;

public class DictionarySQLBuilder {
    public static final String DIC_TB = "sys_dict";

    public String insertDictionary() {
        return new SQL() {
            {
                INSERT_INTO(DIC_TB);
                VALUES("dict_id, name, description, create_by, update_by, create_time, update_time",
                        "#{dictId}, #{name}, #{description}, #{createBy}, #{updateBy}, #{createTime}, #{updateTime}");
            }
        }.toString();
    }

    public String updateDictionary() {
        return new SQL() {
            {
                UPDATE(DIC_TB);
                SET("dict_id = #{dictId}, name = #{name}, description = #{description}, create_by = #{createBy}, " +
                        "update_by = #{updateBy}, create_time = #{createTime}, update_time = #{updateTime}");
                WHERE("dict_id = #{dictId}");
            }
        }.toString();
    }

    public String selectDictionary(final long dictId) {
        return new SQL() {
            {
                SELECT("*");
                FROM(DIC_TB);
                WHERE("dict_id = #{dictId}");
            }
        }.toString();
    }

    public String selectAllDictionary() {
        return new SQL() {
            {
                SELECT("*");
                FROM(DIC_TB);
            }
        }.toString();
    }

    public String deleteDictionary(final long dictId) {
        return new SQL() {
            {
                DELETE_FROM(DIC_TB);
                WHERE("dict_id = #{dictId}");
            }
        }.toString();
    }
}

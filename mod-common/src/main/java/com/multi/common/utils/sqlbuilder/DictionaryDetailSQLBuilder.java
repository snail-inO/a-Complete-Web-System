package com.multi.common.utils.sqlbuilder;

import org.apache.ibatis.jdbc.SQL;

public class DictionaryDetailSQLBuilder {
    public static final String DIC_D_TB = "sys_dict_detail";

    public String insertDictionaryDetail() {
        return new SQL() {
            {
                INSERT_INTO(DIC_D_TB);
                VALUES("detail_id, dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time",
                        "#{detailId}, #{dictId}, #{label}, #{value}, #{dictSort}, #{createBy}, #{updateBy}," +
                                "#{createTime}, #{updateTime}");
            }
        }.toString();
    }

    public String updateDictionaryDetail() {
        return new SQL() {
            {
                UPDATE(DIC_D_TB);
                SET("detail_id = #{detailId}, dict_id = #{dictId}, label = #{label}, value = #{value}, " +
                        "dict_sort = #{dictSort}, create_by = #{createBy}, update_by = #{updateBy}, " +
                        "create_time = #{createTime}, update_time = #{updateTime}");
                WHERE("detail_id = #{detailId}");
            }
        }.toString();
    }

    public String selectDictionaryDetail(final long detailId) {
        return new SQL() {
            {
                SELECT("*");
                FROM(DIC_D_TB);
                WHERE("detail_id = #{detailId}");
            }
        }.toString();
    }

    public String selectAllDictionaryDetail() {
        return new SQL() {
            {
                SELECT("*");
                FROM(DIC_D_TB);
            }
        }.toString();
    }

    public String deleteDictionaryDetail(final long detailId) {
        return new SQL() {
            {
                DELETE_FROM(DIC_D_TB);
                WHERE("detail_id = #{detailId}");
            }
        }.toString();
    }
}

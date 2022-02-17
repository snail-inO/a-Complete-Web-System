package com.multi.common.utils.sqlbuilder;

import org.apache.ibatis.jdbc.SQL;

public class DepartmentSQLBuilder {
    public static final String DEP_TB = "sys_dept";

    public String insertDepartment() {
        return new SQL() {
            {
                INSERT_INTO(DEP_TB);
                VALUES("dept_id, pid, sub_count, name, dept_sort, enabled, create_by, update_by, create_time, update_time",
                        "#{deptId}, #{pid}, #{subCount}, #{name}, #{deptSort}, #{enabled}, #{createBy}," +
                                "#{updateBy}, #{createTime}, #{updateTime}");
            }
        }.toString();
    }

    public String updateDepartment() {
        return new SQL() {
            {
                UPDATE(DEP_TB);
                SET("dept_id = #{deptId}, pid = #{pid}, sub_count = #{subCount}, name = #{name}," +
                        "dept_sort = #{deptSort}, enabled = #{enabled}, create_by = #{createBy}, update_by = #{updateBy}, " +
                        "create_time = #{createTime}, update_time = #{updateTime}");
                WHERE("dept_id = #{deptId}");
            }
        }.toString();
    }

    public String selectDepartment(final long deptId) {
        return new SQL() {
            {
                SELECT("*");
                FROM(DEP_TB);
                WHERE("dept_id = #{deptId}");
            }
        }.toString();
    }

    public String selectAllDepartment() {
        return new SQL() {
            {
                SELECT("*");
                FROM(DEP_TB);
            }
        }.toString();
    }

    public String deleteDepartment(final long deptId) {
        return new SQL() {
            {
                DELETE_FROM(DEP_TB);
                WHERE("dept_id = #{deptId}");
            }
        }.toString();
    }
}

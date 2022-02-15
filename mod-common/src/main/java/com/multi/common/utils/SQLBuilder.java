package com.multi.common.utils;

import org.apache.ibatis.jdbc.SQL;

public class SQLBuilder {
    private static final String LOG_TB = "sys_log";
    private static final String USER_TB = "sys_user u";
    private static final String USER_ROLE_TB = "sys_users_roles";
    private static final String ROLE_TB = "sys_role";
    private static final String MENU_TB = "sys_menu";

    public String insertLog() {
        return new SQL() {
            {
                INSERT_INTO(LOG_TB);
                VALUES("log_id, description, log_type, method, params, request_ip, time, username, address," +
                                "browser, exception_detail, create_time",
                        "#{logId}, #{description}, #{logType}, #{method}, #{params}, #{requestIp}, #{time}," +
                                "#{username}, #{address}, #{browser}, #{exceptionDetail}, #{createTime}");
            }
        }.toString();
    }

    public String selectUserByName(final String userName) {
        return new SQL() {
            {
                SELECT("*");
                FROM(USER_TB);
                WHERE("username = #{userName}");
            }
        }.toString();
    }

    public String selectRoleByUserId(final long userId) {
        return new SQL() {
            {
                SELECT("r.*");
                FROM(USER_TB);
                JOIN("sys_users_roles ur on u.user_id = ur.user_id");
                JOIN("sys_role r on r.role_id = ur.role_id");
                WHERE("u.user_id = #{userId}");
            }
        }.toString();
    }

    public String selectMenuById(final long menuId) {
        return new SQL() {
            {
                SELECT("*");
                FROM(MENU_TB);
                WHERE("menu_id = #{menuId}");
            }
        }.toString();
    }

    public String selectMenuByPid(final long pid) {
        return new SQL() {
            {
                SELECT("*");
                FROM(MENU_TB);
                WHERE("pid = #{pid}");
            }
        }.toString();
    }

    public String createMenu() {
        return new SQL() {
            {
                INSERT_INTO(MENU_TB);
                VALUES("menu_id, pid, sub_count, type, title, name, component, menu_sort, icon, path," +
                        "i_frame, cache, hidden, permission, create_by, update_by, create_time, update_time",
                        "#{menuId}, #{pid}, #{subCount}, #{type}, #{title}, #{name}, #{component}, #{menuSort}," +
                                "#{icon}, #{path}, #{iFrame}, #{cache}, #{hidden}, #{permission}, #{createBy}," +
                                "#{updateBy}, #{createTime}, #{updateTime}");
            }
        }.toString();
    }

    public String updateMenu() {
        return new SQL() {
            {
                UPDATE(MENU_TB);
                SET("menu_id = #{menuId}, pid = #{pid}, sub_count = #{subCount}, type = #{type}, title = #{title}," +
                        "name = #{name}, component = #{component}, menu_sort = #{menuSort}, icon = #{icon}, path = #{path}," +
                        "i_frame = #{iFrame}, cache = #{cache}, hidden = #{hidden}, permission = #{permission}," +
                        "create_by = #{createBy}, update_by = #{updateBy}, create_time = #{createTime}, update_time = #{updateTime}");
                WHERE("menu_id = #{menuId}");
            }
        }.toString();
    }

    public String deleteMenuById(final long menuId) {
        return new SQL() {
            {
                DELETE_FROM(MENU_TB);
                WHERE("menu_id = #{menuId}");
            }
        }.toString();
    }
}

package com.multi.sys.mapper;

import com.multi.common.utils.SQLBuilder;
import com.multi.sys.bean.Menu;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface MenuMapper {
    @SelectProvider(type = SQLBuilder.class, method = "selectMenuById")
    Menu selectMenuById(final long menuId);
    @SelectProvider(type = SQLBuilder.class, method = "selectMenuByPid")
    Set<Menu> selectMenuByPid(final long pid);
    @InsertProvider(type = SQLBuilder.class, method = "createMenu")
    void insertMenu(Menu menu);
    @UpdateProvider(type = SQLBuilder.class, method = "updateMenu")
    void updateMenu(Menu menu);
    @DeleteProvider(type = SQLBuilder.class, method = "deleteMenuById")
    void deleteMenuById(final long menuId);
}

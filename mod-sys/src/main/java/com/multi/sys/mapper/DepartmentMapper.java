package com.multi.sys.mapper;

import com.multi.common.utils.sqlbuilder.DepartmentSQLBuilder;
import com.multi.sys.bean.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    @InsertProvider(value = DepartmentSQLBuilder.class, method = "insertDepartment")
    void createDepartment(Department department);
    @UpdateProvider(value = DepartmentSQLBuilder.class, method = "updateDepartment")
    void updateDepartment(Department department);
    @SelectProvider(value = DepartmentSQLBuilder.class, method = "selectDepartment")
    Department retrieveDepartment(final long deptId);
    @SelectProvider(value = DepartmentSQLBuilder.class, method = "selectAllDepartment")
    List<Department> retrieveAllDepartment();
    @DeleteProvider(value = DepartmentSQLBuilder.class, method = "deleteDepartment")
    void deleteDepartment(final long deptId);
}

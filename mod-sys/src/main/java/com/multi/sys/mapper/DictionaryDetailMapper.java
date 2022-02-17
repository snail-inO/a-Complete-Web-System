package com.multi.sys.mapper;

import com.multi.common.utils.sqlbuilder.DictionaryDetailSQLBuilder;
import com.multi.sys.bean.DictionaryDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DictionaryDetailMapper {
    @InsertProvider(value = DictionaryDetailSQLBuilder.class, method = "insertDictionaryDetail")
    void createDictionaryDetail(DictionaryDetail dictionaryDetail);
    @UpdateProvider(value = DictionaryDetailSQLBuilder.class, method = "updateDictionaryDetail")
    void updateDictionaryDetail(DictionaryDetail dictionaryDetail);
    @SelectProvider(value = DictionaryDetailSQLBuilder.class, method = "selectDictionaryDetail")
    DictionaryDetail retrieveDictionaryDetail(final long detailId);
    @SelectProvider(value = DictionaryDetailSQLBuilder.class, method = "selectAllDictionaryDetail")
    List<DictionaryDetail> retrieveAllDictionaryDetail();
    @DeleteProvider(value = DictionaryDetailSQLBuilder.class, method = "deleteDictionaryDetail")
    void deleteDictionaryDetail(final long detailId);
}

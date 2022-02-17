package com.multi.sys.mapper;

import com.multi.common.utils.sqlbuilder.DictionarySQLBuilder;
import com.multi.sys.bean.Dictionary;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DictionaryMapper {
    @InsertProvider(value = DictionarySQLBuilder.class, method = "insertDictionary")
    void createDictionary(Dictionary dictionary);
    @UpdateProvider(value = DictionarySQLBuilder.class, method = "updateDictionary")
    void updateDictionary(Dictionary dictionary);
    @SelectProvider(value = DictionarySQLBuilder.class, method = "selectDictionary")
    Dictionary retrieveDictionary(final long dictId);
    @SelectProvider(value = DictionarySQLBuilder.class, method = "selectAllDictionary")
    List<Dictionary> retrieveAllDictionary();
    @DeleteProvider(value = DictionarySQLBuilder.class, method = "deleteDictionary")
    void deleteDictionary(final long dictId);
}

package com.java.xdd.shiro.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/20.
 */
public interface CheckPermissionMapper {

    Map<String, Object> getOneByCheckPermission(@Param("tableName") String tableName,
                                                @Param("queryFields") String[] queryFields,
                                                @Param("queryValues") String[] queryValues);

}

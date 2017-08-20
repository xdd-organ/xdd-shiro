package com.java.xdd.shiro.service;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/20.
 */
public interface CheckPermissionService {

    Map<String, Object> getOneByCheckPermission(String tableName, String[] queryFields, String[] queryValues);
}

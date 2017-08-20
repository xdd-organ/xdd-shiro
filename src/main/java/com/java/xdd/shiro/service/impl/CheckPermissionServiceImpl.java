package com.java.xdd.shiro.service.impl;

import com.java.xdd.shiro.mapper.CheckPermissionMapper;
import com.java.xdd.shiro.service.CheckPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/20.
 */
@Service("checkPermissionService")
public class CheckPermissionServiceImpl implements CheckPermissionService{
    @Autowired
    private CheckPermissionMapper checkPermissionMapper;

    @Override
    public Map<String, Object> getOneByCheckPermission(String tableName, String[] queryFields, String[] queryValues) {
        Map<String, Object> checkPermission = checkPermissionMapper.getOneByCheckPermission(tableName, queryFields, queryValues);
        return checkPermission;
    }
}

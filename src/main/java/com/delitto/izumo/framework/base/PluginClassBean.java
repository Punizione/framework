package com.delitto.izumo.framework.base;

import lombok.Data;

@Data
public class PluginClassBean {
    String path;
    String mainClass;
    //相应指令前缀
    String responsePrefix;
    //执行指令需要的权限级别
    int needPermission;
}

package com.delitto.izumo.framework.base.plugin;

import lombok.Data;

@Data
public class PluginConfig {
    String proxyIP;
    int proxyPort;

    String imageSavePath;
    String tempSavePath;
}

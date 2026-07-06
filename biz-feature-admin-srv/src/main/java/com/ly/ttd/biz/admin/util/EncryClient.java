package com.ly.ttd.biz.admin.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yong.li
 * @since 2026/4/2 16:15
 */
@Configuration
public class EncryClient {
    @Value("${enc.key}")
    private String key;

    public String encrypt(String data) {
        if (StringUtils.isBlank(data)) {
            return data;
        }
        AES aes = SecureUtil.aes(key.getBytes());
        return aes.encryptHex(data);
    }

    public String decrypt(String data) {
        if (StringUtils.isBlank(data)) {
            return data;
        }
        AES aes = SecureUtil.aes(key.getBytes());
        return aes.decryptStr(data);
    }

}

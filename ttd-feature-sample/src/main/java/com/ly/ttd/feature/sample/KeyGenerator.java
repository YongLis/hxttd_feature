package com.ly.ttd.feature.sample;

import java.util.Base64;

/**
 * @author yong.li
 * @since 2026/6/29 21:00
 */
public class KeyGenerator {
    public static void main(String[] args) {
        // 生成一个 32 字节的随机字节数组，并编码为 Base64
        byte[] keyBytes = new byte[32];
        new java.security.SecureRandom().nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Generated NACOS_AUTH_TOKEN: " + base64Key);

        // MODE=standalone
        // NACOS_AUTH_ENABLE=false
        // NACOS_AUTH_TOKEN=dl50uUWh+aS0C9XHrjEnwd/JkJOh2NFBOyicbvfoVNg=
        // NACOS_AUTH_IDENTITY_KEY=admin123
        // NACOS_AUTH_IDENTITY_VALUE=admin123
    }
}

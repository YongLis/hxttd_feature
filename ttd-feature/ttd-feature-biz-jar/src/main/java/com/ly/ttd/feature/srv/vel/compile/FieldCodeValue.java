package com.ly.ttd.feature.srv.vel.compile;

import lombok.Data;

/**
 * @author yong.li
 * @since 2026/4/22 18:04
 */
@Data
public class FieldCodeValue {
    private String code;
    private Object value;

    public FieldCodeValue() {
    }

    public FieldCodeValue(String code, Object value) {
        this.code = code;
        this.value = value;
    }
}

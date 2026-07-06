package com.ly.ttd.feature.common.exception;

import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;

/**
 * @author yong.li
 * @since 2026/4/14 12:56
 */
public class FeatureBizException extends RuntimeException {

    private String code;
    private String message;

    public FeatureBizException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public FeatureBizException(String message) {
        super(message);
        this.code = "-999999";
        this.message = message;
    }

    public FeatureBizException(FeatureResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

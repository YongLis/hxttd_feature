package com.ly.ttd.feature.engine.req;

import com.ly.ttd.feature.common.enums.RunModeEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 交易特征查询请求
 *
 * @author yong.li
 * @since 2026/6/10 09:29
 */
@Data
public class TxnFeatureReq {
    // 运行模式(不保存trace)
    private String runMode = RunModeEnum.TEST.getCode();

    /**
     * 交易号
     */
    @NotBlank(message = "交易号不能为空")
    private String txnId;
    /**
     * 接入点编码
     */
    @NotBlank(message = "接入点编码不能为空")
    private String pointCode;
    /**
     * 业务代码
     */
    @NotBlank(message = "业务代码不能为空")
    private String bizCode;
    /**
     * 交易时间
     */
    @NotBlank(message = "交易时间不能为空")
    private Date txnTime;

    private String traceId;

    private Map<String, Object> req;


}

package com.ly.ttd.feature.common.ctx;

import com.ly.ttd.feature.common.consts.TxnConsts;
import com.ly.ttd.feature.common.enums.RunModeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 交易上下文
 *
 * @author yong.li
 * @since 2026/6/10 09:29
 */
@Data
@Slf4j
public class TxnParamContext {

    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 交易号
     */
    private String txnId;
    /**
     * 接入点编码
     */
    private String pointCode;
    /**
     * 业务代码
     */
    private String bizCode;
    private Date txnTime;
    private String traceId;

    // 运行模式
    private String runMode = RunModeEnum.TEST.getCode();

    private Map<String, Object> req;

    private Set<String> computingSet = ConcurrentHashMap.newKeySet();

    public TxnParamContext() {
        traceId = UUID.randomUUID().toString();
    }

    /**
     * 当前实例指标
     */
    private Map<String, Object> variable = new ConcurrentHashMap<>();

    public boolean existKey(String factorCode) {
        return variable.containsKey(factorCode);
    }

    public void put(String factorCode, Object val) {
        if (null != val) {
            variable.put(factorCode, val);
        } else {
            log.warn("{} field calculate result is null", factorCode);
        }
        // 无论是否计算成功，都添加计算集合中
        computingSet.add(factorCode);
    }

    public Object get(String factorCode) {
        return variable.get(factorCode);
    }


    public Map<String, Object> buildScriptParams() {
        Map<String, Object> param = new HashMap<>();
        param.put(TxnConsts.TXN_ID, getTxnId());
        param.put(TxnConsts.POINT_CODE, getPointCode());
        return param;
    }


    public Map<String, Object> getParamByFields(List<String> factorCodes) {
        Map<String, Object> param = new HashMap<>();
        if (CollectionUtils.isEmpty(factorCodes)) {
            return param;
        }
        factorCodes.forEach(factorCode -> param.put(factorCode, get(factorCode)));
        return param;
    }

    /**
     * 已计算集合包含当前依赖的因子判定
     */
    public boolean isComputed(List<String> refFactorCodes) {
        return refFactorCodes.stream().allMatch(computingSet::contains);
    }


}

package com.ly.ttd.biz.feature.dem.sweb.service.sequence;


/**
 * 序列查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface SequenceQueryService {

    /**
     * 查询下一个接入点编码
     */
    public String getNextPointCode();
}

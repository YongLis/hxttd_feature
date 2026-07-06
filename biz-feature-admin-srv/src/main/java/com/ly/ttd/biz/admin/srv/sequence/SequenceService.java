package com.ly.ttd.biz.admin.srv.sequence;

/**
 * 序列管理服务
 *
 * @author yong.li
 * @since 2026-05-24
 */
public interface SequenceService {

    /**
     * 获取下一个序列值（原子递增）
     *
     * @param seqCode 序列编码
     * @return 新序列值
     */
    Long nextVal(String seqCode);
}

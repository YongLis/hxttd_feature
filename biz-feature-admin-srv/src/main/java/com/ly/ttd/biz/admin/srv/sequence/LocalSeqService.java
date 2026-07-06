package com.ly.ttd.biz.admin.srv.sequence;

/**
 * @author yong.li
 * @since 2026/5/26 14:14
 */
public interface LocalSeqService {

    /**
     * 生成序号
     *
     * @param prefix  前缀
     * @param length  序号长度
     * @param seqCode 序列编码
     */
    public String generateSeq(String prefix, int length, String seqCode);


}

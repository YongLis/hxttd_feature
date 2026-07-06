package com.ly.ttd.biz.admin.srv.sequence;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.srv.sequence.req.SequenceQueryReq;
import com.ly.ttd.biz.admin.srv.sequence.res.SequenceQueryRes;

/**
 * 序列查询服务
 *
 * @author yong.li
 * @since 2026-06-23
 */
public interface SequenceQueryService {

    PageResult<SequenceQueryRes> pageQuery(SequenceQueryReq req);
}

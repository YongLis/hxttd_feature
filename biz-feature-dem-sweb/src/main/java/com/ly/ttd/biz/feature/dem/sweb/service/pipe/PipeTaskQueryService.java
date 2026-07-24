package com.ly.ttd.biz.feature.dem.sweb.service.pipe;

import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.pipe.req.PipeTaskQueryReq;
import com.ly.ttd.feature.admin.api.dto.PipeTaskDto;

/**
 * @author yong.li
 * @since 2026/7/24 12:01
 */
public interface PipeTaskQueryService {

    PageResult<PipeTaskDto> pageQuery(PipeTaskQueryReq req);

}

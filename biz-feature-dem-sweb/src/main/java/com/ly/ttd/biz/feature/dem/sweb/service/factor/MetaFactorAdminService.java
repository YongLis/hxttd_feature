package com.ly.ttd.biz.feature.dem.sweb.service.factor;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.MetaFactorAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.MetaFactorUpdateReq;

/**
 * @author yong.li
 * @since 2026/7/13 15:22
 */
public interface MetaFactorAdminService {

    void add(MetaFactorAddReq req) throws BizException;

    void update(MetaFactorUpdateReq req) throws BizException;


    void delete(Long id, String opUser);

}

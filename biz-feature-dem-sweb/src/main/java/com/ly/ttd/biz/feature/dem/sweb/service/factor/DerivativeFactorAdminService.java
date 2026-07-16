package com.ly.ttd.biz.feature.dem.sweb.service.factor;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.DerivativeFactorAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.DerivativeFactorUpdateReq;

/**
 * @author yong.li
 * @since 2026/7/13 15:22
 */
public interface DerivativeFactorAdminService {

    void add(DerivativeFactorAddReq req) throws BizException;

    void update(DerivativeFactorUpdateReq req) throws BizException;


    void delete(Long id, String opUser);

}

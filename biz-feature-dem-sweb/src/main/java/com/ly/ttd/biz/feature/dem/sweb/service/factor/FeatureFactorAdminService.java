package com.ly.ttd.biz.feature.dem.sweb.service.factor;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FeatureFactorAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FeatureFactorUpdateReq;

/**
 * @author yong.li
 * @since 2026/7/13 15:22
 */
public interface FeatureFactorAdminService {

    void add(FeatureFactorAddReq req) throws BizException;

    void update(FeatureFactorUpdateReq req) throws BizException;


    void delete(Long id, String opUser);

}

package com.ly.ttd.biz.admin.srv.feature.express;


import com.ly.ttd.biz.admin.srv.feature.req.FeatureConfigForm;
import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;

/**
 * 表单配置转脚本
 *
 * @author yong.li
 * @since 2026/6/2 14:51
 */
public interface FeatureConfigFormService {


    /**
     * 表单转脚本
     */
    FeatureConfigModel convertForm(FeatureConfigForm form) throws BizException;

}

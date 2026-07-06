package com.ly.ttd.feature.cfg.resource;

import com.ly.ttd.feature.common.model.factor.DerivativeFactorModel;
import com.ly.ttd.feature.common.model.factor.FactorDependencyModel;
import com.ly.ttd.feature.common.model.factor.FeatureFactorModel;
import com.ly.ttd.feature.common.model.factor.MetaFactorModel;

import java.util.List;

/**
 * 指标配置
 *
 * @author yong.li
 * @since 2026/6/22 15:20
 */
public interface FactorResourceLoader {

    /**
     * 加载元字段因子资源(待实现)
     */
    public List<MetaFactorModel> loadMetaFactor(Long projectId);


    /**
     * 加载衍生因子资源(待实现)
     */
    public List<DerivativeFactorModel> loadDerivativeFactor(Long projectId);

    /**
     * 加载实时特征因子资源(待实现)
     */
    public List<FeatureFactorModel> loadFeatureFactor(Long projectId);


    /**
     * 查询指标依赖血缘
     */
    public List<FactorDependencyModel> queryDependency(Long projectId, String parent);


}

package com.ly.ttd.biz.feature.dem.sweb.service.resource.op;

import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.DataStructMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.resource.AbstractResourceOpService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 数据集资源操作服务
 *
 * @author yong.li
 * @since 2026/6/23 13:13
 */
@Service
@Slf4j
public class DataStructOpService extends AbstractResourceOpService {
    @Resource
    private DataStructMapper dataStructMapper;


    @Override
    public String getResourceType() {
        return FeatureResourceType.DATA_STRUCT.getType();
    }


    @Override
    public AuditDetail getDetail(Long id) {
        return null;
    }
}
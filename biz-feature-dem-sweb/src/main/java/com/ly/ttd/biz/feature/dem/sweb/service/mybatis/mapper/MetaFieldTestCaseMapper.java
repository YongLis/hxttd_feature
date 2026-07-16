package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.MetaFieldTestCaseEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 元字段测试用例 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface MetaFieldTestCaseMapper extends BaseMapper<MetaFieldTestCaseEntity> {
    IPage<MetaFieldTestCaseEntity> pageQuery(Page<MetaFieldTestCaseEntity> page, TestCaseQueryReq req);
}

package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.MetaFieldTestCaseEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.TestCaseQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 元字段测试用例 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface MetaFieldTestCaseMapper {
    List<MetaFieldTestCaseEntity> pageQuery(Page<MetaFieldTestCaseEntity> page, TestCaseQueryReq req);

    /**
     * 根据ID查询
     */
    MetaFieldTestCaseEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<MetaFieldTestCaseEntity> selectAllByMetaFieldCode(String metaFieldCode);

}

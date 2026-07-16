package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.account.req.AccountQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.UserAccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户账号 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccountEntity> {

    IPage<UserAccountEntity> pageQuery(Page<UserAccountEntity> page, @Param("req") AccountQueryReq req);
}

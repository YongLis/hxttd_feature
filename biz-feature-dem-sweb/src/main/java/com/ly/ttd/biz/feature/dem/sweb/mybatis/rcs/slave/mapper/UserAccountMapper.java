package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.UserAccountEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.account.req.AccountQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户账号 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface UserAccountMapper {

    List<UserAccountEntity> pageQuery(Page<UserAccountEntity> page, @Param("req") AccountQueryReq req);

    /**
     * 根据ID查询
     */
    UserAccountEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<UserAccountEntity> selectAll();

}

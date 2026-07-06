package com.ly.ttd.biz.admin.srv.account.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.mybatis.entity.UserAccountEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.UserAccountMapper;
import com.ly.ttd.biz.admin.srv.account.AccountQueryService;
import com.ly.ttd.biz.admin.srv.account.req.AccountQueryReq;
import com.ly.ttd.biz.admin.srv.account.res.AccountQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 账户查询服务实现
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Service
public class AccountQueryServiceImpl implements AccountQueryService {
    @Resource
    private UserAccountMapper userAccountMapper;

    @Override
    public PageResult<AccountQueryRes> pageQuery(AccountQueryReq req) {
        PageResult<AccountQueryRes> result = new PageResult<>();
        Page<UserAccountEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        userAccountMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(e -> {
                AccountQueryRes res = new AccountQueryRes();
                res.setId(e.getId());
                res.setUserAccount(e.getUserAccount());
                res.setCrtUser(e.getCrtUser());
                res.setCrtTime(e.getCrtTime());
                return res;
            }).collect(Collectors.toList()));
        }
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }
}

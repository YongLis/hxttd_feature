package com.ly.ttd.biz.feature.dem.sweb.service.access.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.access.AccessPointQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.access.req.AccessPointQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.access.res.AccessPointDocRes;
import com.ly.ttd.biz.feature.dem.sweb.service.access.res.AccessPointQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.access.res.ParamItem;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AccessPointEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AccessPointParamEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.AccessPointMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.AccessPointParamMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/6/23 14:50
 */
@Service
@Slf4j
public class AccessPointQueryServiceImpl implements AccessPointQueryService {
    @Resource
    private AccessPointMapper accessPointMapper;
    @Resource
    private AccessPointParamMapper accessPointParamMapper;


    @Override
    public PageResult<AccessPointQueryRes> pageQuery(AccessPointQueryReq req) {
        PageResult<AccessPointQueryRes> pageResult = new PageResult<>();
        pageResult.setCurrent(req.getCurrent());
        pageResult.setPageSize(req.getPageSize());
        Page<AccessPointEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        accessPointMapper.pageQuery(page, req);
        List<AccessPointEntity> records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<AccessPointQueryRes> resList = records.stream().map(entity -> {
                AccessPointQueryRes res = new AccessPointQueryRes();
                res.setId(entity.getId());
                res.setCode(entity.getCode());
                res.setName(entity.getName());
                res.setRemark(entity.getRemark());
                res.setProjectId(entity.getProjectId());
                res.setDeleted(entity.getDeleted());
                res.setCrtUser(entity.getCrtUser());
                res.setCrtTime(entity.getCrtTime());
                res.setParams(queryParams(entity.getCode(), entity.getVersion()));
                return res;
            }).collect(Collectors.toList());
            pageResult.setData(resList);
            pageResult.setTotal(page.getTotal());
        }
        return pageResult;
    }

    @Override
    public List<AccessPointEntity> list(Long projectId) {
        return accessPointMapper.listByProjectId(projectId);
    }

    @Override
    public AccessPointQueryRes getDetail(Long id) {
        AccessPointEntity entity = accessPointMapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("接入点不存在");
        }

        AccessPointQueryRes res = new AccessPointQueryRes();
        res.setId(entity.getId());
        res.setCode(entity.getCode());
        res.setName(entity.getName());
        res.setRemark(entity.getRemark());
        res.setProjectId(entity.getProjectId());
        res.setDeleted(entity.getDeleted());
        res.setCrtUser(entity.getCrtUser());
        res.setCrtTime(entity.getCrtTime());

        res.setParams(queryParams(entity.getCode(), entity.getVersion()));

        return res;
    }

    @Override
    public AccessPointDocRes getPointDoc(Long id) {
        AccessPointEntity entity = accessPointMapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("接入点不存在");
        }
        AccessPointDocRes docRes = new AccessPointDocRes();
        docRes.setApiUrl("");
        docRes.setCode(entity.getCode());
        docRes.setName(entity.getName());
        docRes.setVersion(entity.getVersion());
        docRes.setRemark(entity.getRemark());
        docRes.setDeleted(entity.getDeleted());
        docRes.setCrtUser(entity.getCrtUser());
        docRes.setCrtTime(entity.getCrtTime());
        docRes.setReqParam(queryParams(entity.getCode(), entity.getVersion()));
        List<ParamItem> resParam = new ArrayList<>();
        ParamItem retCode = new ParamItem("响应码", "retCode", "STRING", 1, 0, "", "响应码:00000成功，其他失败", 1, null);
        ParamItem retMsg = new ParamItem("响应消息", "retMsg", "STRING", 1, 0, "", "响应消息:成功", 1, null);
        ParamItem data = new ParamItem("数据", "data", "DICT", 1, 0, "", "数据", 1, null);
        resParam.add(retCode);
        resParam.add(retMsg);
        resParam.add(data);
        docRes.setResParam(resParam);
        return docRes;
    }

    private List<ParamItem> queryParams(String accessPointCode, String version) {
        LambdaQueryWrapper<AccessPointParamEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessPointParamEntity::getAccessPointCode, accessPointCode);
        wrapper.eq(AccessPointParamEntity::getDeleted, false);
        wrapper.eq(AccessPointParamEntity::getVersion, version);
        wrapper.orderByAsc(AccessPointParamEntity::getSortOrder);
        List<AccessPointParamEntity> allParams = accessPointParamMapper.selectList(wrapper);
        return buildParamTree(allParams, null);
    }

    /**
     * 递归构建参数树
     */
    private List<ParamItem> buildParamTree(List<AccessPointParamEntity> allParams, String parentParamCode) {
        return allParams.stream()
                .filter(e -> StringUtils.isEmpty(parentParamCode) && StringUtils.isEmpty(e.getParentParamCode()) || !StringUtils.isAnyEmpty(parentParamCode) && parentParamCode.equals(e.getParentParamCode()))
                .map(e -> {
                    ParamItem r = new ParamItem();
                    r.setId(e.getId());
                    r.setParamName(e.getParamName());
                    r.setParamCode(e.getParamCode());
                    r.setParamType(e.getParamType());
                    r.setRequired(e.getRequired());
                    r.setDefaultValue(e.getDefaultValue());
                    r.setDescription(e.getDescription());
                    r.setSortOrder(e.getSortOrder());
                    r.setParentParamCode(parentParamCode);
                    r.setParamLevel(e.getParamLevel());
                    r.setChildren(buildParamTree(allParams, e.getParamCode()));
                    return r;
                }).collect(Collectors.toList());
    }


}

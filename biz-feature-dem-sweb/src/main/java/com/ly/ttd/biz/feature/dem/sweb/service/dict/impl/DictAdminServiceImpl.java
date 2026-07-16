package com.ly.ttd.biz.feature.dem.sweb.service.dict.impl;

import com.ly.ttd.biz.feature.dem.sweb.service.dict.DictAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dict.DictService;
import com.ly.ttd.feature.admin.api.dto.DictDto;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import org.springframework.stereotype.Service;

/**
 * 字典管理服务实现
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Service
public class DictAdminServiceImpl implements DictAdminService {

    @Rpcwired
    private DictService dictService;


    @Override
    public void addDict(DictAddReq req) {

        DictDto dictDto = new DictDto();
        dictDto.setSystemCode(req.getSystemCode());
        dictDto.setDictCode(req.getDictCode());
        dictDto.setDictName(req.getDictName());
        dictDto.setCrtUser(LoginUser.getLoginUserName());
        dictDto.setDeleted(false);
        dictService.add(dictDto);
    }

    @Override
    public void updateDict(DictUpdateReq req) {
        DictDto dictDto = dictService.queryById(req.getId());
        dictDto.setDictName(req.getDictName());
        dictDto.setDeleted(req.getDeleted());
        dictDto.setUptUser(LoginUser.getLoginUserName());
        dictService.update(dictDto);
    }

    @Override
    public void deleteDict(Long id) {
        DictDto dictDto = dictService.queryById(id);
        if (dictDto == null) {
            throw new RuntimeException("字典不存在");
        }
        dictService.delete(id, LoginUser.getLoginUserName());
    }

    @Override
    public DictDto getDictCode(String systemCode, String dictCode) {
        return dictService.getDictCode(systemCode, dictCode);
    }
}

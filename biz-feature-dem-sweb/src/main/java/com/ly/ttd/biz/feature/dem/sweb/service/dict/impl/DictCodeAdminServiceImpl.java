package com.ly.ttd.biz.feature.dem.sweb.service.dict.impl;

import com.ly.ttd.biz.feature.dem.sweb.service.dict.DictCodeAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeUpdateReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.dict.DictCodeService;
import com.ly.ttd.feature.admin.api.dict.DictService;
import com.ly.ttd.feature.admin.api.dto.DictCodeDto;
import com.ly.ttd.feature.admin.api.dto.DictDto;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典键值管理服务实现
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Service
public class DictCodeAdminServiceImpl implements DictCodeAdminService {

    @Rpcwired
    private DictCodeService dictCodeService;
    @Rpcwired
    private DictService dictService;

    @Override
    public void addDictCode(DictCodeAddReq req) {
        dictCodeService.addDictCode(req.getDictId(), req.getDictKey(), req.getDictValue());

    }

    @Override
    public void updateDictCode(DictCodeUpdateReq req) {
        DictCodeDto dictCodeDto = dictCodeService.queryById(req.getId());
        dictCodeDto.setDictValue(req.getDictValue());
        dictCodeDto.setUptUser(LoginUser.getLoginUserName());
        dictCodeService.update(dictCodeDto);
    }

    @Override
    public void deleteDictCode(Long id) {
        dictCodeService.delete(id, LoginUser.getLoginUserName());
    }

    @Override
    public List<DictCodeDto> getByDictId(Long dictId) {
        return dictCodeService.getByDictId(dictId);
    }

    @Override
    public List<DictCodeDto> getDictCode(String systemCode, String dictCode) {
        DictDto dictDto = dictService.getDictCode(systemCode, dictCode);
        if (null == dictCode) {
            return null;
        }
        return dictCodeService.getByDictId(dictDto.getId());
    }
}

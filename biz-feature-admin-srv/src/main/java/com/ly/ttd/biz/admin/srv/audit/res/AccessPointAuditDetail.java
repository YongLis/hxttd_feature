package com.ly.ttd.biz.admin.srv.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.admin.mybatis.entity.AccessPointEntity;
import com.ly.ttd.biz.admin.srv.access.res.AccessPointQueryRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/6/23 21:25
 */
@Data
public class AccessPointAuditDetail extends AuditDetail {


    @Schema(description = "变更前配置")
    private AccessPointQueryRes before;

    @Schema(description = "变更后配置")
    private AccessPointQueryRes after;


    public static AccessPointQueryRes jsonConvert(String json) {
        AccessPointEntity entity = JSON.parseObject(json, AccessPointEntity.class);
        AccessPointQueryRes res = new AccessPointQueryRes();
        res.setId(entity.getId());
        res.setCode(entity.getCode());
        res.setName(entity.getName());
        res.setRemark(entity.getRemark());
        res.setProjectId(entity.getProjectId());
        res.setCrtUser(entity.getCrtUser());
        res.setCrtTime(entity.getCrtTime());
        res.setDeleted(entity.getDeleted());
        res.setParams(entity.apiConvert());
        return res;
    }


}

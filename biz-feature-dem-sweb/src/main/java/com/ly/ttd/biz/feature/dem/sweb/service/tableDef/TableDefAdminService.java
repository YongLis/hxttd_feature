package com.ly.ttd.biz.feature.dem.sweb.service.tableDef;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req.TableDefUpdateReq;

/**
 * 数据表定义 管理服务（接入统一审核）
 *
 * @author yong.li
 * @since 2026-07-14
 */
public interface TableDefAdminService {

    /**
     * 新增表定义（含字段）
     */
    void addTableDef(TableDefAddReq req) throws BizException;

    /**
     * 更新表定义（含字段）
     */
    void updateTableDef(TableDefUpdateReq req) throws BizException;

    /**
     * 删除表定义
     */
    void deleteTableDef(String id) throws BizException;
}

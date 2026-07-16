package com.ly.ttd.biz.feature.dem.sweb.service.user;


import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.BaseRequest;
import com.ly.ttd.biz.feature.dem.sweb.service.user.req.UserLoginReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.res.UserCurrentRes;
import com.ly.ttd.biz.feature.dem.sweb.service.user.res.UserLoginRes;

/**
 * @author yong.li
 * @since 2026/4/13 10:48
 */
public interface UserAdminService {


    /**
     * 登录
     *
     * @param req
     * @return
     */
    UserLoginRes login(UserLoginReq req) throws BizException;

    Boolean logout(BaseRequest req);

    UserLoginRes getCurrentUser() throws BizException;

    /**
     * 获取当前登录用户的完整信息（角色、租户、项目列表）
     *
     * @return 用户当前信息
     */
    UserCurrentRes getCurrentUserInfo() throws BizException;
}

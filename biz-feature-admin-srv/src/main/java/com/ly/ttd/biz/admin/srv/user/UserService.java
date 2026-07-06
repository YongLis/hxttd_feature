package com.ly.ttd.biz.admin.srv.user;

import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.biz.admin.srv.user.req.UserLoginReq;
import com.ly.ttd.biz.admin.srv.user.res.UserCurrentRes;
import com.ly.ttd.biz.admin.srv.user.res.UserLoginRes;
import com.ly.ttd.consts.exception.BizException;

/**
 * @author yong.li
 * @since 2026/4/13 10:48
 */
public interface UserService {


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

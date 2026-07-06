// @ts-ignore
/* eslint-disable */

declare namespace API {
    type CurrentUser = {
        name?: string;
        avatar?: string;
        userid?: string;
        email?: string;
        signature?: string;
        title?: string;
        group?: string;
        tags?: { key?: string; label?: string }[];
        notifyCount?: number;
        unreadCount?: number;
        country?: string;
        access?: string;
        geographic?: {
            province?: { label?: string; key?: string };
            city?: { label?: string; key?: string };
        };
        address?: string;
        phone?: string;
    };

    type LoginResult = {
        sessionId: string;
        userInfo: UserInfo;
    };

    type PageParams = {
        current?: number;
        pageSize?: number;
    };

    type LoginParams = {
        userName?: string;
        password?: string;
        autoLogin?: boolean;
        type?: string;
    };

    type ErrorResponse = {
        /** 业务约定的错误码 */
        errorCode: string;
        /** 业务上的错误信息 */
        errorMessage?: string;
        /** 业务上的请求是否成功 */
        success?: boolean;
    };


    export interface Status {
        code: string;
        message: string;

    }


// 后端统一响应结构
    export interface Result<T> extends Status {
        data: T
    }


    /**
     * 分页结果
     */
    export interface PageResult<T> extends Status {
        data: T[];
        total: number;
        pageSize: number;
        current: number;
    }

    export interface Menu {
        url: string;
        displayName: string;
        icon: string;
        sort: number;
        extField: string;
        pid: number;
        id: number;
    }


    /**
     * 用户信息类型定义
     */
    export interface UserInfo {
        id: number;
        name: string;
        role: 'admin' | 'user';
        nickname: string;
        email: string;
        phone: string;
    }

    /**
     * 当前登录用户完整信息（含租户和项目）
     */
    export interface UserCurrentRes {
        userAccount: string;
        role: string;
        tenantId: number;
        projectIds: number[];
        selectedProjectId: number;
    }


}

// @ts-ignore
/* eslint-disable */
import {API} from './typings';
import {fetchRemotePost} from '@/utils/http';

/** 获取当前的用户 GET /api/currentUser */
export async function currentUser(options?: { [key: string]: any }) {
    return fetchRemotePost('/api/admin/getCurrentUser', {})
}

//退出登录接口
export async function outLogin(options?: { [key: string]: any }) {
    return fetchRemotePost('/api/admin/logout', {})
}

/** 登录接口 POST /api/login/account */
export async function login(body: API.LoginParams) {
    return fetchRemotePost('/api/admin/login', body)
}

/** 获取当前用户完整信息 POST /api/user/getCurrent */
export async function getCurrentUserInfo(options?: { [key: string]: any }) {
    return fetchRemotePost('/api/user/getCurrent', {})
}

/** 根据 header 中 projectId 查询当前项目信息 POST /api/system/getProject */
export async function getCurrentProject(options?: { [key: string]: any }) {
    return fetchRemotePost('/api/system/getProject', {})
}




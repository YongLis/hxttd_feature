import {fetchRemoteGet, fetchRemotePost} from "@/utils/http"

/**
 * 管理员登录
 * @returns 登录结果
 * @param params 登录参数
 */
export function adminLogin(params: any) {
    return fetchRemotePost('/api/admin/login', params)
}

/**
 * 推出登录
 * @returns 推出结果
 */
export function adminLogout() {
    return fetchRemoteGet('/api/admin/logout')
}

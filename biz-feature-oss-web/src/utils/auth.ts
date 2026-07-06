import {API} from "@/services/srv/typings";

/**
 * 获取当前用户信息
 */
export const getCurrentUser = (): API.UserInfo | null => {
    const userStr = localStorage.getItem('userInfo');
    if (userStr) {
        try {
            return JSON.parse(userStr);
        } catch (e) {
            return null;
        }
    }
    return null;
};

/**
 * 判断是否为管理员
 */
export const isAdmin = (): boolean => {
    const user = getCurrentUser();
    return user?.role === 'admin';
};

/**
 * 检查是否有权限访问
 * @param requiredRole 需要的角色
 */
export const hasPermission = (requiredRole: 'admin' | 'user'): boolean => {
    const user = getCurrentUser();
    if (!user) return false;

    // 管理员拥有所有权限
    if (user.role === 'admin') return true;

    // 普通用户只能访问 user 级别
    return user.role === requiredRole;
};

/**
 * 保存用户信息到本地
 */
export const saveUserInfo = (userInfo: API.UserInfo): void => {
    localStorage.setItem('userInfo', JSON.stringify(userInfo));
};

/**
 * 清除用户信息
 */
export const clearUserInfo = (): void => {
    localStorage.removeItem('userInfo');
    localStorage.removeItem('sessionId');
    localStorage.removeItem('username');
};

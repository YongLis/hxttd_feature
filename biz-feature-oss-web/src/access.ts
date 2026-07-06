/**
 * 权限定义文件
 * 用于控制菜单和路由的访问权限
 *
 * 注意：当前已移除所有权限控制，所有菜单对所有用户可见
 */
export default (initialState: any) => {
    // 从 initialState 中获取用户信息
    const {currentUser} = initialState || {};

    console.log('Current user:', currentUser);

    return {
        // 仅 admin 角色才能访问系统配置菜单
        isAdmin: currentUser?.role === 'admin',

        // 判断是否已登录
        isLogin: !!currentUser,
    };
};

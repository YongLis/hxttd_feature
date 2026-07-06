import type {RunTimeLayoutConfig} from '@umijs/max';
import {history, useModel} from '@umijs/max';
import {currentUser} from '@/services/srv/api';
import type {API} from '@/services/srv/typings';
import defaultSettings from '../config/defaultSettings';
import {Avatar, Button, Space} from 'antd';
import {LoginOutlined, UserOutlined} from '@ant-design/icons';

/**
 * 获取初始状态
 * 应用启动时执行，用于获取用户信息
 */
export async function getInitialState(): Promise<{
    currentUser?: API.UserInfo;
    fetchUserInfo?: () => Promise<API.UserInfo | undefined>;
}> {
    // 定义获取用户信息的方法
    const fetchUserInfo = async () => {
        try {
            const response = await currentUser() as API.Result<API.LoginResult>;
            // 获取用户信息
            const userData = response.data?.userInfo;
            if (userData) {
                // 转换为 UserInfo 格式
                return {
                    id: userData.id || 0,
                    name: userData.nickname || userData.name || '',
                    role: (userData.role || 'user') as 'admin' | 'user',
                    nickname: userData.nickname || '',
                    email: userData.email || '',
                    phone: userData.phone || '',
                };
            }
            return undefined;
        } catch (error) {
            console.error('获取用户信息失败:', error);
            return undefined;
        }
    };

    // 如果已登录（localStorage 中有 sessionId），则获取用户信息
    const sessionId = localStorage.getItem('sessionId');
    if (sessionId) {
        const userInfo = await fetchUserInfo();
        return {
            currentUser: userInfo,
            fetchUserInfo,
        };
    }

    // 未登录时只返回 fetchUserInfo 方法
    return {
        fetchUserInfo,
    };
}

// 运行时菜单配置
export const layout: RunTimeLayoutConfig = () => {
    const {initialState} = useModel('@@initialState');
    const {currentUser} = initialState || {};

    return {
        // 继承默认配置
        ...defaultSettings,
        // 菜单配置 - 根据权限动态显示
        menu: {
            // 动态菜单数据
            locale: false,
        },
        // 菜单footer区域，可放置额外内容
        menuFooterRender: (props) => {
            return null;
        },
        // 自定义右侧内容渲染
        rightContentRender: () => {
            return (
                <Space>
                    {currentUser && currentUser.name ? (
                        <span style={{display: 'flex', alignItems: 'center', cursor: 'pointer', padding: '4px 8px'}}>
              <Avatar size="small" icon={<UserOutlined/>}/>
              <span style={{marginLeft: '8px'}}>{currentUser.name}</span>
            </span>
                    ) : (
                        <Button
                            type="text"
                            icon={<LoginOutlined/>}
                            onClick={() => history.push('/user/login')}
                        >
                            登录
                        </Button>
                    )}
                </Space>
            );
        },
    };
};



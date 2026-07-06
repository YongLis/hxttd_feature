import {LockOutlined, UserOutlined,} from '@ant-design/icons';
import {LoginForm, ProFormText,} from '@ant-design/pro-components';
import {FormattedMessage, Helmet, useIntl, useModel,} from '@umijs/max';
import {App} from 'antd';
import React, {useState} from 'react';
import {flushSync} from 'react-dom';
import {Footer} from '@/components';
import {getCurrentUserInfo, login} from '@/services/srv/api';
import Settings from '../../../../config/defaultSettings';
import {API} from '@/services/srv/typings';

const Login: React.FC = () => {
    const [userLoginState, setUserLoginState] = useState<string>('');
    const {initialState, setInitialState} = useModel('@@initialState');
    const {message} = App.useApp();
    const intl = useIntl();

    const fetchUserInfo = async () => {
        const userInfo = await initialState?.fetchUserInfo?.();
        if (userInfo) {
            flushSync(() => {
                setInitialState((s) => ({
                    ...s,
                    currentUser: userInfo,
                }));
            });
        }
    };

    const handleSubmit = async (values: API.LoginParams) => {
        try {
            // 登录
            const msg = await login({...values}) as API.Result<API.LoginResult>;

            console.log('login res')
            console.log(msg)

            if (msg.code === '0000') {
                // 保存 sessionId 和用户信息到 localStorage
                if (msg.data.sessionId) {
                    localStorage.setItem('sessionId', msg.data.sessionId);
                }
                if (msg.data.userInfo) {
                    localStorage.setItem('userInfo', JSON.stringify(msg.data.userInfo));
                    localStorage.setItem('username', msg.data.userInfo.userName || values.userName || '');
                    localStorage.setItem('role', msg.data.userInfo.role || 'user');
                }

                // 调用 getCurrent 获取租户、项目等完整信息
                try {
                    const currentRes = await getCurrentUserInfo() as API.Result<API.UserCurrentRes>;
                    if (currentRes.code === '0000' && currentRes.data) {
                        // localStorage.setItem('tenantId', String(currentRes.data.tenantId || ''));
                        localStorage.setItem('projectIds', JSON.stringify(currentRes.data.projectIds || []));
                        localStorage.setItem('selectedProjectId', String(currentRes.data.selectedProjectId || ''));
                    }
                } catch (e) {
                    console.error('获取用户当前信息失败:', e);
                }

                await fetchUserInfo();
                const urlParams = new URL(window.location.href).searchParams;
                // 登录成功后跳转到首页
                window.location.href = urlParams.get('redirect') || '/home/index';
                return;
            }
            console.log(msg);
            // 如果失败去设置用户错误信息
            setUserLoginState(msg.code);
        } catch (error) {
            console.log(error);
            message.error('登录失败，请重试！');
        }
    };

    return (
        <div
            style={{
                display: 'flex',
                flexDirection: 'column',
                height: '100vh',
                overflow: 'auto',
                backgroundImage:
                    "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
                backgroundSize: '100% 100%',
            }}
        >
            <Helmet>
                <title>
                    {intl.formatMessage({
                        id: 'menu.login',
                        defaultMessage: '登录页',
                    })}
                    {Settings.title && ` - ${Settings.title}`}
                </title>
            </Helmet>
            <div
                style={{
                    flex: '1',
                    padding: '32px 0',
                }}
            >
                <LoginForm
                    contentStyle={{
                        minWidth: 280,
                        maxWidth: '75vw',
                    }}
                    initialValues={{
                        autoLogin: true,
                    }}
                    onFinish={async (values) => {
                        await handleSubmit(values as API.LoginParams);
                    }}
                >
                    {userLoginState !== '0000' && userLoginState !== '' && (
                        <div
                            style={{
                                marginBottom: 24,
                                padding: '12px',
                                background: '#fff2f0',
                                border: '1px solid #ffccc7',
                                borderRadius: '4px',
                            }}
                        >
              <span style={{color: '#ff4d4f'}}>
                {intl.formatMessage({
                    id: 'pages.login.accountLogin.errorMessage',
                    defaultMessage: '账户或密码错误',
                })}
              </span>
                        </div>
                    )}
                    <ProFormText
                        name="userName"
                        fieldProps={{
                            size: 'large',
                            prefix: <UserOutlined/>,
                        }}
                        placeholder={intl.formatMessage({
                            id: 'pages.login.username.placeholder',
                            defaultMessage: '请输入用户名',
                        })}
                        rules={[
                            {
                                required: true,
                                message: (
                                    <FormattedMessage
                                        id="pages.login.username.required"
                                        defaultMessage="请输入用户名!"
                                    />
                                ),
                            },
                        ]}
                    />
                    <ProFormText.Password
                        name="password"
                        fieldProps={{
                            size: 'large',
                            prefix: <LockOutlined/>,
                        }}
                        placeholder={intl.formatMessage({
                            id: 'pages.login.password.placeholder',
                            defaultMessage: '请输入密码',
                        })}
                        rules={[
                            {
                                required: true,
                                message: (
                                    <FormattedMessage
                                        id="pages.login.password.required"
                                        defaultMessage="请输入密码！"
                                    />
                                ),
                            },
                        ]}
                    />
                </LoginForm>
            </div>
            <Footer/>
        </div>
    );
};

export default Login;

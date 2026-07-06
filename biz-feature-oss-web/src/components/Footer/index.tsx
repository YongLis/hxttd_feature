import {DefaultFooter} from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
    return (
        <DefaultFooter
            style={{
                background: 'none',
            }}
            copyright="淘淘工作室"
            links={[
                {
                    key: 'ttd-space',
                    title: '淘淘团队',
                    href: 'https://pro.ant.design',
                    blankTarget: true,
                },
                // {
                //   key: 'github',
                //   title: <GithubOutlined />,
                //   href: 'https://github.com/ant-design/ant-design-pro',
                //   blankTarget: true,
                // },
                {
                    key: 'benan',
                    title: '备案号XXXXX',
                    href: 'https://ant.design',
                    blankTarget: true,
                },
            ]}
        />
    );
};

export default Footer;

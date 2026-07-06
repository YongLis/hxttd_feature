import React from 'react';
import {PageContainer} from '@ant-design/pro-components';
import {Divider, Typography} from 'antd';
import styles from './PageContainer.less';

const {Text} = Typography;

interface PageContainerProps {
    children: React.ReactNode;
    title?: string | false;
    subTitle?: string;
    extra?: React.ReactNode[];

    [key: string]: any;
}

/**
 * 封装的 PageContainer 组件
 * 包含底部授权备案信息
 */
const CustomPageContainer: React.FC<PageContainerProps> = ({
                                                               children,
                                                               title,
                                                               subTitle,
                                                               extra,
                                                               ...restProps
                                                           }) => {
    return (
        <div className={styles.customPageContainer}>
            <PageContainer
                title={title}
                subTitle={subTitle}
                extra={extra}
                {...restProps}
                style={{
                    maxWidth: '1200px',
                    overflowX: 'auto'
                }}
            >
                {children}
            </PageContainer>

            {/* 底部授权备案信息 */}
            <div className={styles.footer}>
                <Divider style={{margin: '12px 0'}}/>
                <div className={styles.footerContent}>
                    <Text type="secondary" style={{fontSize: '12px'}}>
                        Copyright © {new Date().getFullYear()} 极客工坊. All Rights Reserved.
                    </Text>
                    <Divider type="vertical" style={{margin: '0 8px'}}/>
                    <Text type="secondary" style={{fontSize: '12px'}}>
                        <a href="https://beian.miit.gov.cn/" target="_blank" rel="noopener noreferrer">
                            沪ICP备2026020321号
                        </a>
                    </Text>
                    <Divider type="vertical" style={{margin: '0 8px'}}/>
                    <Text type="secondary" style={{fontSize: '12px'}}>
                        <a href="http://www.beian.gov.cn/" target="_blank" rel="noopener noreferrer">
                            京公网安备XXXXXXXXXXX号
                        </a>
                    </Text>
                </div>
            </div>
        </div>
    );
};

export default CustomPageContainer;

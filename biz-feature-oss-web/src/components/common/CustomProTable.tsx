import React from 'react';
import type {ProTableProps} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';

/**
 * 封装 ProTable — 默认启用水平滚动，最小宽度 1200px
 *
 * 用法：与 ProTable 完全一致，额外支持：
 *   - scrollX: 自定义水平滚动最小宽度（默认 1200）
 */
function CustomProTable<T extends Record<string, any>>(
    props: Omit<ProTableProps<T, any>, 'scroll'> & { scrollX?: number; scroll?: ProTableProps<T, any>['scroll'] },
) {
    const {scrollX = 1200, scroll, style, ...rest} = props;

    return (
        <div style={{overflowX: 'auto', minWidth: '100%'}}>
            <ProTable<T>
                {...(rest as ProTableProps<T, any>)}
                scroll={{x: scrollX, ...scroll}}
                style={{minWidth: scrollX, ...style}}
                size='small'
            />
        </div>
    );
}

export default CustomProTable;

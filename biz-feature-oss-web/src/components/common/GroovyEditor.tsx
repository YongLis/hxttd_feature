import React, {useEffect, useMemo, useState} from 'react';
import CodeMirror from '@uiw/react-codemirror';
import {loadLanguage} from '@uiw/codemirror-extensions-langs';
import {autocompletion, CompletionContext} from '@codemirror/autocomplete';
import {fetchRemoteGet} from '@/utils/http';

/** 后端返回的函数提示项 */
interface FunctionTip {
    group: string;
    methodName: string;
    description: string;
    params: { type: string; name: string }[];
    returnObj: string;
    shortcutCode: string;
    info: string;
}

interface Prop {
    val?: string;
    editable: boolean;
    height?: string;

    onChange(value: string): void;
}

const GroovyEditor: React.FC<Prop> = ({val, onChange, editable, height}) => {
    const groovyLang = loadLanguage('groovy');
    const [functionTips, setFunctionTips] = useState<FunctionTip[]>([]);

    // 组件挂载时从后端获取函数提示数据
    useEffect(() => {
        fetchRemoteGet<FunctionTip[]>('/api/system/getFunctionTips?language=groovy')
            .then((res: any) => {
                if (res?.data) {
                    setFunctionTips(res.data);
                }
            })
            .catch((err: any) => console.error('获取函数提示失败:', err));
    }, []);

    // 构建 group -> methods[] 的映射，方便快速查找
    const groupMap = useMemo(() => {
        const map = new Map<string, FunctionTip[]>();
        functionTips.forEach((tip) => {
            if (!map.has(tip.group)) {
                map.set(tip.group, []);
            }
            map.get(tip.group)!.push(tip);
        });
        return map;
    }, [functionTips]);

    /**
     * 自定义补全源：根据光标上下文返回补全建议
     * - 输入 "math." → 展示该 group 下的所有方法
     * - 输入 "math.a" → 过滤以 "a" 开头的方法
     * - 输入普通单词 → 展示匹配的 group 名称
     */
    function groovyCompletions(context: CompletionContext) {
        // 场景1：检测到 "group.methodName" 模式（输入了 . 或 . + 部分方法名）
        const dotMatch = context.matchBefore(/\w+\.\w*/);
        if (dotMatch) {
            const text = dotMatch.text;
            const dotIndex = text.indexOf('.');
            const group = text.slice(0, dotIndex);
            const partialMethod = text.slice(dotIndex + 1);

            const methods = groupMap.get(group);
            if (methods && methods.length > 0) {
                const filtered = partialMethod
                    ? methods.filter((m) => m.methodName.startsWith(partialMethod))
                    : methods;

                if (filtered.length === 0) return null;

                return {
                    from: dotMatch.from + dotIndex + 1, // 从 . 之后开始替换
                    options: filtered.map((m) => ({
                        label: m.methodName,
                        type: 'function',
                        detail: m.description,
                        info: m.info,
                        apply: m.shortcutCode,
                    })),
                    validFor: /^\w*$/,
                };
            }
            return null;
        }

        // 场景2：普通单词输入，提示 group 名称
        const wordMatch = context.matchBefore(/\w*/);
        if (!wordMatch || (wordMatch.text === '' && !context.explicit)) return null;

        const groups = Array.from(groupMap.keys());
        const options = groups
            .filter((g) => g.startsWith(wordMatch.text))
            .map((g) => ({
                label: g,
                type: 'namespace',
                detail: '函数分组',
                apply: g + '.', // 选中后自动补上 "."，触发下一级方法提示
            }));

        if (options.length === 0) return null;

        return {
            from: wordMatch.from,
            options,
            validFor: /^\w*$/,
        };
    }

    // 组装 extensions，当 groupMap 变化时重建
    const extensions = useMemo(() => {
        const exts: any[] = [];
        if (groovyLang) {
            exts.push(groovyLang);
        }
        exts.push(
            autocompletion({
                override: [groovyCompletions],
                // 键入任意字符（含 .）时触发自动补全
                activateOnTyping: true,
                // 选中补全后立即重新触发查询（如选 "math" → 插入 "math." → 自动弹出方法列表）
                activateOnCompletion: () => true,
            }),
        );
        return exts;
    }, [groovyLang, groupMap]);

    return (
        <CodeMirror
            style={{width: '100%', height: '100%'}}
            value={val}
            height={height || '300px'}
            theme="dark"
            editable={editable}
            extensions={extensions}
            onChange={onChange}
        />
    );
};

export default GroovyEditor;

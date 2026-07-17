import React, {useEffect, useMemo, useState} from 'react';
import {
    Background,
    Controls,
    Handle,
    MarkerType,
    MiniMap,
    type NodeProps,
    Position,
    ReactFlow,
    useEdgesState,
    useNodesState,
} from '@xyflow/react';
import '@xyflow/react/dist/style.css';
import {Modal, Result, Spin} from 'antd';
import dagre from 'dagre';
import {type DependencyEdge, type DependencyNode, queryDependency} from '@/services/srv/dependencyApi';
import {DEFAULT_NODE_COLOR, NODE_TYPE_COLOR, NODE_TYPE_LABEL} from './consts';
import './index.less';

// ==================== Types ====================

export interface LineageGraphProps {
    /** 资源标识键 */
    resourceKey: string;
    /** 资源类型 (FACTOR/META/FEATURE 等) */
    resourceType: string;
    visible: boolean;
    onCancel?: () => void;
    /** 标题（可选） */
    title?: string;
    /** 容器高度 */
    height?: number | string;
}

interface CustomNodeData {
    label: string;
    nodeType: string;
    color: string;
    typeLabel: string;
}

// ==================== Constants ====================

const NODE_WIDTH = 100;
const NODE_HEIGHT = 50;

// ==================== Custom Node ====================

function CustomNode({data}: NodeProps) {
    const nodeData = data as unknown as CustomNodeData;
    return (
        <div
            className="lineage-node"
            style={{borderColor: nodeData.color}}
        >
            <Handle type="target" position={Position.Top}/>
            <div className="lineage-node-type" style={{backgroundColor: nodeData.color}}>
                {nodeData.typeLabel}
            </div>
            <div className="lineage-node-label">{nodeData.label}</div>
            <Handle type="source" position={Position.Bottom}/>
        </div>
    );
}

const nodeTypes = {custom: CustomNode};

// ==================== Layout ====================

function getLayoutedElements(
    nodes: DependencyNode[],
    edges: DependencyEdge[],
) {
    const g = new dagre.graphlib.Graph();
    g.setDefaultEdgeLabel(() => ({}));
    g.setGraph({rankdir: 'TB', nodesep: 80, ranksep: 120});

    for (const node of nodes) {
        g.setNode(node.nodeId, {width: NODE_WIDTH, height: NODE_HEIGHT});
    }

    for (const edge of edges) {
        g.setEdge(edge.from, edge.to);
    }

    dagre.layout(g);

    const layoutedNodes = nodes.map((node) => {
        const pos = g.node(node.nodeId);
        return {
            id: node.nodeId,
            type: 'custom',
            position: {
                x: pos.x - NODE_WIDTH / 2,
                y: pos.y - NODE_HEIGHT / 2,
            },
            data: {
                label: node.label,
                nodeType: node.nodeType,
                color: NODE_TYPE_COLOR[node.nodeType] || DEFAULT_NODE_COLOR,
                typeLabel: NODE_TYPE_LABEL[node.nodeType] || node.nodeType,
            } satisfies CustomNodeData,
        };
    });

    const layoutedEdges = edges.map((edge) => ({
        id: edge.edgeId,
        source: edge.from,
        target: edge.to,
        animated: true,
        markerEnd: {type: MarkerType.ArrowClosed, color: '#b1b1b7'},
        style: {stroke: '#b1b1b7', strokeWidth: 1.5},
    }));

    return {nodes: layoutedNodes, edges: layoutedEdges};
}

// ==================== Main Component ====================

const LineageGraph: React.FC<LineageGraphProps> = ({
                                                       resourceKey,
                                                       resourceType,
                                                       visible,
                                                       onCancel,
                                                       title
                                                   }) => {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [rawNodes, setRawNodes] = useState<DependencyNode[]>([]);
    const [rawEdges, setRawEdges] = useState<DependencyEdge[]>([]);

    useEffect(() => {
        if (!resourceKey || !resourceType) return;
        setLoading(true);
        setError(null);
        queryDependency(resourceKey, resourceType)
            .then((res: any) => {
                if (res?.code === '0000' && res.data) {
                    setRawNodes(res.data.nodes || []);
                    setRawEdges(res.data.edges || []);
                } else {
                    setError(res?.message || '血缘查询失败');
                }
            })
            .catch((e: any) => {
                setError(e?.message || '血缘查询失败');
            })
            .finally(() => setLoading(false));
    }, [resourceKey, resourceType]);

    const {nodes: flowNodes, edges: flowEdges} = useMemo(
        () => getLayoutedElements(rawNodes, rawEdges),
        [rawNodes, rawEdges],
    );

    const [nodes, setNodes, onNodesChange] = useNodesState(flowNodes as any);
    const [edges, setEdges, onEdgesChange] = useEdgesState(flowEdges as any);

    useEffect(() => {
        setNodes(flowNodes as any);
        setEdges(flowEdges as any);
    }, [flowNodes, flowEdges, setNodes, setEdges]);

    // ==================== Loading ====================
    const height = 500
    if (loading) {
        return (
            <div className="lineage-container" style={{height}}>
                <Spin tip="加载血缘关系中..."
                      style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%'}}>
                    <div style={{height: '100%'}}/>
                </Spin>
            </div>
        );
    }

    // ==================== Error ====================
    if (error) {
        return (
            <div className="lineage-container" style={{height}}>
                <Result status="error" title="血缘查询失败" subTitle={error}/>
            </div>
        );
    }

    // ==================== Empty ====================
    if (rawEdges.length === 0) {
        return (
            <div className="lineage-container" style={{height}}>
                {title && <div className="lineage-title">{title}</div>}
                <Result
                    status="info"
                    title="暂无血缘关系"
                    subTitle={`当前 ${NODE_TYPE_LABEL[resourceType] || resourceType} 未关联上下游依赖`}
                />
            </div>
        );
    }

    // ==================== Normal ====================
    return (
        <Modal width='90vw' style={{height: '90vh'}} centered open={visible} onCancel={onCancel}>
            <div className="lineage-container" style={{height: 'calc(90vh - 80px)'}}>
                {title && <div className="lineage-title">{title}</div>}
                <div className="lineage-flow-wrapper">
                    <ReactFlow
                        nodes={nodes}
                        edges={edges}
                        onNodesChange={onNodesChange}
                        onEdgesChange={onEdgesChange}
                        nodeTypes={nodeTypes}
                        fitView
                        attributionPosition="bottom-left"
                    >
                        <Background color="#f0f0f0" gap={16}/>
                        <Controls/>
                        <MiniMap
                            nodeColor={(n) => (n.data as unknown as CustomNodeData)?.color || DEFAULT_NODE_COLOR}
                            maskColor="rgba(0,0,0,0.08)"
                        />
                    </ReactFlow>
                </div>
            </div>
        </Modal>
    );
};

export default LineageGraph;

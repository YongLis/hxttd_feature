import React, {useEffect, useState} from 'react';
import {Card, Col, Empty, Row, Spin, Tag, Typography} from 'antd';
import {CheckCircleOutlined, ProjectOutlined,} from '@ant-design/icons';
import {history} from '@umijs/max';
import {PageContainer} from '@ant-design/pro-layout';
import {getAllProject, ProjectDetail} from '@/services/srv/projectApi';
import styles from './Home.less';

const {Title, Text} = Typography;

interface ProjectCardProps {
    project: ProjectDetail;
    isSelected: boolean;
    onClick: () => void;
}

/**
 * 项目卡片组件
 */
const ProjectCard: React.FC<ProjectCardProps> = ({project, isSelected, onClick}) => {
    return (
        <Card
            className={`${styles.projectCard} ${isSelected ? styles.selectedCard : ''}`}
            hoverable
            onClick={onClick}
        >
            <div className={styles.cardHeader}>
                <ProjectOutlined className={styles.projectIcon}/>
                <div className={styles.projectTitle}>
                    <Title level={5} ellipsis={{rows: 1, tooltip: project.name}}>
                        {project.name}
                    </Title>
                    <Text type="secondary" className={styles.projectCode}>
                        {project.projectCode}
                    </Text>
                    {isSelected && (
                        <Tag icon={<CheckCircleOutlined/>} color="blue" className={styles.selectedTag}>
                            当前项目
                        </Tag>
                    )}
                </div>
            </div>
        </Card>
    );
};

/**
 * 首页 - 项目卡片展示
 */
const Home: React.FC = () => {
    const [projects, setProjects] = useState<ProjectDetail[]>([]);
    const [loading, setLoading] = useState(true);
    const [selectedProjectId, setSelectedProjectId] = useState<number | null>(null);
    const [selectedProjectCode, setSelectedProjectCode] = useState<string>('');

    useEffect(() => {
        loadProjects();
    }, []);

    /**
     * 加载用户项目列表
     */
    const loadProjects = async () => {
        setLoading(true);
        try {
            const projectIdsStr = localStorage.getItem('projectIds') || '[]';
            const storedSelectedId = localStorage.getItem('selectedProjectId');
            const storedProjectCode = localStorage.getItem('selectedProjectCode') || '';
            const projectIds: number[] = JSON.parse(projectIdsStr);

            if (storedSelectedId) {
                setSelectedProjectId(Number(storedSelectedId));
            }
            if (storedProjectCode) {
                setSelectedProjectCode(storedProjectCode);
            }

            const res = await getAllProject();
            if (res.code === '0000' && res.data) {
                if (projectIds.length > 0) {
                    const userProjects = res.data.filter(p => projectIds.includes(p.id));
                    setProjects(userProjects);
                } else {
                    setProjects(res.data);
                }
            }
        } catch (error) {
            console.error('加载项目列表失败:', error);
        } finally {
            setLoading(false);
        }
    };

    /**
     * 选中项目 - 缓存项目信息并跳转到接入点管理
     */
    const handleSelectProject = (project: ProjectDetail) => {
        setSelectedProjectId(project.id);
        setSelectedProjectCode(project.projectCode);
        localStorage.setItem('selectedProjectId', String(project.id));
        localStorage.setItem('selectedProjectCode', project.projectCode);

        // 触发全局事件通知其他组件
        window.dispatchEvent(new CustomEvent('projectChanged', {
            detail: {projectId: project.id, projectCode: project.projectCode}
        }));

        // 跳转到接入点管理
        history.push('/access-point/index');
    };

    return (
        <PageContainer title={false}>
            <div className={styles.homeContainer}>
                <div className={styles.header}>
                    <Title level={3} className={styles.pageTitle}>
                        <ProjectOutlined/> 我的项目
                    </Title>
                    <Text type="secondary">
                        共 {projects.length} 个项目
                    </Text>
                </div>

                {loading ? (
                    <div className={styles.loadingContainer}>
                        <Spin size="large" tip="加载项目中..."/>
                    </div>
                ) : projects.length === 0 ? (
                    <div className={styles.emptyContainer}>
                        <Empty description="暂无项目，请联系管理员分配"/>
                    </div>
                ) : (
                    <Row gutter={[16, 16]} className={styles.cardGrid}>
                        {projects.map(project => (
                            <Col key={project.id} xs={24} sm={12} md={8} lg={6}>
                                <ProjectCard
                                    project={project}
                                    isSelected={project.id === selectedProjectId}
                                    onClick={() => handleSelectProject(project)}
                                />
                            </Col>
                        ))}
                    </Row>
                )}
            </div>
        </PageContainer>
    );
};

export default Home;

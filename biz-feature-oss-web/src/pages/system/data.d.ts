export interface TenantDetail {
    id: number;
    name: string;
    crtTime?: string;
    crtUser?: string;
}

export interface ProjectDetail {
    id: number;
    projectCode: string;
    name: string;
    crtTime?: string;
    crtUser?: string;
    userCount: number;
}

export interface AccountDetail {
    id: number;
    userAccount: string;
    crtTime?: string;
    crtUser?: string;
}

export interface ProjectMemberDetail {
    id?: number;
    projectId?: number;
    userAccount?: string;
    userName?: string;
    crtUser?: string;
    uptUser?: string;
    crtTime?: string;
    uptTime?: string;
    deleted: boolean;
}

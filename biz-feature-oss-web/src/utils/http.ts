import {API} from "@/services/srv/typings"
import {request} from "@umijs/max"

/**
 * 获取请求头，包含 sessionId
 * @returns 请求头对象
 */
function getHeaders() {
    const sessionId = localStorage.getItem('sessionId') || ''
    const tenantId = localStorage.getItem('tenantId') || ''
    const projectId = localStorage.getItem('selectedProjectId') || ''
    const projectCode = localStorage.getItem('selectedProjectCode') || ''
    return {
        'Content-Type': 'application/json',
        'X-Session-Id': sessionId,
        'X-Tenant-Id': tenantId,
        'X-Project-Id': projectId,
        'X-Project-Code': projectCode,
    }
}

/**
 * 通用分页查询(Post 请求)
 * @param url 请求地址
 * @param params 请求参数
 * @param pageNo 页码
 * @param pageSize 每页数量
 * @returns 分页数据
 */

export const fetchPageRemote = <T, >(
    url: string,
    params: any) => {
    return request(url, {
        method: "POST",
        headers: {...getHeaders()},
        data: {
            ...params
        },
        timeout: 5000,
        skipErrorHandler: true,
        getResponse: false,
    });
}


export const RemoteRequestPost = <T, >(
    url: string,
    params: any) => {
    return request(url, {
        method: "POST",
        headers: {...getHeaders()},
        data: {
            ...params
        },
        timeout: 5000,
        skipErrorHandler: true,
        getResponse: false,
    });
}

export const RemoteRequestGet = <T, >(
    url: string) => {
    return request(url, {
        method: "GET",
        headers: {...getHeaders()},
        timeout: 5000,
        skipErrorHandler: true,
        getResponse: false,
    });
}


export const fetchRemotePost = <T, >(
    url: string,
    params: any) => {
    return new Promise<API.Result<T>>((resolve, reject) => {
        const sessionId = localStorage.getItem('sessionId') || ''
        const projectId = localStorage.getItem('selectedProjectId') || ''
        const projectCode = localStorage.getItem('selectedProjectCode') || ''

        request(url, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'X-Session-Id': sessionId,
                'X-Project-Id': projectId,
                'X-Project-Code': projectCode,
            },
            data: {
                ...params,
            },
            timeout: 5000,
            skipErrorHandler: true,
            getResponse: false,
        }).then((res: any) => {
            resolve(res)
        })
            .catch((err: any) => {
                alert('系统异常，请稍后再试')
                console.error(err)
                reject(err)
            })
    })
}


export const fetchRemoteGet = <T, >(url: string) => {
    return new Promise<API.Result<T>>((resolve, reject) => {
        const sessionId = localStorage.getItem('sessionId') || ''
        // const tenantId = localStorage.getItem('tenantId') || ''
        const projectId = localStorage.getItem('selectedProjectId') || ''
        const projectCode = localStorage.getItem('selectedProjectCode') || ''
        request(url, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'X-Session-Id': sessionId,
                // 'X-Tenant-Id': tenantId,
                'X-Project-Id': projectId,
                'X-Project-Code': projectCode,
            },
            timeout: 5000,
            skipErrorHandler: true,
            getResponse: false,
        }).then((res: any) => {
            resolve(res)
        })
            .catch((err: any) => {
                alert('系统异常，请稍后再试')
                console.error(err)
                reject(err)
            })
    })
}
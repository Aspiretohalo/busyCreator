import axios, { AxiosInstance } from "axios";

const isDev = process.env.NODE_ENV === 'development';

const myAxios: AxiosInstance = axios.create({
    baseURL: isDev ? 'http://localhost:8101/api' : 'api',
    withCredentials: true,
});

myAxios.interceptors.request.use(function (config) {
    // 添加 token
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, function (error) {
    return Promise.reject(error);
});

myAxios.interceptors.response.use(function (response: any) {
    // 未登录则跳转到登录页
    if (response?.data?.code === 40100) {
        const redirectUrl = window.location.href;
        window.location.href = `/login?redirect=${redirectUrl}`;
    }
    return response;
}, function (error) {
    return Promise.reject(error);
});

export default myAxios;

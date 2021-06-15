import axios from 'axios';

const HOST = 'https://amazing-store-backend.azurewebsites.net';

export const GOOGLE_LOGIN_URL = 'https://amazing-store-backend.azurewebsites.net/authenticate/google';
export const GITHUB_LOGIN_URL = 'https://amazing-store-backend.azurewebsites.net/authenticate/github';

export const registerUser = async (email: string, password: string) => {
    return axios.post(`${HOST}/api/user/sign-up`, {
        email: email,
        password: password
    });
};

export const loginUser = async (email: string, password: string) => {
    return axios.post(`${HOST}/api/user/sign-in`, {
        email: email,
        password: password
    });
};

export const logoutUser = async (email: string, password: string) => {
    return axios.post(`${HOST}/api/user/sign-out`, {
        email: email,
        password: password
    });
};

export const getUser = async (userId: number) => {
    return axios.get(`${HOST}/api/user/get-by-id/${userId}`);
};



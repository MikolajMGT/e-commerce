import axios from 'axios';

const HOST = 'https://amazing-store-backend.azurewebsites.net';

export const listCategories = async () => {
    return axios.get(`${HOST}/api/category/list`);
};

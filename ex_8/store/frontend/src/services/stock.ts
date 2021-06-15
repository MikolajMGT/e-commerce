import axios from 'axios';

const HOST = 'https://amazing-store-backend.azurewebsites.net';

export const listStocks = async () => {
    return axios.get(`${HOST}/api/product-stock/list`);
};

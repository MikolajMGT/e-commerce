import axios from 'axios';

const HOST = 'https://amazing-store-backend.azurewebsites.net';

export const listProducts = async () => {
    return axios.get(`${HOST}/api/product/list`);
};

export const listProductsByOrderId = async (orderId: number) => {
    return axios.get(`${HOST}/api/order-product/list-products-by-order/${orderId}`);
};

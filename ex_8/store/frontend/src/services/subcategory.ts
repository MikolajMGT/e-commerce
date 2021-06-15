import axios from 'axios';

const HOST = 'https://amazing-store-backend.azurewebsites.net';

export const listSubcategories = async () => {
    return axios.get(`${HOST}/api/subcategory/list`);
};

import axios from 'axios';

const HOST = 'https://amazing-store-backend.azurewebsites.net';

export const addUserAddress = async (userId: number, firstname: string, lastname: string, address: string, zipcode: string, city: string, country: string) => {
    return axios.post(`${HOST}/api/user-address/create`, {
        userId: userId,
        firstname: firstname,
        lastname: lastname,
        address: address,
        zipcode: zipcode,
        city: city,
        country: country
    });
};

export const listUserAddresses = async (userId: number) => {
    return axios.get(`${HOST}/api/user-address/list-by-user/${userId}`);
};

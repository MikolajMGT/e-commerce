import axios from 'axios';

const HOST = 'https://amazing-store-backend.azurewebsites.net';

export const addCreditCard = async (userId: number, cardholderName: string, number: string, expDate: string, cvcCode: string) => {
    return axios.post(`${HOST}/api/credit-card/create`, {
        userId: userId,
        cardholderName: cardholderName,
        number: number,
        expDate: expDate,
        cvcCode: cvcCode
    });
};

export const listUserCreditCards = async (userId: number) => {
    return axios.get(`${HOST}/api/credit-card/list-by-user/${userId}`);
};

import axios from 'axios';

const HOST = 'http://localhost:9000'

export const addCreditCard = async (cardholderName: string, number: string, expDate: string, cvcCode: string) => {
	return axios.post(`${HOST}/api/credit-card/create`, {
		cardholderName: cardholderName,
		number: number,
		expDate: expDate,
		cvcCode: cvcCode
	})
}

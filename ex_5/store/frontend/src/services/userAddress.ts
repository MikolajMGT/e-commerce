import axios from 'axios';

const HOST = 'http://localhost:9000'

export const addUserAddress = async (firstname: string, lastname: string, address: string, zipcode: string, city: string, country: string) => {
	return axios.post(`${HOST}/api/user-address/create`, {
		firstname: firstname,
		lastname: lastname,
		address: address,
		zipcode: zipcode,
		city: city,
		country: country,
	})
}

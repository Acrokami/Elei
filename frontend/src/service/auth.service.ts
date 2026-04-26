import axios from 'axios';

const API_URL = 'http://localhost:8080'

class AuthService{
    async login(username: string, password: string) {
        const response = await axios.post(API_URL + "/login", {
            username,
            password
        });

        if(response.data.token) {
            localStorage.setItem('user_token', response.data.token)
        }

        return response.data;
    }
}

export default new AuthService();

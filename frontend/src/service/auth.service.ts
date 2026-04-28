import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/'

class AuthService{

     async register(username:string, email:string,password:string) {
        const registerResponse = await axios.post(API_URL + "register", {
            username,
            email,
            password
        });

        return registerResponse.data
    }


    async login(username: string, password: string) {
        const loginResponse = await axios.post(API_URL + "login", {
            username,
            password
        });

        if(loginResponse.data.token) {
            localStorage.setItem('user_token', loginResponse.data.token)
        }

        return loginResponse.data;
    }

}

export default new AuthService();

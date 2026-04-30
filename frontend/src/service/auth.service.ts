import api from "./api";


class AuthService {

     async register(username:string, email:string,password:string) {
        const registerResponse = await api.post("auth/register", {
            username,
            email,
            password
        });

        if(registerResponse.data.token) {
            localStorage.setItem('user_token', registerResponse.data.token);
        }

        return registerResponse.data
    }


    async login(username: string, password: string) {
        const loginResponse = await api.post("auth/login", {
            username,
            password
        });

        if(loginResponse.data.token) {
            localStorage.setItem('user_token', loginResponse.data.token);
            localStorage.setItem('username', loginResponse.data.username)
        }

        return loginResponse.data;
    }

}

export default new AuthService();

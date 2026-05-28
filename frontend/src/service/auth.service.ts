import api from "./api";
import type { components } from '../types/api-schemas';


export type RegisterRequest = components['schemas']['RegisterRequest'];
export type RegisterResponse = components['schemas']['RegisterResponse'];
export type LoginRequest = components['schemas']['LoginRequest'];
export type LoginResponse = components['schemas']['LoginResponse'];

class AuthService {

    async register(username: string, email: string, password: string): Promise<RegisterResponse> {
        const payload: RegisterRequest = {
            username,
            email,
            password
        };

        const response = await api.post<RegisterResponse>("auth/register", payload);


        if (response.data.token) {
            localStorage.setItem('user_token', response.data.token);
        }

        return response.data;
    }

    async login(username: string, password: string): Promise<LoginResponse> {
        const payload: LoginRequest = {
            username,
            password
        };

        const response = await api.post<LoginResponse>("auth/login", payload);

        if (response.data.token) {
            localStorage.setItem('user_token', response.data.token);
            if (response.data.username) {
                localStorage.setItem('username', response.data.username);
            }
        }

        return response.data;
    }

    logout(): void {
        localStorage.removeItem('user_token');
        localStorage.removeItem('username');
    }
}

export default new AuthService();

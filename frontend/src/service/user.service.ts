import api from "./api";
import type { components } from '../types/api-schemas';


export type UserProfileResponse = components['schemas']['UserProfileResponse'];

class UserService {
    async getUserProfile(): Promise<UserProfileResponse> {
        const response = await api.get<UserProfileResponse>('/users/profile');
        return response.data;
    }
}

export default new UserService();

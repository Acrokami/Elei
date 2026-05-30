import api from "./api";
import type { components } from '../types/api-schemas';


export type UserProfileResponse = components['schemas']['UserProfileResponse'];

class UserService {
    async getUserProfile(): Promise<UserProfileResponse> {
        const response = await api.get<UserProfileResponse>('/users/profile');
        return response.data;
    }


    async activateDailyProtocol(): Promise<string | null> {
        try {
            const response = await api.post<string>('/users/check-in');
            return response.data;
        } catch (error) {
            return null;
        }
    }
}

export default new UserService();

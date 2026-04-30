import api from "./api";

class UserService {
    async getUserProfile() {

        const response = await api.get('users/profile');
    }
}

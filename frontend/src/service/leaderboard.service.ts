import api from "./api";
import type { components } from '../types/api-schemas';


export type LeaderboardEntryDto = components['schemas']['LeaderboardEntryDto'];

class LeaderboardService {
    async getTopCitizens(limit: number = 10): Promise<LeaderboardEntryDto[]> {
        
        const response = await api.get<LeaderboardEntryDto[]>(`/leaderboard/top?limit=${limit}`);
        return response.data;
    }
}

export default new LeaderboardService();

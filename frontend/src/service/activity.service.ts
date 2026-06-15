import api from "./api";
import type { components } from '../types/api-schemas';

export type CreateActivityDto = components['schemas']['CreateActivityDto'];
export type UserStatsDto = components['schemas']['UserStatsDto'];
export type ActivityFeedItemDto = components['schemas']['ActivityFeedItemDto'];

export type ActivityCompletionDto = components['schemas']['ActivityCompletionDto'];

class ActivityService {

    async createActivity(data: CreateActivityDto): Promise<void> {
        const response = await api.post('/activity', data);
        return response.data;
    }

    async deleteActivity(activityId: number): Promise<void> {
        await api.delete(`/activity/${activityId}`);
    }

    async completeActivity(activityId: number, measurementCompleted: number): Promise<void> {

        const payload: ActivityCompletionDto = {
            activityId: activityId,
            measurementCompleted: measurementCompleted
        };
        const response = await api.post('/activity/complete', payload);
        return response.data;
    }

    async getStats(): Promise<UserStatsDto> {
        const response = await api.get<UserStatsDto>('/activity/stats');
        return response.data;
    }

    async getFeed(): Promise<ActivityFeedItemDto[]> {
        const response = await api.get<ActivityFeedItemDto[]>('/activity/feed');
        return response.data;
    }
}

export default new ActivityService();

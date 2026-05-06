import api from "./api";


class ActivityService {
    async getStats() {
        const response = await api.get('/activity/stats')
        return response.data;
    }
    async completeActivity(activityId: number, amount:number) {
        const response = await api.post('/activity/complete', {
            activityId: activityId,
            amount:amount
        });
        return response.data;
    }
}

export default new ActivityService()

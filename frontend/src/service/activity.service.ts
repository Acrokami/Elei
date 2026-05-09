import api from "./api";


class ActivityService {


    async createActivity(data: {name: string, pointsMultiplier: number, unitName: string}) {
        const response = await api.post('/activity', data);
        return response.data;
    }

    async deleteActivity(activityId: number) {
        await api.delete(`activity/${activityId}`);
    };

    async completeActivity(activityId: number, unitsCompleted:number) {
        const response = await api.post('/activity/complete', {
            activityId: activityId,
            unitsCompleted:unitsCompleted
        });
        return response.data;
    }

    async getStats() {
        const response = await api.get('/activity/stats')
        return response.data;
    }

    async getFeed() {
        const response = await api.get('activity/feed');
        return response.data;
    }

}

export default new ActivityService()

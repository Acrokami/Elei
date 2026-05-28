import api from './api';
import type { components } from '../types/api-schemas';

export type SystemStatsDto = components['schemas']['SystemStatsDto'];

export const AdminService = {
    async getTelemetry(): Promise<SystemStatsDto> {
        const response = await api.get<SystemStatsDto>('/admin/telemetry');
        return response.data;
    }
};

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import api from '../../service/api';
import type { components } from '../../types/api-schemas';

type SystemStatsDto = components['schemas']['SystemStatsDto'];

const router = useRouter();

const stats = ref<SystemStatsDto | null>(null);
const isLoading = ref<boolean>(true);
const errorMessage = ref<string>('');
const grafanaUrl = import.meta.env.VITE_GRAFANA_URL;

onMounted(async () => {
    try {
        isLoading.value = true;
        const response = await api.get<SystemStatsDto>('/admin/telemetry');
        stats.value = response.data;
    } catch (error: any) {
        if (error.message && error.message.includes('403')) {
            errorMessage.value = 'Access Denied: Level 4 Security Clearance (ADMIN) required';
        } else {
            errorMessage.value = error.message || 'Failed to synchronize with backend server.';
        }
    } finally {
        isLoading.value = false;
    }
});


const handleHome = () => {
  router.push('/');
};
</script>


<template>
  <div class="dashboard-container">



    <div class="topbar">
      <span class="logo">Elei<span class="logo-dot">.</span></span>


      <div class="topbar-actions">
        <a :href="grafanaUrl" target="_blank" rel="noopener noreferrer" class="grafana-btn">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="action-icon">
            <path d="M2 12h4l2-9 5 18 2-9h5"></path>
          </svg>
          Grafana
        </a>
      </div>

      <button class="back-btn" @click="handleHome">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="back-icon">
          <line x1="19" y1="12" x2="5" y2="12"></line>
          <polyline points="12 19 5 12 12 5"></polyline>
        </svg>
        Dashboard
      </button>
    </div>

    <div class="content">
      <header class="dashboard-header">
        <h1 class="glow-text">System Telemetry</h1>
        <p class="subtitle">Real-time Elei global metrics</p>
      </header>

      <div v-if="isLoading" class="loader-container">
        <div class="radar-spinner"></div>
        <p>Establishing secure connection...</p>
      </div>

      <div v-else-if="errorMessage" class="error-container">
        <svg xmlns="http://www.w3.org/2000/svg" class="error-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
          <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
        </svg>
        <h2>Connection Rejected</h2>
        <p>{{ errorMessage }}</p>
      </div>

      <div v-else-if="stats" class="stats-grid">
        <div class="stat-card">
          <div class="icon-box blue-glow">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
              <circle cx="9" cy="7" r="4"></circle>
              <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
              <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
            </svg>
          </div>
          <div class="stat-info">
            <h3>Total Citizens</h3>
            <span class="stat-value">{{ stats.totalUsers }}</span>
          </div>
        </div>

        <div class="stat-card">
          <div class="icon-box purple-glow">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
            </svg>
          </div>
          <div class="stat-info">
            <h3>Active Protocols</h3>
            <span class="stat-value">{{ stats.totalActivities }}</span>
          </div>
        </div>

        <div class="stat-card">
          <div class="icon-box green-glow">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"></polygon>
            </svg>
          </div>
          <div class="stat-info">
            <h3>Experience Events</h3>
            <span class="stat-value">{{ stats.totalExperienceLogs }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard-container {
  min-height: 100vh;
  width: 100%;
  background-color: #0b0c10;
  color: #c5c6c7;
  font-family: 'Inter', system-ui, sans-serif;
  display: flex;
  flex-direction: column;
}


.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 32px;
  background: rgba(11, 12, 16, 0.8);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(102, 252, 241, 0.1);
  position: sticky;
  top: 0;
  z-index: 50;
}

.back-btn, .grafana-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(102, 252, 241, 0.05);
  color: #c5c6c7;
  border: 1px solid rgba(102, 252, 241, 0.2);
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  text-decoration: none;
  transition: all 0.2s ease;
}

.back-btn:hover, .grafana-btn:hover {
  background: rgba(102, 252, 241, 0.1);
  border-color: rgba(102, 252, 241, 0.4);
  color: #ffffff;
  transform: translateY(-2px);
}

.grafana-btn:hover {
  background: rgba(244, 104, 0, 0.1);
  border-color: rgba(244, 104, 0, 0.4);
  color: #ff9d5c;
}

.action-icon {
  width: 16px;
  height: 16px;
}

.logo {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 0.05em;
  background: linear-gradient(to right, #ffffff, #c5c6c7);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.logo-dot {
  color: #66fcf1;
  -webkit-text-fill-color: #66fcf1;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(102, 252, 241, 0.05);
  color: #c5c6c7;
  border: 1px solid rgba(102, 252, 241, 0.2);
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.back-btn:hover {
  background: rgba(102, 252, 241, 0.1);
  border-color: rgba(102, 252, 241, 0.4);
  color: #ffffff;
  transform: translateX(-2px);
}

.back-icon {
  width: 16px;
  height: 16px;
  transition: transform 0.2s ease;
}

.back-btn:hover .back-icon {
  transform: translateX(-3px);
}


.content {
  flex: 1;
  padding: 40px 20px;
}

.dashboard-header {
  text-align: center;
  margin-bottom: 50px;
}

.glow-text {
  font-size: 2.5rem;
  font-weight: 800;
  color: #ffffff;
  margin: 0;
  letter-spacing: 1px;
  text-shadow: 0 0 20px rgba(255, 255, 255, 0.2);
}

.subtitle {
  color: #66fcf1;
  font-size: 1.1rem;
  margin-top: 8px;
  letter-spacing: 2px;
  text-transform: uppercase;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.stat-card {
  background: rgba(31, 40, 51, 0.6);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(102, 252, 241, 0.1);
  border-radius: 16px;
  padding: 30px;
  display: flex;
  align-items: center;
  gap: 24px;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.stat-card:hover {
  transform: translateY(-5px);
  border-color: rgba(102, 252, 241, 0.4);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.5), 0 0 20px rgba(102, 252, 241, 0.1);
}

.icon-box {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.icon-box svg {
  width: 32px;
  height: 32px;
  color: #ffffff;
}

.blue-glow {
  background: linear-gradient(135deg, #1e3a8a, #3b82f6);
  box-shadow: 0 0 20px rgba(59, 130, 246, 0.4);
}

.purple-glow {
  background: linear-gradient(135deg, #581c87, #8b5cf6);
  box-shadow: 0 0 20px rgba(139, 92, 246, 0.4);
}

.green-glow {
  background: linear-gradient(135deg, #064e3b, #10b981);
  box-shadow: 0 0 20px rgba(16, 185, 129, 0.4);
}

.stat-info h3 {
  margin: 0;
  font-size: 1rem;
  color: #9ca3af;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.stat-value {
  display: block;
  font-size: 2.5rem;
  font-weight: 700;
  color: #ffffff;
  margin-top: 4px;
  line-height: 1;
}

.error-container, .loader-container {
  text-align: center;
  margin-top: 100px;
}

.error-icon {
  width: 64px;
  height: 64px;
  color: #ef4444;
  margin-bottom: 20px;
}

.error-container h2 {
  color: #ef4444;
  margin-bottom: 10px;
}

.radar-spinner {
  width: 60px;
  height: 60px;
  border: 3px solid rgba(102, 252, 241, 0.2);
  border-top-color: #66fcf1;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px auto;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>

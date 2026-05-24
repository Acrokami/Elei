<script setup lang="ts">
import {ref, onMounted} from 'vue';



interface SystemStats {
    totalUsers: number;
    totalActivities: number;
    totalExperienceLogs: number;
}


const stats = ref<SystemStats | null>(null);
const isLoading = ref<boolean>(true);
const errorMessage = ref<string>('');


onMounted(async () => {
    try {
        const token = localStorage.getItem('user_token');
        const response = await fetch('http://localhost:8080/api/v1/admin/telemetry', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if(response.status === 403) {
            throw new Error('Access Denied: Level 4 Security Clearance (ADMIN) required');
        }
        if (!response.ok) {
            throw new Error('Failed to synchronize with backend server.');
        }

        stats.value = await response.json();
    } catch (error: any) {
        errorMessage.value = error.message;
    } finally {
        isLoading.value = false;
    }
});
</script>


<template>
  <div class="dashboard-container">
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
</template>

<style scoped>


.dashboard-container {
  min-height: 100vh;
  padding: 40px 20px;
  background-color: #0b0c10;
  color: #c5c6c7;
  font-family: 'Inter', system-ui, sans-serif;
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

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import api from "../../service/api";
import type { components } from "../../types/api-schemas";
import activityService from "../../service/activity.service";

type SystemStatsDto = components["schemas"]["SystemStatsDto"];
type CreateActivityDto = components["schemas"]["CreateActivityDto"];

const router = useRouter();


const stats = ref<SystemStatsDto | null>(null);
const isLoading = ref<boolean>(true);
const errorMessage = ref<string>("");
const grafanaUrl = import.meta.env.VITE_GRAFANA_URL;


const showCreateForm = ref(false);
const isSubmitting = ref(false);
const successMessage = ref("");
const newActivity = ref<CreateActivityDto>({
  name: "",
  pointsMultiplier: 1,
  measurementName: "",
});

const loadTelemetry = async () => {
  try {
    isLoading.value = true;
    const response = await api.get<SystemStatsDto>("/admin/telemetry");
    stats.value = response.data;
  } catch (error: any) {
    if (error.message && error.message.includes("403")) {
      errorMessage.value =
        "Access Denied: Level 4 Security Clearance (ADMIN) required";
    } else {
      errorMessage.value =
        error.message || "Failed to synchronize with backend server.";
    }
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  loadTelemetry();
});

const handleHome = () => {
  router.push("/");
};

const handleCreateActivity = async () => {
  try {
    isSubmitting.value = true;
    await activityService.createActivity(newActivity.value);


    newActivity.value = { name: "", pointsMultiplier: 1, measurementName: "" };
    showCreateForm.value = false;


    successMessage.value = "System Protocol successfully deployed!";
    setTimeout(() => successMessage.value = "", 5000);


    await loadTelemetry();
  } catch (error) {
    console.error("Error creating new system protocol", error);
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<template>
  <div class="dashboard-container">
    <div class="topbar">
      <span class="logo">Elei<span class="logo-dot">.</span></span>

      <div class="topbar-actions">
        <a
          :href="grafanaUrl"
          target="_blank"
          rel="noopener noreferrer"
          class="grafana-btn"
        >
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

      <template v-else-if="stats">
        <div class="stats-grid">
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


        <div class="admin-actions-section">
          <div class="actions-header">
            <h2 class="section-subtitle">Protocol Management</h2>
            <button class="toggle-form-btn" :class="{ 'is-active': showCreateForm }" @click="showCreateForm = !showCreateForm">
              <span class="btn-icon">
                <svg v-if="!showCreateForm" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="12" y1="5" x2="12" y2="19"></line>
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="18" y1="6" x2="6" y2="18"></line>
                  <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
              </span>
              {{ showCreateForm ? "Cancel Deployment" : "Deploy New Protocol" }}
            </button>
          </div>

          <transition name="slide-fade">
            <div v-if="successMessage" class="success-banner">
              {{ successMessage }}
            </div>
          </transition>

          <Transition name="expand">
            <div v-if="showCreateForm" class="form-wrapper">
              <div class="create-form-card stat-card">
                <form @submit.prevent="handleCreateActivity" class="create-form">
                  <div class="form-group">
                    <label>Protocol Designation (Name)</label>
                    <div class="input-wrapper">
                      <input v-model="newActivity.name" type="text" required placeholder="e.g. Advanced Coding, Meditation" />
                    </div>
                  </div>

                  <div class="form-row">
                    <div class="form-group half-width">
                      <label>XP Multiplier</label>
                      <div class="input-wrapper">
                        <input v-model="newActivity.pointsMultiplier" type="number" min="1" required placeholder="Base XP per unit" />
                      </div>
                    </div>

                    <div class="form-group half-width">
                      <label>Unit of Measurement</label>
                      <div class="input-wrapper">
                        <input v-model="newActivity.measurementName" type="text" required placeholder="e.g. pages, minutes, modules" />
                      </div>
                    </div>
                  </div>

                  <button type="submit" class="submit-btn" :disabled="isSubmitting">
                    <svg class="save-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"></path>
                      <polyline points="17 21 17 13 7 13 7 21"></polyline>
                      <polyline points="7 3 7 8 15 8"></polyline>
                    </svg>
                    {{ isSubmitting ? "Deploying..." : "Initialize Protocol" }}
                  </button>
                </form>
              </div>
            </div>
          </Transition>
        </div>
      </template>
    </div>
  </div>
</template>

<style scoped>
.dashboard-container {
  min-height: 100vh;
  width: 100%;
  background-color: #0b0c10;
  color: #c5c6c7;
  font-family: "Inter", system-ui, sans-serif;
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

.topbar-actions {
  display: flex;
  gap: 16px;
}

.back-btn,
.grafana-btn {
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

.back-btn:hover,
.grafana-btn:hover {
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

.action-icon, .back-icon {
  width: 16px;
  height: 16px;
  transition: transform 0.2s ease;
}

.back-btn:hover .back-icon {
  transform: translateX(-3px);
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
  margin: 0 auto 50px auto;
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
  box-shadow:
    0 15px 35px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(102, 252, 241, 0.1);
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

/* ADMIN ACTIONS SECTION */
.admin-actions-section {
  max-width: 1200px;
  margin: 0 auto;
  border-top: 1px solid rgba(102, 252, 241, 0.1);
  padding-top: 40px;
}

.actions-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-subtitle {
  color: #ffffff;
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0;
  letter-spacing: 1px;
}

.toggle-form-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(102, 252, 241, 0.1);
  color: #66fcf1;
  border: 1px solid rgba(102, 252, 241, 0.2);
  padding: 10px 20px;
  border-radius: 10px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.toggle-form-btn:hover {
  background: rgba(102, 252, 241, 0.2);
  border-color: rgba(102, 252, 241, 0.4);
}

.toggle-form-btn.is-active {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  border-color: rgba(239, 68, 68, 0.2);
}

.btn-icon svg {
  width: 16px;
  height: 16px;
}

.create-form-card {
  display: block; 
  padding: 32px;
}

.create-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-row {
  display: flex;
  gap: 24px;
}

.half-width {
  flex: 1;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  color: #c5c6c7;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

.form-group input {
  box-sizing: border-box;
  width: 100%;
  padding: 14px 16px;
  background: rgba(11, 12, 16, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  font-size: 15px;
  color: #ffffff;
  outline: none;
  transition: all 0.3s ease;
}

.form-group input:focus {
  border-color: #66fcf1;
  background: rgba(11, 12, 16, 0.8);
  box-shadow: 0 0 0 3px rgba(102, 252, 241, 0.2);
}

.submit-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  padding: 16px;
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2);
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(16, 185, 129, 0.4);
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.save-icon {
  width: 18px;
  height: 18px;
}

.success-banner {
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
  border: 1px solid rgba(16, 185, 129, 0.3);
  padding: 16px;
  border-radius: 12px;
  margin-bottom: 24px;
  text-align: center;
  font-weight: 600;
}

/* STATES & ANIMATIONS */
.error-container,
.loader-container {
  text-align: center;
  margin-top: 100px;
}

.error-icon {
  width: 64px;
  height: 64px;
  color: #ef4444;
  margin-bottom: 20px;
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

.expand-enter-active,
.expand-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  max-height: 500px;
  opacity: 1;
  overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
  max-height: 0;
  opacity: 0;
  transform: translateY(-10px);
}

.slide-fade-enter-active {
  transition: all 0.3s ease-out;
}
.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}
</style>

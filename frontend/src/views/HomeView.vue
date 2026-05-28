<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router'
import userService from '../service/user.service';

import type { UserProfileResponse } from '../service/user.service';
import authService from '../service/auth.service';

const router = useRouter();


const userProfile = ref<UserProfileResponse | null>(null);
const isAdmin = ref(false);

onMounted(async () => {
    try {
        userProfile.value = await userService.getUserProfile();
        checkAdminStatus();
    } catch (error) {
        console.error("Error retrieving profile");
    }
});

const checkAdminStatus = () => {
  const token = localStorage.getItem('user_token');

  if(token) {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      const payload = JSON.parse(jsonPayload);
      if (payload.role === 'ADMIN') {
        isAdmin.value = true;
      }
    } catch (e) {
      console.error('Error parsing JWT token:', e)
    }
  }
}

const handleLogout = () => {
    authService.logout();
    router.push('/login');
};
</script>

<template>
<div class="page-wrapper">
    <div class="ambient-glow glow-1"></div>
    <div class="ambient-glow glow-2"></div>

    <div class="topbar">
      <span class="logo">Elei<span class="logo-dot">.</span></span>
      <button class="logout-btn" @click="handleLogout">
        <span>Logout</span>
      </button>
    </div>

    <div class="content">
      <div v-if="!userProfile" class="loading-state">
        <div class="spinner"></div>
        <p>Connecting to servers...</p>
      </div>

      <div v-else class="dashboard-container">
        <div class="welcome-card glass-panel">
          <div class="avatar-container">
            <div class="avatar-ring"></div>
            <div class="avatar">{{ userProfile.username[0].toUpperCase() }}</div>
          </div>
          <div class="welcome-text">
            <h1>Welcome back, <span>{{ userProfile.username }}</span></h1>
            <p>Your dashboard is ready. What is our objective today?</p>
          </div>
        </div>

        <div class="nav-menu">
          <router-link to="/activity" class="nav-btn glass-panel">
            <div class="nav-icon-wrapper blue-glow">
              <div class="nav-icon">⚡</div>
            </div>
            <div class="nav-text">
              <span class="nav-title">Activity & XP</span>
              <span class="nav-subtitle">Track your progression metrics</span>
            </div>
            <div class="nav-arrow">→</div>
          </router-link>

          <router-link v-if="isAdmin" to="/admin" class="nav-btn glass-panel">
            <div class="nav-icon-wrapper purple-glow">
              <div class="nav-icon">🛡️</div>
            </div>
            <div class="nav-text">
              <span class="nav-title">Admin Dashboard</span>
              <span class="nav-subtitle">System management and overrides</span>
            </div>
            <div class="nav-arrow">→</div>
          </router-link>

          <router-link to="/leaderboard" class="nav-btn glass-panel">
            <div class="nav-icon-wrapper gold-glow">
              <div class="nav-icon">🏆</div>
            </div>
            <div class="nav-text">
              <span class="nav-title">Leaderboard</span>
              <span class="nav-subtitle">Global citizen rankings</span>
            </div>
            <div class="nav-arrow">→</div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.page-wrapper {
  min-height: 100vh;
  background-color: #0b1120;
  font-family: 'Inter', sans-serif;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
  color: #f8fafc;
}


.ambient-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(120px);
  z-index: 0;
  opacity: 0.4;
  pointer-events: none;
}
.glow-1 {
  width: 400px;
  height: 400px;
  background: rgba(59, 130, 246, 0.3);
  top: -100px;
  left: -100px;
}
.glow-2 {
  width: 500px;
  height: 500px;
  background: rgba(139, 92, 246, 0.2);
  bottom: -200px;
  right: -100px;
}


.topbar, .content {
  position: relative;
  z-index: 1;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
  background: rgba(11, 17, 32, 0.5);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.logo {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 0.05em;
  background: linear-gradient(to right, #ffffff, #94a3b8);
  background-clip: text;
  -webkit-text-fill-color: transparent;
}
.logo-dot {
  color: #3b82f6;
  -webkit-text-fill-color: #3b82f6;
}

.logout-btn {
  padding: 8px 20px;
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.logout-btn:hover {
  background: rgba(239, 68, 68, 0.2);
  border-color: rgba(239, 68, 68, 0.4);
  box-shadow: 0 0 15px rgba(239, 68, 68, 0.2);
  transform: translateY(-1px);
}

.content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
}


.glass-panel {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.dashboard-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 40px;
  width: 100%;
  max-width: 480px;
}

.welcome-card {
  width: 100%;
  padding: 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 20px;
}

.avatar-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-ring {
  position: absolute;
  width: 88px;
  height: 88px;
  border-radius: 50%;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  opacity: 0.5;
  filter: blur(8px);
  animation: pulse 3s infinite alternate;
}

@keyframes pulse {
  0% { transform: scale(0.95); opacity: 0.5; }
  100% { transform: scale(1.05); opacity: 0.8; }
}

.avatar {
  width: 76px;
  height: 76px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1e293b, #0f172a);
  border: 2px solid rgba(255, 255, 255, 0.1);
  color: white;
  font-size: 32px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.welcome-text h1 {
  font-size: 26px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #e2e8f0;
}
.welcome-text h1 span {
  color: #ffffff;
}
.welcome-text p {
  color: #94a3b8;
  font-size: 15px;
  margin: 0;
  line-height: 1.5;
}

.nav-menu {
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 100%;
}

.nav-btn {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  text-decoration: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.nav-btn::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: linear-gradient(90deg, rgba(255,255,255,0.03), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.nav-btn:hover {
  transform: translateY(-3px);
  border-color: rgba(255, 255, 255, 0.15);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}
.nav-btn:hover::before {
  opacity: 1;
}

.nav-icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: 12px;
  margin-right: 16px;
  background: rgba(30, 41, 59, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.nav-icon {
  font-size: 20px;
}


.blue-glow { box-shadow: inset 0 0 15px rgba(59, 130, 246, 0.2); }
.purple-glow { box-shadow: inset 0 0 15px rgba(168, 85, 247, 0.2); }
.gold-glow { box-shadow: inset 0 0 15px rgba(234, 179, 8, 0.2); }

.nav-text {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.nav-title {
  color: #f8fafc;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.01em;
}

.nav-subtitle {
  color: #64748b;
  font-size: 13px;
  margin-top: 4px;
}

.nav-arrow {
  color: #475569;
  font-size: 20px;
  transition: transform 0.3s ease, color 0.3s ease;
}
.nav-btn:hover .nav-arrow {
  transform: translateX(4px);
  color: #f8fafc;
}


.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}
.spinner {
  width: 32px;
  height: 32px;
  border: 3px solid rgba(59, 130, 246, 0.2);
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}
@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>

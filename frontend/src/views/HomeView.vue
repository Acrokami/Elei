<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import userService from "../service/user.service";
import type { UserProfileResponse } from "../service/user.service";
import authService from "../service/auth.service";
import api from "../service/api.ts";

import ActivityHeatmap from "../components/activity/ActivityHeatmap.vue";

const router = useRouter();
const username = ref('Citizen');
const level = ref(1);
const currentXp = ref(0);
const nextLevelXp = ref(100);
const rankName = ref('Initiate');

const userProfile = ref<UserProfileResponse | null>(null);
const isAdmin = ref(false);

const isCheckedInToday = ref(false);
const checkInMessage = ref("");

const xpPercentage = computed(() => {
  if (nextLevelXp.value === 0) return 0;
  const percent = (currentXp.value / nextLevelXp.value) * 100;
  return Math.min(Math.max(percent, 0), 100);
});

const fetchDashboardData = async () => {
  try {
    const response = await api.get('/users/profile');
    username.value = response.data.username;
    level.value = response.data.level || 1;
    currentXp.value = response.data.totalExperience || 0;
    nextLevelXp.value = response.data.nextLevelExperience || 100;
    rankName.value = response.data.rank || 'Initiate';
  } catch (e) {
    console.error('[SYSTEM] Failed to load telemetry:', e);
  }
};

onMounted(async () => {
  try {
    userProfile.value = await userService.getUserProfile();
    checkAdminStatus();

    const message = await userService.activateDailyProtocol();
    if (message) {
      isCheckedInToday.value = true;
      checkInMessage.value = message;

      setTimeout(() => {
        checkInMessage.value = "";
      }, 5000);
    }
    fetchDashboardData();
  } catch (error) {
    console.error("Error retrieving profile");
  }
});

const checkAdminStatus = () => {
  const token = localStorage.getItem("user_token");

  if (token) {
    try {
      const base64Url = token.split(".")[1];
      const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split("")
          .map(function (c) {
            return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
          })
          .join(""),
      );
      const payload = JSON.parse(jsonPayload);

      console.log("[SYSTEM] Decoded JWT Payload:", payload);
      const userRole = payload.role || payload.roles;

      if (
        userRole === "ADMIN" ||
        userRole === "ROLE_ADMIN" ||
        (Array.isArray(userRole) && (userRole.includes("ADMIN") || userRole.includes("ROLE_ADMIN")))
      ) {
        isAdmin.value = true;
      }
    } catch (e) {
      console.error("[SYSTEM] Error parsing JWT token:", e);
    }
  }
};

const handleLogout = () => {
  authService.logout();
  router.push("/login");
};
</script>

<template>
  <div class="page-wrapper">
    <div class="ambient-glow glow-1"></div>
    <div class="ambient-glow glow-2"></div>

    <div class="topbar">
      <span class="logo">Elei<span class="logo-dot">.</span></span>

      <div class="topbar-actions">
        <Transition name="fade">
          <div v-if="isCheckedInToday" class="streak-badge">
            <span class="fire-icon">🔥</span> Protocol Active
          </div>
        </Transition>

        <router-link to="/settings" class="settings-icon-btn">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="3"></circle>
            <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path>
          </svg>
        </router-link>

        <button class="logout-btn" @click="handleLogout">
          <span>Logout</span>
        </button>
      </div>
    </div>

    <div class="content">
      <div v-if="!userProfile" class="loading-state">
        <div class="spinner"></div>
        <p>Connecting to servers...</p>
      </div>

      <div v-else class="dashboard-layout">
        <aside class="sidebar-nav">
          <div class="nav-menu">
            <router-link to="/activity" class="nav-btn glass-panel">
              <div class="nav-icon-wrapper blue-glow">
                <div class="nav-icon">⚡</div>
              </div>
              <div class="nav-text">
                <span class="nav-title">Activity & XP</span>
                <span class="nav-subtitle">Track progression</span>
              </div>
              <div class="nav-arrow">→</div>
            </router-link>

            <router-link to="/quests" class="nav-btn glass-panel">
              <div class="nav-icon-wrapper emerald-glow">
                <div class="nav-icon">🎯</div>
              </div>
              <div class="nav-text">
                <span class="nav-title">Active Protocols</span>
                <span class="nav-subtitle">Manage daily tasks</span>
              </div>
              <div class="nav-arrow">→</div>
            </router-link>

            <router-link to="/market" class="nav-btn glass-panel">
              <div class="nav-icon-wrapper amber-glow">
                <div class="nav-icon">💎</div>
              </div>
              <div class="nav-text">
                <span class="nav-title">System Market</span>
                <span class="nav-subtitle">Exchange assets</span>
              </div>
              <div class="nav-arrow">→</div>
            </router-link>

            <router-link v-if="isAdmin" to="/admin" class="nav-btn glass-panel">
              <div class="nav-icon-wrapper purple-glow">
                <div class="nav-icon">🛡️</div>
              </div>
              <div class="nav-text">
                <span class="nav-title">Admin</span>
                <span class="nav-subtitle">System overrides</span>
              </div>
              <div class="nav-arrow">→</div>
            </router-link>

            <router-link to="/leaderboard" class="nav-btn glass-panel">
              <div class="nav-icon-wrapper gold-glow">
                <div class="nav-icon">🏆</div>
              </div>
              <div class="nav-text">
                <span class="nav-title">Leaderboard</span>
                <span class="nav-subtitle">Global rankings</span>
              </div>
              <div class="nav-arrow">→</div>
            </router-link>
          </div>
        </aside>

        <main class="main-workspace">
          <Transition name="slide-down">
            <div v-if="checkInMessage" class="checkin-toast glass-panel">
              <div class="toast-icon">✨</div>
              <div class="toast-text">{{ checkInMessage }}</div>
            </div>
          </Transition>

          <div class="glass-panel progression-panel mb-6">
            <div class="profile-header">
              <div class="avatar-ring-progression">
                <div class="avatar-progression">{{ username.charAt(0).toUpperCase() }}</div>
              </div>
              <div class="profile-info">
                <h3 class="citizen-name">{{ username }}</h3>
                <div class="rank-badge">
                  <span class="level-indicator">LVL {{ level }}</span>
                  <span class="rank-name">{{ rankName }}</span>
                </div>
              </div>
            </div>

            <div class="xp-container">
              <div class="xp-stats">
                <span class="xp-label">System Experience</span>
                <span class="xp-values">{{ currentXp }} <span class="xp-divider">/</span> {{ nextLevelXp }} XP</span>
              </div>
              <div class="xp-track">
                <div class="xp-fill" :style="{ width: xpPercentage + '%' }">
                  <div class="xp-glow"></div>
                </div>
              </div>
            </div>
          </div>

          <div class="welcome-card glass-panel">
            <div class="welcome-text">
              <h1>
                Welcome back, <span>{{ userProfile.username }}</span>
              </h1>
              <p>Your dashboard is ready. What is our objective today?</p>
            </div>
          </div>

          <div class="activity-section glass-panel">
            <ActivityHeatmap :activeDates="userProfile?.activeDays || []" />
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap");

.page-wrapper {
  min-height: 100vh;
  background-color: #0b1120;
  font-family: "Inter", sans-serif;
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
  background: var(--primary-glow);
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

.topbar,
.content {
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
  color: var(--primary-accent);
  -webkit-text-fill-color: var(--primary-accent);
}

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.streak-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: rgba(245, 158, 11, 0.1);
  border: 1px solid rgba(245, 158, 11, 0.2);
  border-radius: 20px;
  color: #fcd34d;
  font-size: 13px;
  font-weight: 600;
  box-shadow: 0 0 10px rgba(245, 158, 11, 0.1);
}

.fire-icon {
  font-size: 14px;
  animation: flicker 2s infinite alternate;
}

@keyframes flicker {
  0% { transform: scale(0.95); opacity: 0.8; }
  100% { transform: scale(1.1); opacity: 1; }
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

.settings-icon-btn {
  color: #94a3b8;
  display: flex;
  align-items: center;
  transition: color 0.3s ease;
}
.settings-icon-btn:hover {
  color: #fff;
}

.content {
  flex: 1;
  display: flex;
  justify-content: center;
  padding: 40px;
}

.glass-panel {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}
.mb-6 { margin-bottom: 24px; }

.dashboard-layout {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 32px;
  width: 100%;
  max-width: 1100px;
  align-items: start;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 40px;
}

.nav-menu {
  display: flex;
  flex-direction: column;
  gap: 16px;
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
  content: "";
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.03), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.nav-btn:hover {
  transform: translateY(-3px);
  border-color: rgba(255, 255, 255, 0.15);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}
.nav-btn:hover::before { opacity: 1; }

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

.nav-icon { font-size: 20px; }
.blue-glow { box-shadow: inset 0 0 15px var(--primary-glow); }
.purple-glow { box-shadow: inset 0 0 15px rgba(168, 85, 247, 0.2); }
.gold-glow { box-shadow: inset 0 0 15px rgba(234, 179, 8, 0.2); }
.emerald-glow { box-shadow: inset 0 0 15px rgba(16, 185, 129, 0.2); }
.amber-glow { box-shadow: inset 0 0 15px rgba(245, 158, 11, 0.2); } /* НОВЫЙ ЭФФЕКТ ДЛЯ МАРКЕТА */

.nav-text { display: flex; flex-direction: column; flex: 1; }
.nav-title { color: #f8fafc; font-size: 16px; font-weight: 600; letter-spacing: 0.01em; }
.nav-subtitle { color: #64748b; font-size: 12px; margin-top: 4px; }
.nav-arrow {
  color: #475569; font-size: 20px;
  transition: transform 0.3s ease, color 0.3s ease;
}
.nav-btn:hover .nav-arrow { transform: translateX(4px); color: #f8fafc; }

.main-workspace {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.checkin-toast {
  display: flex; align-items: center; gap: 12px; width: 100%; padding: 16px 24px;
  background: linear-gradient(90deg, rgba(16, 185, 129, 0.1), rgba(30, 41, 59, 0.4));
  border-left: 4px solid var(--primary-accent);
}
.toast-icon { font-size: 20px; }
.toast-text { color: #a7f3d0; font-size: 14px; font-weight: 500; line-height: 1.4; }


.progression-panel {
  padding: 24px; display: flex; flex-direction: column; gap: 20px;
}
.profile-header { display: flex; align-items: center; gap: 16px; }
.avatar-ring-progression {
  padding: 3px; border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-accent), transparent);
  box-shadow: 0 0 15px var(--primary-glow);
}
.avatar-progression {
  width: 56px; height: 56px; background: #0f172a; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 24px; font-weight: 700; color: var(--primary-accent);
  border: 2px solid rgba(255, 255, 255, 0.05);
}
.citizen-name {
  font-size: 20px; font-weight: 700; margin: 0 0 4px 0; color: #fff; letter-spacing: 0.05em;
}
.rank-badge { display: flex; align-items: center; gap: 8px; }
.level-indicator {
  background: var(--primary-glow); color: var(--primary-accent);
  padding: 2px 8px; border-radius: 4px; font-size: 12px; font-weight: 700; border: 1px solid var(--primary-accent);
}
.rank-name { font-size: 14px; color: #94a3b8; font-weight: 500; }
.xp-container { display: flex; flex-direction: column; gap: 8px; }
.xp-stats { display: flex; justify-content: space-between; align-items: flex-end; }
.xp-label { font-size: 13px; color: #cbd5e1; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em; }
.xp-values { font-size: 14px; font-weight: 700; color: #fff; }
.xp-divider { color: #64748b; margin: 0 2px; }
.xp-track {
  height: 12px; background: rgba(15, 23, 42, 0.6); border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.05); overflow: hidden; position: relative;
}
.xp-fill {
  height: 100%; background: var(--primary-accent); border-radius: 6px;
  transition: width 1s cubic-bezier(0.4, 0, 0.2, 1); position: relative;
}
.xp-glow {
  position: absolute; right: 0; top: 0; height: 100%; width: 20px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.8)); filter: blur(2px);
}

.welcome-card {
  width: 100%; padding: 32px; display: flex; flex-direction: column; align-items: flex-start; gap: 20px;
}
.welcome-text h1 { font-size: 28px; font-weight: 600; margin: 0 0 8px 0; color: #e2e8f0; }
.welcome-text h1 span { color: #ffffff; }
.welcome-text p { color: #94a3b8; font-size: 15px; margin: 0; line-height: 1.5; }

.activity-section { padding: 24px; width: 100%; }

.loading-state { display: flex; flex-direction: column; align-items: center; gap: 16px; margin-top: 100px; }
.spinner {
  width: 32px; height: 32px; border: 3px solid rgba(59, 130, 246, 0.2);
  border-top-color: var(--primary-accent); border-radius: 50%; animation: spin 1s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

.slide-down-enter-active, .slide-down-leave-active { transition: all 0.5s ease; }
.slide-down-enter-from, .slide-down-leave-to { opacity: 0; transform: translateY(-20px); }
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

@media (max-width: 860px) {
  .dashboard-layout { grid-template-columns: 1fr; gap: 24px; }
  .sidebar-nav { position: static; }
  .content { padding: 20px; }
}
</style>

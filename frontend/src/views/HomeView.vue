<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router'
import userService from '../service/user.service';
import authService from '../service/auth.service';

const router = useRouter();
const userProfile = ref<any>(null)

onMounted(async () => {
    try {
        userProfile.value = await userService.getUserProfile();
    } catch (error) {
        console.error("Error retrieving profile");
    }
});

const handleLogout = () => {
    authService.logout();
    router.push('/login');
};
</script>

<template>
  <div class="page-wrapper">

    <div class="topbar">
      <span class="logo">Elei</span>
      <button class="logout-btn" @click="handleLogout">Logout</button>
    </div>

    <div class="content">
      <div v-if="!userProfile" class="loading">
        <p>Loading profile...</p>
      </div>

      <div v-else>
        <div class="welcome-card">
          <div class="avatar">{{ userProfile.username[0].toUpperCase() }}</div>
          <h1>Welcome back, {{ userProfile.username }}</h1>
          <p>What would you like to do today?</p>
        </div>

        <div class="nav-menu">
          <router-link to="/activity" class="nav-btn">
            <div class="nav-icon">⚡</div>
            <div class="nav-text">
              <span class="nav-title">Activity & XP</span>
              <span class="nav-subtitle">Track your progress</span>
            </div>
          </router-link>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>
.page-wrapper {
  min-height: 100vh;
  background-color: #0f172a;
  display: flex;
  flex-direction: column;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 32px;
  border-bottom: 1px solid #1e293b;
}

.logo {
  color: #f1f5f9;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 0.05em;
}

.logout-btn {
  padding: 8px 18px;
  background: transparent;
  color: #ef4444;
  border: 1px solid #ef4444;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.logout-btn:hover {
  background: #ef444420;
}

.content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.loading p {
  color: #475569;
  font-size: 16px;
}

.welcome-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  text-align: center;
  margin-bottom: 40px;
}

.avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: #2563eb;
  color: white;
  font-size: 28px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
}

.welcome-card h1 {
  color: #f1f5f9;
  font-size: 28px;
  font-weight: 600;
  margin: 0;
}

.welcome-card p {
  color: #475569;
  font-size: 16px;
  margin: 0;
}

.nav-menu {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 300px;
}

.nav-btn {
  display: flex;
  align-items: center;
  gap: 16px;
  background: #1e293b;
  border: 1px solid #1e293b;
  border-radius: 12px;
  padding: 16px 20px;
  text-decoration: none;
  transition: all 0.2s ease;
}

.nav-btn:hover {
  border-color: #3b82f6;
  transform: translateY(-2px);
}

.nav-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
  border-radius: 10px;
  font-size: 20px;
}

.nav-text {
  display: flex;
  flex-direction: column;
}

.nav-title {
  color: #f1f5f9;
  font-size: 15px;
  font-weight: 600;
}

.nav-subtitle {
  color: #64748b;
  font-size: 12px;
  margin-top: 2px;
}
</style>

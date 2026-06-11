<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import api from "../service/api";

const router = useRouter();
const username = ref("");
const email = ref("");
const provider = ref("");
const currentPassword = ref("");
const newPassword = ref("");
const isSaving = ref(false);


const emailError = ref("");
const passwordError = ref("");
const statusMessage = ref("");
const statusType = ref<"success" | "error" | "">("");

const currentTheme = ref("emerald");
const themes = [
  { id: "emerald", name: "Jarvis Protocol", color: "#10b981" },
  { id: "blue",    name: "Deep Ocean",      color: "#3b82f6" },
  { id: "purple",  name: "Neon Amethyst",   color: "#a855f7" },
  { id: "rose",    name: "Crimson Alert",   color: "#e11d48" },
];

const returnToHome = () => router.push("/");
const changeTheme = (themeId: string) => {
  currentTheme.value = themeId;
  document.documentElement.setAttribute("data-theme", themeId);
  localStorage.setItem("elei_theme", themeId);
};


const showStatus = (message: string, type: "success" | "error") => {
  statusMessage.value = message;
  statusType.value = type;
  setTimeout(() => {
    statusMessage.value = "";
  }, 5000);
};

const handleProfileSave = async () => {
  emailError.value = "";


  if (!email.value) {
    emailError.value = "Communication link (email) cannot be empty.";
    return;
  }
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email.value)) {
    emailError.value = "Invalid email format.";
    return;
  }

  isSaving.value = true;
  try {
    await api.put('/settings/email', { newEmail: email.value });
    showStatus("Identity parameters updated successfully.", "success");
  } catch (e: any) {
    showStatus(e.response?.data || "Failed to update identity parameters.", "error");
  } finally {
    isSaving.value = false;
  }
};

const handlePasswordUpdate = async () => {
  passwordError.value = "";


  if (!currentPassword.value) {
    passwordError.value = "Current authorization code is required.";
    return;
  }
  if (!newPassword.value || newPassword.value.length < 6) {
    passwordError.value = "New code must be at least 6 characters long.";
    return;
  }

  try {
    await api.put('/settings/password', {
      currentPassword: currentPassword.value,
      newPassword: newPassword.value
    });
    showStatus("Security protocols updated successfully.", "success");
    currentPassword.value = "";
    newPassword.value = "";
  } catch (e: any) {

    passwordError.value = e.response?.data || "Failed to update security protocols.";
  }
};

const fetchUserProfile = async () => {
  try {
    const response = await api.get('/users/profile');
    username.value = response.data.username;
    email.value = response.data.email;
    provider.value = response.data.provider;
  } catch (e: any) {
    console.error('[SYSTEM] Failed to load citizen identity:', e);
  }
};

onMounted(() => {
  fetchUserProfile();
  const savedTheme = localStorage.getItem("elei_theme") || "emerald";
  changeTheme(savedTheme);
});
</script>

<template>
  <div class="page-wrapper">
    <div class="ambient-glow glow-1"></div>
    <div class="ambient-glow glow-2"></div>

    <div class="topbar">
      <span class="logo">Elei<span class="logo-dot">.</span></span>
      <div class="topbar-actions">
        <button class="back-btn glass-panel" @click="returnToHome">
          <span class="back-icon">←</span>
          <span>Return to Hub</span>
        </button>
      </div>
    </div>

    <div class="content">
      <main class="main-workspace">
        <div class="section-header">
          <div class="header-accent"></div>
          <h2>System Configuration</h2>
        </div>

        <transition name="slide-fade">
          <div v-if="statusMessage" class="status-banner" :class="`status-${statusType}`">
            <svg v-if="statusType === 'success'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="status-icon">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="status-icon">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span>{{ statusMessage }}</span>
          </div>
        </transition>

        <div class="settings-grid">
          <div class="settings-column">

            <div class="glass-panel p-6 mb-6">
              <h3 class="panel-title">Identity Parameters</h3>
              <form @submit.prevent="handleProfileSave" class="space-y-4">
                <div class="input-group">
                  <label>Citizen ID (Username)</label>
                  <input type="text" v-model="username" class="glass-input disabled-input" disabled />
                </div>
                <div class="input-group">
                  <label>Communication Link (Email)</label>
                  <input type="email" v-model="email" class="glass-input" :class="{'input-error': emailError}" />
                  <transition name="slide-fade">
                    <span v-if="emailError" class="error-text">{{ emailError }}</span>
                  </transition>
                </div>
                <button type="submit" class="action-btn" :disabled="isSaving">
                  {{ isSaving ? "Synchronizing..." : "Save Identity" }}
                </button>
              </form>
            </div>

            <div class="glass-panel p-6">
              <h3 class="panel-title">Security Protocols</h3>

              <form v-if="provider === 'LOCAL' || !provider" @submit.prevent="handlePasswordUpdate" class="space-y-4">
                <div class="input-group">
                  <label>Current Password</label>
                  <input
                    type="password"
                    v-model="currentPassword"
                    class="glass-input"
                    :class="{'input-error': passwordError}"
                    placeholder="••••••••"
                  />
                </div>
                <div class="input-group">
                  <label>New Password (Min 6 chars)</label>
                  <input
                    type="password"
                    v-model="newPassword"
                    class="glass-input"
                    :class="{'input-error': passwordError}"
                    placeholder="••••••••"
                  />
                  <transition name="slide-fade">
                    <span v-if="passwordError" class="error-text">{{ passwordError }}</span>
                  </transition>
                </div>
                <button type="submit" class="action-btn danger-btn">
                  Update Security
                </button>
              </form>

              <div v-else>
                <p class="panel-desc" style="margin-bottom: 0;">
                  Your identity is securely managed by <strong style="color: var(--primary-accent); text-transform: capitalize;">{{ provider }}</strong>.
                  <br><br>
                  Security authorization codes must be updated via the external provider's interface.
                </p>
              </div>

            </div>
        </div>

          <div class="settings-column">
            <div class="glass-panel p-6">
              <h3 class="panel-title">Visual Interface (HUD)</h3>
              <p class="panel-desc">
                Select the primary accent color for your workspace.
              </p>

              <div class="theme-selector">
                <button
                  v-for="theme in themes"
                  :key="theme.id"
                  @click="changeTheme(theme.id)"
                  class="theme-btn"
                  :class="{ active: currentTheme === theme.id }"
                >
                  <span
                    class="theme-color-dot"
                    :style="{ backgroundColor: theme.color, boxShadow: `0 0 10px ${theme.color}` }"
                  ></span>
                  <span class="theme-name">{{ theme.name }}</span>
                  <svg
                    v-if="currentTheme === theme.id"
                    class="check-icon"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                  >
                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"></path>
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap");

:root {
  --primary-accent: #10b981;
  --primary-glow: rgba(16, 185, 129, 0.2);
}
[data-theme="blue"] {
  --primary-accent: #3b82f6;
  --primary-glow: rgba(59, 130, 246, 0.2);
}
[data-theme="purple"] {
  --primary-accent: #a855f7;
  --primary-glow: rgba(168, 85, 247, 0.2);
}
[data-theme="rose"] {
  --primary-accent: #e11d48;
  --primary-glow: rgba(225, 29, 72, 0.2);
}

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
  opacity: 0.3;
  pointer-events: none;
  transition: background 0.5s ease;
}
.glow-1 {
  width: 400px; height: 400px;
  background: var(--primary-accent);
  top: -100px; left: -100px;
}
.glow-2 {
  width: 500px; height: 500px;
  background: var(--primary-glow);
  bottom: -200px; right: -100px;
}

.topbar, .content { position: relative; z-index: 1; }
.topbar {
  display: flex; justify-content: space-between; align-items: center;
  padding: 20px 40px; background: rgba(11, 17, 32, 0.5);
  backdrop-filter: blur(12px); border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}
.logo { font-size: 22px; font-weight: 700; letter-spacing: 0.05em; color: #fff; }
.logo-dot { color: var(--primary-accent); transition: color 0.5s ease; }

.glass-panel {
  background: rgba(30, 41, 59, 0.4); backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 16px;
}
.p-6 { padding: 24px; }
.mb-6 { margin-bottom: 24px; }

.back-btn {
  display: flex; align-items: center; gap: 8px; padding: 8px 16px;
  color: #cbd5e1; border-radius: 8px; font-size: 13px; font-weight: 600;
  cursor: pointer; transition: all 0.3s ease; background: transparent; border: 1px solid rgba(255, 255, 255, 0.1);
}
.back-btn:hover { color: #fff; background: rgba(255, 255, 255, 0.05); transform: translateX(-2px); }

.content { display: flex; justify-content: center; padding: 40px; flex: 1; }
.main-workspace { width: 100%; max-width: 900px; }

.section-header { display: flex; align-items: center; gap: 12px; margin-bottom: 24px; }
.header-accent {
  width: 6px; height: 24px; background: var(--primary-accent);
  border-radius: 4px; box-shadow: 0 0 10px var(--primary-glow); transition: all 0.5s ease;
}
.section-header h2 { font-size: 20px; font-weight: 700; margin: 0; }

.settings-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; }
@media (max-width: 768px) { .settings-grid { grid-template-columns: 1fr; } }

.panel-title {
  font-size: 16px; font-weight: 600; margin: 0 0 16px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05); padding-bottom: 12px;
}
.panel-desc { font-size: 13px; color: #94a3b8; margin-bottom: 16px; }

.input-group { display: flex; flex-direction: column; gap: 8px; margin-bottom: 16px; }
.input-group label { font-size: 12px; font-weight: 500; color: #cbd5e1; }
.glass-input {
  background: rgba(15, 23, 42, 0.6); border: 1px solid rgba(255, 255, 255, 0.1);
  padding: 10px 12px; border-radius: 8px; color: #fff; font-size: 14px;
  outline: none; transition: all 0.3s;
}
.glass-input:focus { border-color: var(--primary-accent); }
.glass-input.input-error { border-color: #ef4444; }
.disabled-input { opacity: 0.5; cursor: not-allowed; }

.action-btn {
  width: 100%; padding: 10px; background: var(--primary-accent);
  color: #fff; border: none; border-radius: 8px; font-weight: 600;
  cursor: pointer; transition: all 0.3s; margin-top: 8px;
}
.action-btn:hover { filter: brightness(1.1); }
.action-btn:disabled { opacity: 0.5; cursor: not-allowed; }
.danger-btn { background: transparent; border: 1px solid #ef4444; color: #ef4444; }
.danger-btn:hover { background: rgba(239, 68, 68, 0.1); filter: brightness(1); }


.error-text { color: #ef4444; font-size: 12px; font-weight: 500; }

.status-banner {
  display: flex; align-items: center; gap: 12px;
  padding: 14px 20px; border-radius: 12px; margin-bottom: 24px;
  font-size: 14px; font-weight: 500; backdrop-filter: blur(8px);
}
.status-success {
  background: rgba(16, 185, 129, 0.1); border: 1px solid var(--primary-accent); color: var(--primary-accent);
}
.status-error {
  background: rgba(239, 68, 68, 0.1); border: 1px solid #ef4444; color: #ef4444;
}
.status-icon { width: 20px; height: 20px; }


.slide-fade-enter-active { transition: all 0.4s ease-out; }
.slide-fade-leave-active { transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1); }
.slide-fade-enter-from,
.slide-fade-leave-to { transform: translateY(-10px); opacity: 0; }

.theme-selector { display: flex; flex-direction: column; gap: 12px; }
.theme-btn {
  display: flex; align-items: center; gap: 12px; padding: 12px;
  background: rgba(15, 23, 42, 0.4); border: 1px solid rgba(255, 255, 255, 0.05);
  border-radius: 8px; color: #cbd5e1; cursor: pointer; transition: all 0.2s;
}
.theme-btn:hover { background: rgba(255, 255, 255, 0.05); }
.theme-btn.active { border-color: var(--primary-accent); background: var(--primary-glow); color: #fff; }
.theme-color-dot { width: 16px; height: 16px; border-radius: 50%; }
.theme-name { flex: 1; text-align: left; font-size: 14px; font-weight: 500; }
.check-icon { width: 18px; height: 18px; color: var(--primary-accent); }
</style>

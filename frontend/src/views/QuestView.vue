<script setup lang="ts">
import { useRouter } from "vue-router";

import QuestList from "../components/quest/QuestList.vue";

const router = useRouter();

const returnToHub = () => {
  router.push("/");
};
</script>

<template>
  <div class="page-wrapper">
    <div class="ambient-glow glow-1"></div>
    <div class="ambient-glow glow-2"></div>

    <div class="topbar">
      <span class="logo">Elei<span class="logo-dot">.</span></span>

      <div class="topbar-actions">
        <button class="back-btn glass-panel" @click="returnToHub">
          <span class="back-icon">←</span>
          <span>Return to Hub</span>
        </button>
      </div>
    </div>

    <div class="content">
      <main class="main-workspace">
        <QuestList />
      </main>
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
  background: rgba(16, 185, 129, 0.2);
  top: -100px;
  left: -100px;
}
.glow-2 {
  width: 500px;
  height: 500px;
  background: rgba(59, 130, 246, 0.15);
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
}

.glass-panel {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  color: #cbd5e1;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.back-btn:hover {
  color: #ffffff;
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.15);
  transform: translateX(-2px);
}

.content {
  flex: 1;
  display: flex;
  justify-content: center;
  padding: 40px;
}

.main-workspace {
  width: 100%;
  max-width: 900px;
}

@media (max-width: 860px) {
  .content {
    padding: 20px;
  }
  .topbar {
    padding: 16px 20px;
  }
}
</style>

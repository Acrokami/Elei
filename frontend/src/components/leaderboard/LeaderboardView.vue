<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import leaderboardService from "../../service/leaderboard.service";
import type { LeaderboardEntryDto } from "../../service/leaderboard.service";

const router = useRouter();
const leaderboard = ref<LeaderboardEntryDto[]>([]);
const isLoading = ref<boolean>(true);
const errorMessage = ref<string | null>(null);

const fetchLeaderboard = async () => {
  try {
    isLoading.value = true;
    errorMessage.value = null;
    leaderboard.value = await leaderboardService.getTopCitizens(10);
  } catch (error) {
    console.error("Failed to load leaderboard", error);
    errorMessage.value = "Failed to load leaderboard. Server connection error.";
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  fetchLeaderboard();
});

const getRankBadge = (index: number) => {
  if (index === 0) return "🥇";
  if (index === 1) return "🥈";
  if (index === 2) return "🥉";
  return `#${index + 1}`;
};

const handleHome = () => {
  router.push("/");
};
</script>

<template>
  <div class="page-wrapper">
    <div class="ambient-glow glow-1"></div>
    <div class="ambient-glow glow-2"></div>

    <div class="topbar">
      <span class="logo">Elei<span>.</span></span>
      <button class="back-btn" @click="handleHome">
        <svg
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
          class="back-icon"
        >
          <line x1="19" y1="12" x2="5" y2="12"></line>
          <polyline points="12 19 5 12 12 5"></polyline>
        </svg>
        Dashboard
      </button>
    </div>

    <div class="leaderboard-container">
      <div class="header-section">
        <h1 class="glow-title">Hall of Fame</h1>
        <p class="subtitle">Top 10 citizens of Elei</p>
      </div>

      <div v-if="isLoading" class="status-message">
        <span class="loader-icon">⌛</span>
        Loading rankings...
      </div>

      <div v-else-if="errorMessage" class="status-message error-text">
        {{ errorMessage }}
      </div>

      <div v-else-if="leaderboard.length === 0" class="status-message">
        No one is here yet. Be the first!
      </div>

      <TransitionGroup v-else name="list" tag="div" class="ranking-list">
        <div
          v-for="(player, index) in leaderboard"
          :key="player.id"
          class="rank-card glass-panel"
          :class="{
            'rank-1': index === 0,
            'rank-2': index === 1,
            'rank-3': index === 2,
          }"
        >
          <div class="rank-position">
            <span class="badge">{{ getRankBadge(index) }}</span>
          </div>

          <div class="player-info">
            <span class="player-name">{{ player.username }}</span>
          </div>

          <div class="player-score">
            <span class="xp-value">{{ player.score }}</span>
            <span class="xp-label">XP</span>
          </div>
        </div>
      </TransitionGroup>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap");

* {
  box-sizing: border-box;
  font-family: "Inter", sans-serif;
}

.page-wrapper {
  min-height: 100vh;
  width: 100%;
  background-color: #0b1120;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow-x: hidden;
}

.ambient-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(120px);
  z-index: 0;
  opacity: 0.2;
  pointer-events: none;
}
.glow-1 {
  width: 500px;
  height: 500px;
  background: rgba(251, 191, 36, 0.3);
  top: -150px;
  left: 50%;
  transform: translateX(-50%);
}
.glow-2 {
  width: 400px;
  height: 400px;
  background: rgba(59, 130, 246, 0.2);
  bottom: 0;
  right: -100px;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 32px;
  background: rgba(11, 17, 32, 0.6);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  position: sticky;
  top: 0;
  z-index: 50;
}

.logo {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 0.05em;
  background: linear-gradient(to right, #ffffff, #94a3b8);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.logo span {
  color: var(--primary-accent);
  -webkit-text-fill-color: var(--primary-accent);
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.05);
  color: #e2e8f0;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
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

.leaderboard-container {
  max-width: 800px;
  width: 100%;
  margin: 0 auto;
  padding: 40px 20px;
  color: #e5e7eb;
  position: relative;
  z-index: 1;
}

.header-section {
  text-align: center;
  margin-bottom: 40px;
}

.glow-title {
  font-size: 2.5rem;
  color: #ffffff;
  text-shadow: 0 0 15px rgba(255, 255, 255, 0.3);
  margin: 0 0 8px 0;
}

.subtitle {
  color: var(--primary-accent);
  text-transform: uppercase;
  letter-spacing: 2px;
  font-size: 0.9rem;
  font-weight: 600;
}

.status-message {
  text-align: center;
  padding: 40px;
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  color: #9ca3af;
  font-size: 1.1rem;
}

.error-text {
  color: #ef4444;
  border: 1px solid rgba(239, 68, 68, 0.2);
  background: rgba(239, 68, 68, 0.05);
}

.loader-icon {
  display: inline-block;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  100% {
    transform: rotate(360deg);
  }
}

.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.glass-panel {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.rank-card {
  display: flex;
  align-items: center;
  gap: 16px;
  border-radius: 12px;
  padding: 16px 24px;
  transition:
    transform 0.2s ease,
    background 0.2s ease,
    box-shadow 0.2s ease,
    border-color 0.2s ease;
}

.rank-card:hover {
  transform: translateX(5px);
  background: rgba(30, 41, 59, 0.6);
  border-color: rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.rank-position {
  width: 50px;
  font-size: 1.5rem;
  font-weight: bold;
  color: #6b7280;
  display: flex;
  align-items: center;
}

.player-info {
  flex-grow: 1;
  margin-right: 16px;
}

.player-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #f8fafc;
  letter-spacing: 0.02em;
}

.player-score {
  text-align: right;
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.xp-value {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--primary-accent);
}

.xp-label {
  font-size: 0.8rem;
  font-weight: 600;
  color: #9ca3af;
}

.rank-1 {
  background: linear-gradient(
    90deg,
    rgba(251, 191, 36, 0.15),
    rgba(30, 41, 59, 0.4)
  );
  border-left: 4px solid #fbbf24;
}
.rank-1 .player-name {
  color: #fbbf24;
}

.rank-2 {
  background: linear-gradient(
    90deg,
    rgba(156, 163, 175, 0.15),
    rgba(30, 41, 59, 0.4)
  );
  border-left: 4px solid #9ca3af;
}

.rank-3 {
  background: linear-gradient(
    90deg,
    rgba(180, 83, 9, 0.15),
    rgba(30, 41, 59, 0.4)
  );
  border-left: 4px solid #d97706;
}

.list-enter-active,
.list-leave-active {
  transition: all 0.4s ease;
}
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}
</style>

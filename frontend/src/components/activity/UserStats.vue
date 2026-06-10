<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{
  xp: number;
  level: number;
  nextLevelXp: number;
}>();

const progressPercentage = computed(() => {
  const currentLevelStartXp = Math.pow(props.level - 1, 2) * 100;
  const xpNeededForThisLevel = props.nextLevelXp - currentLevelStartXp;
  const xpEarnedOnThisLevel = props.xp - currentLevelStartXp;

  if (xpNeededForThisLevel <= 0) return 0;
  let percent = (xpEarnedOnThisLevel / xpNeededForThisLevel) * 100;
  return Math.min(Math.max(percent, 0), 100);
});
</script>

<template>
  <div class="stats-container">
    <div class="stats-header">
      <div class="stat-card glass-panel">
        <div class="stat-icon-wrapper blue-glow">
          <svg
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="stat-icon"
          >
            <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"></polygon>
          </svg>
        </div>
        <div class="stat-info">
          <p class="stat-label">Total XP</p>
          <p class="stat-value">{{ xp }}</p>
        </div>
      </div>

      <div class="stat-card glass-panel">
        <div class="stat-icon-wrapper purple-glow">
          <svg
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="stat-icon"
          >
            <polygon
              points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"
            ></polygon>
          </svg>
        </div>
        <div class="stat-info">
          <p class="stat-label">Current Level</p>
          <p class="stat-value">{{ level }}</p>
        </div>
      </div>
    </div>

    <div class="progress-section glass-panel">
      <div class="progress-labels">
        <span class="progress-title">Level Progress</span>
        <span class="progress-numbers"
          ><span class="highlight">{{ xp }}</span> / {{ nextLevelXp }} XP</span
        >
      </div>

      <div class="progress-bar-bg">
        <div
          class="progress-bar-fill"
          :style="{ width: progressPercentage + '%' }"
        >
          <div class="progress-glow"></div>
        </div>
      </div>

      <div class="progress-footer">
        <span
          >{{ Math.round(progressPercentage) }}% to Level {{ level + 1 }}</span
        >
      </div>
    </div>
  </div>
</template>

<style scoped>
* {
  box-sizing: border-box;
  font-family: "Inter", sans-serif;
}

.stats-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
}

.glass-panel {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.stats-header {
  display: flex;
  gap: 16px;
}

.stat-card {
  flex: 1;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition:
    transform 0.3s ease,
    border-color 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  border-color: rgba(255, 255, 255, 0.15);
}

.stat-icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: rgba(15, 23, 42, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.05);
  flex-shrink: 0;
}

.stat-icon {
  width: 24px;
  height: 24px;
}

.blue-glow {
  color: var(--primary-accent);
  box-shadow: inset 0 0 15px rgba(59, 130, 246, 0.2);
}

.purple-glow {
  color: #a855f7;
  box-shadow: inset 0 0 15px rgba(168, 85, 247, 0.2);
}

.stat-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-label {
  color: #94a3b8;
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-weight: 600;
  margin: 0;
}

.stat-value {
  color: #f8fafc;
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  letter-spacing: -0.02em;
  line-height: 1;
}

.progress-section {
  border-radius: 16px;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.progress-labels {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.progress-title {
  font-size: 14px;
  color: #cbd5e1;
  font-weight: 600;
}

.progress-numbers {
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
}

.progress-numbers .highlight {
  color: #f8fafc;
  font-weight: 700;
}

.progress-bar-bg {
  width: 100%;
  height: 12px;
  background-color: rgba(15, 23, 42, 0.6);
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.05);
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
}

.progress-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--primary-accent), #06b6d4);
  border-radius: 8px;
  transition: width 1s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.progress-glow {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transform: translateX(-100%);
  animation: shimmer 2.5s infinite;
}

@keyframes shimmer {
  100% {
    transform: translateX(100%);
  }
}

.progress-footer {
  text-align: right;
  font-size: 12px;
  color: #64748b;
  font-weight: 500;
}
</style>

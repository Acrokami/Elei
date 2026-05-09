<script setup lang="ts">
import {computed} from 'vue';

const props = defineProps<{
    xp: number;
    level: number;
    nextLevelXp: number;
}>();


const progressPercentage = computed(() => {
  const currentLevelStartXp = Math.pow(props.level - 1, 2) * 100;
  const xpNeededForThisLevel = props.nextLevelXp - currentLevelStartXp;
  const xpEarnedOnThisLevel = props.xp - currentLevelStartXp;

  if(xpNeededForThisLevel <= 0) return 0;
  let percent = (xpEarnedOnThisLevel / xpNeededForThisLevel) * 100;
  return Math.min(Math.max(percent, 0), 100);

})

</script>

<template>
  <div class="stats-container">
   <div class="stats-header">
    <div class="stat-card">
      <p class="stat-label">Total XP</p>
      <p class="stat-value">{{ xp }}</p>
    </div>
    <div class="stat-card">
      <p class="stat-label">Level</p>
      <p class="stat-value">{{ level }}</p>
    </div>
  </div>

  <div class="progress-section">
      <div class="progress-labels">
        <span>Level Progress</span>
        <span>{{ xp }} / {{ nextLevelXp }} XP</span>
      </div>
      <div class="progress-bar-bg">
        <div
          class="progress-bar-fill"
          :style="{ width: progressPercentage + '%' }"
        ></div>
      </div>
    </div>

  </div>
</template>

<style scoped>
.stats-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.stats-header {display: flex; gap:16px}
.stat-card {flex: 1; background: #1e293b; border: 1px solid #1e293b; border-radius: 12px; padding: 20px;
text-align: center;}
.stat-label {color: #64748b; font-size: 13px; text-transform: uppercase; letter-spacing: 0.05em; margin: 0 0 8px;}
.stat-value { color: #f1f5f9; font-size: 32px; font-weight: 700; margin: 0; }


.progress-section {
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 12px;
  padding: 16px 20px;
}

.progress-labels {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 14px;
  color: #94a3b8;
  font-weight: 500;
}


.progress-labels span:last-child {
  color: #f1f5f9;
}

.progress-bar-bg {
  width: 100%;
  height: 12px;
  background-color: #0f172a;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #0f172a;
}

.progress-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #3b82f6, #06b6d4);
  border-radius: 6px;
  transition: width 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 0 10px rgba(6, 182, 212, 0.5);
}
</style>

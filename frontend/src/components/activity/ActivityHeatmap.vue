<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{
  activeDates: string[];
}>();

const heatmapDays = computed(() => {
  const ObjectDays = [];
  const today = new Date();

  let currentDayOfWeek = today.getDay() - 1;
  if (currentDayOfWeek === -1) currentDayOfWeek = 6;

  const totalDaysHistory = 12 * 7 + currentDayOfWeek + 1;

  for (let i = totalDaysHistory - 1; i >= 0; i--) {
    const targetDate = new Date(today);
    targetDate.setDate(today.getDate() - i);
    const dateString = targetDate.toISOString().split("T")[0];

    ObjectDays.push({
      date: dateString,
      isActive: props.activeDates.includes(dateString),
      isFuture: false,
    });
  }

  const futureDaysToFill = 6 - currentDayOfWeek;
  for (let i = 1; i <= futureDaysToFill; i++) {
    ObjectDays.push({ date: "future", isActive: false, isFuture: true });
  }

  return ObjectDays;
});
</script>

<template>
  <div class="heatmap-wrapper">
    <div class="heatmap-header">
      <h3 class="heatmap-title">Activity Protocol</h3>
      <span class="heatmap-stats">{{ activeDates.length }} days total</span>
    </div>

    <div class="heatmap-body">
      <div class="day-labels">
        <span>Mon</span>
        <span>Wed</span>
        <span>Fri</span>
      </div>

      <div class="heatmap-grid">
        <div
          v-for="(day, index) in heatmapDays"
          :key="day.date + index"
          class="day-block"
          :class="{ 'is-active': day.isActive, 'is-future': day.isFuture }"
          :title="
            day.isFuture
              ? ''
              : day.isActive
                ? `Protocol active on ${day.date}`
                : `No activity on ${day.date}`
          "
        ></div>
      </div>
    </div>

    <div class="heatmap-footer">
      <div class="heatmap-legend">
        <div class="legend-item">
          <div class="day-block"></div>
          <span>Offline</span>
        </div>
        <div class="legend-item">
          <div class="day-block is-active"></div>
          <span>Active</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.heatmap-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 100%;
}

.heatmap-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.heatmap-title {
  font-size: 15px;
  font-weight: 600;
  color: #e2e8f0;
  margin: 0;
  letter-spacing: 0.02em;
}

.heatmap-stats {
  font-size: 12px;
  color: #64748b;
  font-weight: 500;
}

.heatmap-body {
  display: flex;
  gap: 8px;
  background: rgba(15, 23, 42, 0.4);
  padding: 16px;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.day-labels {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 11px;
  color: #64748b;
  font-weight: 500;
  user-select: none;
}

.day-labels span {
  line-height: 12px;
}

.heatmap-grid {
  display: grid;
  grid-template-rows: repeat(7, 1fr);
  grid-auto-flow: column;
  gap: 4px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.heatmap-grid::-webkit-scrollbar {
  display: none;
}
.heatmap-grid {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.day-block {
  width: 12px;
  height: 12px;
  background-color: rgba(255, 255, 255, 0.04);
  border-radius: 3px;
  transition: all 0.2s ease;
  border: 1px solid rgba(255, 255, 255, 0.02);
}

.day-block:not(.is-future):hover {
  transform: scale(1.2);
  box-shadow: 0 0 10px rgba(59, 130, 246, 0.4);
  border-color: rgba(59, 130, 246, 0.5);
  z-index: 10;
}

.day-block.is-active {
  background-color: var(--primary-accent);
  box-shadow:
    inset 0 0 4px rgba(255, 255, 255, 0.2),
    0 0 6px rgba(59, 130, 246, 0.3);
  border-color: rgba(59, 130, 246, 0.8);
}

.day-block.is-future {
  background-color: transparent;
  border-color: transparent;
  pointer-events: none;
}

.heatmap-footer {
  display: flex;
  justify-content: flex-end;
}

.heatmap-legend {
  display: flex;
  align-items: center;
  gap: 16px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 11px;
  color: #64748b;
  font-weight: 500;
}

.heatmap-legend span {
  margin: 0 4px;
}
</style>

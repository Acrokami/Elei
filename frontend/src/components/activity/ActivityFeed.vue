<script setup lang="ts">
import type { components } from '../../types/api-schemas';

type ActivityFeedItemDto = components['schemas']['ActivityFeedItemDto'];

defineProps<{
  feed: ActivityFeedItemDto[]
}>();

const formatTime = (dateString?: string) => {
  if(!dateString) return '--:--';
  const date = new Date(dateString);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};
</script>

<template>
  <div class="feed-section">
    <div class="section-header">
      <svg class="header-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
      </svg>
      <h2 class="section-title">Activity History</h2>
    </div>

    <TransitionGroup v-if="feed.length > 0" name="list" tag="div" class="feed-list glass-panel">
      <div class="feed-item" v-for="(item, index) in feed" :key="item.logId">

        <div class="feed-time">{{ formatTime(item.createdAt) }}</div>

        <div class="timeline-node">
          <div class="feed-dot"></div>
          <div v-if="index !== feed.length - 1" class="timeline-line"></div>
        </div>

        <div class="feed-content">
          <span class="feed-activity-name">{{ item.activityName }}</span>
          <span class="feed-units">{{ item.unitsCompleted }} {{ item.unitName }}</span>
        </div>

        <div class="feed-xp-badge">
          <span class="xp-plus">+</span>{{ item.earnedXp }} <span class="xp-label">XP</span>
        </div>

      </div>
    </TransitionGroup>

    <div v-else class="empty-state glass-panel">
      <div class="empty-icon-wrapper">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
          <line x1="3" y1="9" x2="21" y2="9"></line>
          <line x1="9" y1="21" x2="9" y2="9"></line>
        </svg>
      </div>
      <p class="empty-title">No activity yet</p>
      <p class="empty-subtitle">Complete your first task to earn XP</p>
    </div>
  </div>
</template>

<style scoped>
.feed-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 100%;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-left: 4px;
}

.header-icon {
  width: 20px;
  height: 20px;
  color: #38bdf8;
}

.section-title {
  color: #f8fafc;
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  letter-spacing: 0.02em;
}


.glass-panel {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.feed-list {
  display: flex;
  flex-direction: column;
  padding: 8px 20px;
}

.feed-item {
  display: flex;
  align-items: flex-start;
  padding: 16px 0;
  position: relative;
}

.feed-time {
  color: #64748b;
  font-size: 13px;
  font-weight: 500;
  width: 60px;
  flex-shrink: 0;
  padding-top: 2px;
}


.timeline-node {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 20px;
  margin: 0 16px 0 8px;
  flex-shrink: 0;
}

.feed-dot {
  width: 10px;
  height: 10px;
  background: #38bdf8;
  border-radius: 50%;
  box-shadow: 0 0 10px rgba(56, 189, 248, 0.6);
  z-index: 2;
  margin-top: 6px;
}

.timeline-line {
  width: 2px;
  height: calc(100% + 20px); /* Соединяет текущую точку со следующей */
  background: rgba(255, 255, 255, 0.1);
  margin-top: 4px;
}

.feed-content {
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 4px;
}

.feed-activity-name {
  color: #f1f5f9;
  font-weight: 600;
  font-size: 15px;
  letter-spacing: 0.01em;
}

.feed-units {
  color: #94a3b8;
  font-size: 13px;
}


.feed-xp-badge {
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
  color: #10b981;
  padding: 6px 12px;
  border-radius: 8px;
  font-weight: 700;
  font-size: 14px;
  display: flex;
  align-items: baseline;
  gap: 2px;
  margin-left: 12px;
}

.xp-plus {
  font-size: 12px;
  opacity: 0.8;
}

.xp-label {
  font-size: 10px;
  font-weight: 600;
  opacity: 0.7;
  margin-left: 2px;
}


.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
}

.empty-icon-wrapper {
  width: 48px;
  height: 48px;
  background: rgba(56, 189, 248, 0.1);
  color: #38bdf8;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}

.empty-icon-wrapper svg {
  width: 24px;
  height: 24px;
}

.empty-title {
  color: #f8fafc;
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 4px 0;
}

.empty-subtitle {
  color: #64748b;
  font-size: 14px;
  margin: 0;
}


.list-enter-active,
.list-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}
</style>

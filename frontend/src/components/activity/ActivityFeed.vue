<script setup lang="ts">

defineProps<{
  feed: any[]
}>();

const formatTime = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};
</script>

<template>
  <div class="feed-section" v-if="feed.length > 0">
    <h2 class="section-title">Activity History</h2>
    <div class="feed-list">
      <div class="feed-item" v-for="item in feed" :key="item.logId">
        <div class="feed-time">{{ formatTime(item.createdAt) }}</div>
        <div class="feed-content">
          <div class="feed-dot"></div>
          <div class="feed-details">
            <span class="feed-activity-name">{{ item.activityName }}</span>
            <span class="feed-units">{{ item.unitsCompleted }} {{ item.unitName }}</span>
          </div>
        </div>
        <div class="feed-xp">+{{ item.earnedXp }} XP</div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.feed-section { margin-top: 32px; }
.section-title { color: #f1f5f9; font-size: 18px; font-weight: 600; margin-bottom: 16px; }
.feed-list { display: flex; flex-direction: column; background: #1e293b; border-radius: 12px; padding: 16px; border: 1px solid #334155; }
.feed-item { display: flex; align-items: center; padding: 12px 0; border-bottom: 1px solid #334155; }
.feed-item:last-child { border-bottom: none; }
.feed-time { color: #64748b; font-size: 12px; width: 50px; flex-shrink: 0; }
.feed-content { display: flex; align-items: center; flex: 1; gap: 12px; padding-left: 12px; }
.feed-dot { width: 8px; height: 8px; background: #38bdf8; border-radius: 50%; box-shadow: 0 0 8px rgba(56, 189, 248, 0.5); }
.feed-details { display: flex; flex-direction: column; }
.feed-activity-name { color: #f1f5f9; font-weight: 500; font-size: 14px; }
.feed-units { color: #94a3b8; font-size: 12px; }
.feed-xp { color: #10b981; font-weight: 700; font-size: 14px; }
</style>

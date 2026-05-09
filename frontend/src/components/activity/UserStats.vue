<script setup lang="ts">

defineProps<{
    xp: number;
    level: number;
}>();

</script>

<template>
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
</template>

<style scoped>
.stats-header {display: flex; gap:16px}
.stat-card {flex: 1; background: #1e293b; border: 1px solid #1e293b; border-radius: 12px; padding: 20px;
text-align: center;}
.stat-label {color: #64748b; font-size: 13px; text-transform: uppercase; letter-spacing: 0.05em; margin: 0 0 8px;}
.stat-value { color: #f1f5f9; font-size: 32px; font-weight: 700; margin: 0; }
</style>

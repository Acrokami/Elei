<script setup lang="ts">
import {ref, onMounted} from 'vue';
import activityService from '../service/activity.service';


const totalExperience = ref(0);
const currentLevel = ref(1);
const categories = ref<any[]>([]);


const loadStats = async () => {
    try {
        const stats = await activityService.getStats();
        console.log('stats:', stats); // 
        totalExperience.value = stats.totalExperience;
        currentLevel.value = stats.currentLevel;
        categories.value = stats.categories;
    } catch (error) {
        console.error("Error compiling statistics");
    }
}

const handleAddActivity = async (activityId: number, amount: number) => {
    try {
    await activityService.completeActivity(activityId, amount);
    await loadStats();

    } catch (error) {
        console.error("Error adding activity:", error);
    }
}

onMounted(() => {
    loadStats()
})

</script>

<template>
  <div class="page-wrapper">

    <div class="topbar">
      <span class="logo">Elei</span>
    </div>

    <div class="content">

      <div class="stats-header">
        <div class="stat-card">
          <p class="stat-label">Total XP</p>
          <p class="stat-value">{{ totalExperience }}</p>
        </div>
        <div class="stat-card">
          <p class="stat-label">Level</p>
          <p class="stat-value">{{ currentLevel }}</p>
        </div>
      </div>

      <div class="categories">
        <div class="category-card" v-for="category in categories" :key="category.activityId">
          <div class="category-info">
            <h3>{{ category.name }}</h3>
            <p>{{ category.userCategoryExperience }} XP collected</p>
          </div>
          <button class="add-btn" @click="handleAddActivity(category.activityId, 10)">
            + Add
          </button>
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

.content {
  max-width: 600px;
  width: 100%;
  margin: 40px auto;
  padding: 0 20px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.stats-header {
  display: flex;
  gap: 16px;
}

.stat-card {
  flex: 1;
  background: #1e293b;
  border: 1px solid #1e293b;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
}

.stat-label {
  color: #64748b;
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin: 0 0 8px;
}

.stat-value {
  color: #f1f5f9;
  font-size: 32px;
  font-weight: 700;
  margin: 0;
}

.categories {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.category-card {
  background: #1e293b;
  border: 1px solid #1e293b;
  border-radius: 12px;
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: border-color 0.2s;
}

.category-card:hover {
  border-color: #334155;
}

.category-info h3 {
  color: #f1f5f9;
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 4px;
}

.category-info p {
  color: #64748b;
  font-size: 13px;
  margin: 0;
}

.add-btn {
  padding: 8px 18px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
  white-space: nowrap;
}

.add-btn:hover {
  background: #1d4ed8;
}
</style>

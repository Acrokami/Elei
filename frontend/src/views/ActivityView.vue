<script setup lang="ts">
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import activityService from '../service/activity.service';

const router = useRouter();

const totalExperience = ref(0);
const currentLevel = ref(1);
const categories = ref<any[]>([]);


const showCreateForm = ref(false);
const newActivity = ref({
  name: '',
  pointsMultiplier: 1,
  unitName: ''
});

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

const handleHome = () => {
  router.push('/')
}

const handleCreateActivity = async() => {
  try {
    await activityService.createActivity(newActivity.value)
    newActivity.value = {name: '', pointsMultiplier: 1, unitName: ''}
    showCreateForm.value = false;

    await loadStats();
  } catch(error) {
    console.error("Error creating new activity", error)
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


      <div class="actions-header">
        <button class="toggle-form-btn" @click="showCreateForm = !showCreateForm">
          {{ showCreateForm ? 'Cancel' : '+ Create Activity' }}
        </button>
      </div>


      <div v-if="showCreateForm" class="create-form-card">
        <form @submit.prevent="handleCreateActivity" class="create-form">
          <div class="form-group">
            <label>Name (Example: Reading)</label>
            <input v-model="newActivity.name" type="text" required placeholder="Enter name"/>
          </div>

          <div class="form-group">
            <label>XP Multiplier</label>
            <input v-model="newActivity.pointsMultiplier" type="number" required placeholder="Enter name"/>
          </div>

          <div class="form-group">
            <label>What is it measured in (minutes, pages)</label>
            <input v-model="newActivity.unitName" type="text" required placeholder="Enter name"/>
          </div>
          <button type="submit" class="submit-btn">Save</button>
        </form>
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

      <button class="home-btn" @click="handleHome">
        <- Main Page
      </button>

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


.actions-header {
  display: flex;
  justify-content: flex-end;
}

.toggle-form-btn {
  background: transparent;
  color: #38bdf8;
  border: 1px solid #38bdf8;
  padding: 8px 16px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.toggle-form-btn:hover {
  background: #38bdf8;
  color: #0f172a;
}

.create-form-card {
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 12px;
  padding: 24px;
}

.create-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  color: #94a3b8;
  font-size: 14px;
}

.form-group input {
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 8px;
  padding: 10px 12px;
  color: #f1f5f9;
  outline: none;
  transition: border-color 0.2s;
}

.form-group input:focus {
  border-color: #38bdf8;
}

.submit-btn {
  margin-top: 8px;
  padding: 12px;
  background: #10b981;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.submit-btn:hover {
  background: #059669;
}

.home-btn {
  background: transparent;
  color: #64748b;
  border: 1px solid #1e293b;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  align-self: flex-start
}

.home-btn:hover {
  border-color: #475547;
  color: #94a3b8
}
</style>

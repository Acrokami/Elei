<script setup lang="ts">
import {ref} from 'vue';
import activityService from '../service/activity.service';


const emit = defineEmits(['activity-created']);

const showCreateForm = ref(false);
const newActivity = ref({
    name: '',
    pointsMultiplier: 0,
    unitName: ''
});

const handleCreateActivity = async() => {
    try {
        await activityService.createActivity(newActivity.value)
        newActivity.value = {name: '', pointsMultiplier: 1, unitName: ''};
        showCreateForm.value = false;

        emit('activity-created')
    } catch (error) {
        console.error("Error creating new activity", error);
    }
}
</script>


<template>
  <div class="form-container">
    <div class="actions-header">
      <button class="toggle-form-btn" @click="showCreateForm = !showCreateForm">
        {{ showCreateForm ? 'Cancel' : '+ Create new Category' }}
      </button>
    </div>

    <div v-if="showCreateForm" class="create-form-card">
      <form @submit.prevent="handleCreateActivity" class="create-form">
        <div class="form-group">
          <label>Name(Example: reading)</label>
          <input v-model="newActivity.name" type="text" required placeholder="Enter name" />
        </div>
        <div class="form-group">
          <label>XP multiplier(points per unit)</label>
          <input v-model="newActivity.pointsMultiplier" type="number" min="1" required />
        </div>
        <div class="form-group">
          <label>What is it measured in?</label>
          <input v-model="newActivity.unitName" type="text" required placeholder="Unit of measurement" />
        </div>
        <button type="submit" class="submit-btn">Save</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.form-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.actions-header { display: flex; justify-content: flex-end; }
.toggle-form-btn { background: transparent; color: #38bdf8; border: 1px solid #38bdf8; padding: 8px 16px; border-radius: 8px; font-weight: 600; cursor: pointer; transition: all 0.2s; }
.toggle-form-btn:hover { background: #38bdf8; color: #0f172a; }
.create-form-card { background: #1e293b; border: 1px solid #334155; border-radius: 12px; padding: 24px; }
.create-form { display: flex; flex-direction: column; gap: 16px; }
.form-group { display: flex; flex-direction: column; gap: 8px; }
.form-group label { color: #94a3b8; font-size: 14px; }
.form-group input { background: #0f172a; border: 1px solid #334155; border-radius: 8px; padding: 10px 12px; color: #f1f5f9; outline: none; }
.form-group input:focus { border-color: #38bdf8; }
.submit-btn { margin-top: 8px; padding: 12px; background: #10b981; color: white; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; }
.submit-btn:hover { background: #059669; }

</style>

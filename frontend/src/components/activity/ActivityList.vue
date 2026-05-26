<script setup lang="ts">
import { ref, watch } from "vue";
import activityService from "../../service/activity.service";

const props = defineProps<{
  categories: any[];
}>();

const emit = defineEmits(['experience-added', 'activity-deleted']);

const amounts = ref<Record<number, number | string>>({});

watch(
  () => props.categories,
  (newCategories) => {
    newCategories.forEach((cat: any) => {
      if (amounts.value[cat.activityId] === undefined) {
        amounts.value[cat.activityId] = "";
      }
    });
  },
  { immediate: true },
);

const handleAddActivity = async (activityId: number) => {
  const amount = Number(amounts.value[activityId]);
  if (!amount || amount <= 0) return;

  try {
    await activityService.completeActivity(activityId, amount);
    amounts.value[activityId] = "";
    emit("experience-added");
  } catch (error) {
    console.error("Error adding activity:", error);
  }
};


const handleDeleteActivity = async (activityId: number) => {
  if(!confirm('System will delete this activity and the whole history. Continue?')) return;

  try {
    await activityService.deleteActivity(activityId);
    emit('activity-deleted');
  } catch (error) {
    console.error("Error deleting activity", error);
  }
}

</script>

<template>
  <div class="categories-container">

    <TransitionGroup v-if="categories && categories.length > 0" name="list" tag="div" class="categories">
      <div
        class="category-card glass-panel"
        v-for="category in categories"
        :key="category.activityId"
      >
        <div class="category-info">
          <div class="title-row">
            <h3>{{ category.name }}</h3>
            <button class="delete-btn" @click="handleDeleteActivity(category.activityId)" title="Delete category">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="3 6 5 6 21 6"></polyline>
                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
              </svg>
            </button>
          </div>

          <div class="xp-badge">
            <span class="xp-value">{{ category.userCategoryExperience }}</span>
            <span class="xp-label">XP Collected</span>
          </div>
        </div>

        <div class="activity-action">
          <div class="input-wrapper">
            <input
              type="number"
              v-model="amounts[category.activityId]"
              :placeholder="category.unitName"
              class="amount-input"
              min="1"
            />
          </div>
          <button
            class="add-btn"
            @click="handleAddActivity(category.activityId)"
            :disabled="!amounts[category.activityId]"
            title="Add progress"
          >
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="12" y1="5" x2="12" y2="19"></line>
              <line x1="5" y1="12" x2="19" y2="12"></line>
            </svg>
          </button>
        </div>
      </div>
    </TransitionGroup>

    <div v-else class="empty-state glass-panel">
      <div class="empty-icon-wrapper">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
          <line x1="12" y1="8" x2="12" y2="16"></line>
          <line x1="8" y1="12" x2="16" y2="12"></line>
        </svg>
      </div>
      <p class="empty-title">No categories found</p>
      <p class="empty-subtitle">Create your first category above to start tracking progress.</p>
    </div>

  </div>
</template>

<style scoped>
* {
  box-sizing: border-box;
  font-family: 'Inter', sans-serif;
}

.categories-container {
  width: 100%;
}

.categories {
  display: flex;
  flex-direction: column;
  gap: 16px;
}


.glass-panel {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.category-card {
  border-radius: 16px;
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.3s ease;
}

.category-card:hover {
  border-color: rgba(255, 255, 255, 0.15);
  background: rgba(30, 41, 59, 0.6);
  transform: translateY(-2px);
}

.category-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
  padding-right: 20px;
}

.title-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.category-info h3 {
  color: #f8fafc;
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  letter-spacing: 0.01em;
}

.delete-btn {
  background: transparent;
  color: #64748b;
  border: none;
  padding: 6px;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.delete-btn svg {
  width: 16px;
  height: 16px;
}

.delete-btn:hover {
  color: #ef4444;
  background: rgba(239, 68, 68, 0.1);
}

.xp-badge {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.xp-value {
  color: #10b981;
  font-size: 15px;
  font-weight: 700;
}

.xp-label {
  color: #64748b;
  font-size: 13px;
  font-weight: 500;
}

.activity-action {
  display: flex;
  gap: 8px;
  align-items: center;
}

.input-wrapper {
  position: relative;
}

.amount-input {
  width: 120px;
  background: rgba(15, 23, 42, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 10px 14px;
  color: #f1f5f9;
  outline: none;
  font-size: 14px;
  transition: all 0.3s ease;
}

.amount-input:focus {
  border-color: #38bdf8;
  background: rgba(15, 23, 42, 0.8);
  box-shadow: 0 0 0 3px rgba(56, 189, 248, 0.2);
}

.amount-input::placeholder {
  color: #475569;
}

.add-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);
  flex-shrink: 0;
}

.add-btn svg {
  width: 20px;
  height: 20px;
}

.add-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(37, 99, 235, 0.3);
}

.add-btn:active:not(:disabled) {
  transform: translateY(0);
}

.add-btn:disabled {
  background: rgba(255, 255, 255, 0.05);
  color: #475569;
  box-shadow: none;
  cursor: not-allowed;
  border: 1px solid rgba(255, 255, 255, 0.05);
}


.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
  border-radius: 16px;
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
.list-leave-active {
  position: absolute;
  width: 100%;
}
</style>

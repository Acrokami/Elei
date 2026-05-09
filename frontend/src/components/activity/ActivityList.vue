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
  <div class="categories">
    <div
      class="category-card"
      v-for="category in categories"
      :key="category.activityId"
    >
      <div class="category-info">
        <div class="title-row">
        <h3>{{ category.name }}</h3>
        <button class="delete-btn" @click="handleDeleteActivity(category.activityId)" title="Delete">×</button>
        </div>
        <p>{{ category.userCategoryExperience }} XP collected</p>
      </div>

      <div class="activity-action">
        <input
          type="number"
          v-model="amounts[category.activityId]"
          :placeholder="category.unitName"
          class="amount-input"
          min="1"
        />
        <button
          class="add-btn"
          @click="handleAddActivity(category.activityId)"
          :disabled="!amounts[category.activityId]"
        >
          +
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
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
.activity-action {
  display: flex;
  gap: 8px;
  align-items: center;
}
.amount-input {
  width: 100px;
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 8px;
  padding: 8px 12px;
  color: #f1f5f9;
  outline: none;
  font-size: 14px;
  transition: border-color 0.2s;
}
.amount-input:focus {
  border-color: #38bdf8;
}
.add-btn {
  padding: 8px 14px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
}
.add-btn:hover:not(:disabled) {
  background: #1d4ed8;
}
.add-btn:disabled {
  background: #334155;
  color: #64748b;
  cursor: not-allowed;
}

.title-row {
  display:flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.delete-btn {
  background: transparent;
  color: #64748b;
  border: none;
  font-size: 20px;
  line-height: 1;
  cursor: pointer;
  padding: 0 4px;
  border-radius: 4px;
  transition: color 0.2s, background 0.2s;
}

.delete-btn:hover {
  color: #ef4444;
  background: rgba(239, 68, 68, 0.1);
}

.category-info h3 {
  color: #f1f5f9;
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}
</style>

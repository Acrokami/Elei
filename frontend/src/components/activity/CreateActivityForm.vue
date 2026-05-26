<script setup lang="ts">
import { ref } from "vue";
import activityService from "../../service/activity.service";

const emit = defineEmits(["activity-created"]);

const showCreateForm = ref(false);
const newActivity = ref({
  name: "",
  pointsMultiplier: 0,
  unitName: "",
});

const handleCreateActivity = async () => {
  try {
    await activityService.createActivity(newActivity.value);
    newActivity.value = { name: "", pointsMultiplier: 0, unitName: "" };
    showCreateForm.value = false;

    emit("activity-created");
  } catch (error) {
    console.error("Error creating new activity", error);
  }
};
</script>

<template>
  <div class="form-container">
    <div class="actions-header">
      <button
        class="toggle-form-btn"
        :class="{ 'is-active': showCreateForm }"
        @click="showCreateForm = !showCreateForm"
      >
        <span class="btn-icon">
          <svg v-if="!showCreateForm" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19"></line>
            <line x1="5" y1="12" x2="19" y2="12"></line>
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </span>
        {{ showCreateForm ? "Cancel" : "Create new Category" }}
      </button>
    </div>

    <Transition name="expand">
      <div v-if="showCreateForm" class="form-wrapper">
        <div class="create-form-card glass-panel">
          <form @submit.prevent="handleCreateActivity" class="create-form">

            <div class="form-group">
              <label>Category Name</label>
              <div class="input-wrapper">
                <input
                  v-model="newActivity.name"
                  type="text"
                  required
                  placeholder="e.g. Reading, Running, Coding"
                />
              </div>
            </div>

            <div class="form-row">
              <div class="form-group half-width">
                <label>XP Multiplier</label>
                <div class="input-wrapper">
                  <input
                    v-model="newActivity.pointsMultiplier"
                    type="number"
                    min="1"
                    required
                    placeholder="Points per unit"
                  />
                </div>
              </div>

              <div class="form-group half-width">
                <label>Unit of Measurement</label>
                <div class="input-wrapper">
                  <input
                    v-model="newActivity.unitName"
                    type="text"
                    required
                    placeholder="e.g. pages, minutes, km"
                  />
                </div>
              </div>
            </div>

            <button type="submit" class="submit-btn">
              <svg class="save-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"></path>
                <polyline points="17 21 17 13 7 13 7 21"></polyline>
                <polyline points="7 3 7 8 15 8"></polyline>
              </svg>
              Save Category
            </button>

          </form>
        </div>
      </div>
    </Transition>
  </div>
</template>

<style scoped>

 * {
  font-family: 'Inter', sans-serif;
  box-sizing: border-box;
 }

.form-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 100%;
}

.actions-header {
  display: flex;
  justify-content: flex-end;
}


.toggle-form-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(56, 189, 248, 0.1);
  color: #38bdf8;
  border: 1px solid rgba(56, 189, 248, 0.2);
  padding: 10px 20px;
  border-radius: 10px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.toggle-form-btn:hover {
  background: rgba(56, 189, 248, 0.2);
  border-color: rgba(56, 189, 248, 0.4);
  transform: translateY(-2px);
}

.toggle-form-btn.is-active {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  border-color: rgba(239, 68, 68, 0.2);
}

.toggle-form-btn.is-active:hover {
  background: rgba(239, 68, 68, 0.2);
}

.btn-icon svg {
  width: 16px;
  height: 16px;
}


.glass-panel {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.create-form-card {
  border-radius: 16px;
  padding: 24px;
}

.create-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: flex;
  gap: 16px;
}

.half-width {
  flex: 1;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  color: #cbd5e1;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.02em;
}

.input-wrapper {
  position: relative;
  width: 100%;
}

.form-group input {
  box-sizing: border-box;
  width: 100%;
  padding: 12px 16px;
  background: rgba(15, 23, 42, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  font-size: 15px;
  color: #f1f5f9;
  outline: none;
  transition: all 0.3s ease;
  font-family: inherit;
}

.form-group input:focus {
  border-color: #38bdf8;
  background: rgba(15, 23, 42, 0.8);
  box-shadow: 0 0 0 3px rgba(56, 189, 248, 0.2);
}

.form-group input::placeholder {
  color: #475569;
}


.submit-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  padding: 14px;
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2);
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(16, 185, 129, 0.3);
}

.submit-btn:active {
  transform: translateY(0);
}

.save-icon {
  width: 18px;
  height: 18px;
}


.expand-enter-active,
.expand-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  max-height: 400px;
  opacity: 1;
  overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
  max-height: 0;
  opacity: 0;
  transform: translateY(-10px);
}
</style>

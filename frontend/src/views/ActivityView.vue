<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import activityService from "../service/activity.service";
import UserStats from "../components/activity/UserStats.vue";
import CreateActivityForm from "../components/activity/CreateActivityForm.vue";
import ActivityList from "../components/activity/ActivityList.vue";
import ActivityFeed from "../components/activity/ActivityFeed.vue";

const router = useRouter();

const totalExperience = ref(0);
const currentLevel = ref(1);
const categories = ref<any[]>([]);
const feed = ref<any[]>([]);

const nextLevelXp = ref(100);

const loadStats = async () => {
  try {
    const stats = await activityService.getStats();
    totalExperience.value = stats.totalExperience;
    currentLevel.value = stats.currentLevel;
    nextLevelXp.value = stats.nextLevelXp;
    categories.value = stats.categories;
    feed.value = await activityService.getFeed();
  } catch (error) {
    console.error("Error compiling statistics");
  }
};

const handleHome = () => {
  router.push("/");
};

onMounted(() => {
  loadStats();
});
</script>

<template>
  <div class="page-wrapper">
    <div class="ambient-glow glow-1"></div>
    <div class="ambient-glow glow-2"></div>

    <div class="topbar">
      <span class="logo">Elei<span>.</span></span>
      <button class="back-btn" @click="handleHome">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="back-icon">
          <line x1="19" y1="12" x2="5" y2="12"></line>
          <polyline points="12 19 5 12 12 5"></polyline>
        </svg>
        Dashboard
      </button>
    </div>

    <div class="content">
      <div class="dashboard-stack">
        <UserStats
          :xp="totalExperience"
          :level="currentLevel"
          :nextLevelXp="nextLevelXp"
        />

        <CreateActivityForm
          @activity-created="loadStats"
        />

        <ActivityList
          :categories="categories"
          @experience-added="loadStats"
          @activity-deleted="loadStats"
        />

        <ActivityFeed
          :feed="feed"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.page-wrapper {
  min-height: 100vh;
  width: 100%;
  background-color: #0b1120;
  font-family: 'Inter', sans-serif;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow-x: hidden;
}


.ambient-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(120px);
  z-index: 0;
  opacity: 0.25;
  pointer-events: none;
}
.glow-1 {
  width: 500px;
  height: 500px;
  background: rgba(16, 185, 129, 0.3);
  top: -150px;
  left: -150px;
}
.glow-2 {
  width: 400px;
  height: 400px;
  background: rgba(59, 130, 246, 0.2);
  bottom: 10%;
  right: -100px;
}


.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 32px;
  background: rgba(11, 17, 32, 0.6);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  position: sticky;
  top: 0;
  z-index: 50;
}

.logo {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 0.05em;
  background: linear-gradient(to right, #ffffff, #94a3b8);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.logo span {
  color: #3b82f6;
  -webkit-text-fill-color: #3b82f6;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.05);
  color: #e2e8f0;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
  transform: translateX(-2px);
}

.back-icon {
  width: 16px;
  height: 16px;
  transition: transform 0.2s ease;
}

.back-btn:hover .back-icon {
  transform: translateX(-3px);
}

.content {
  flex: 1;
  width: 100%;
  max-width: 700px;
  margin: 0 auto;
  padding: 40px 20px;
  position: relative;
  z-index: 1;
}


.dashboard-stack {
  display: flex;
  flex-direction: column;
  gap: 32px;
}
</style>

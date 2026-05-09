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

const loadStats = async () => {
  try {
    const stats = await activityService.getStats();
    totalExperience.value = stats.totalExperience;
    currentLevel.value = stats.currentLevel;
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
    <div class="topbar">
      <span class="logo">Elei</span>
    </div>

    <div class="content">
      <UserStats :xp="totalExperience" :level="currentLevel" />

      <CreateActivityForm @activity-created="loadStats"
       />

      <ActivityList :categories="categories"
       @experience-added="loadStats"
       @activity-deleted="loadStats" />

      <ActivityFeed :feed="feed" />

      <button class="home-btn" @click="handleHome"><- Main Page</button>
    </div>
  </div>
</template>

<style scoped>
.page-wrapper {
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
  align-self: flex-start;
}

.home-btn:hover {
  border-color: #475547;
  color: #94a3b8;
}
</style>

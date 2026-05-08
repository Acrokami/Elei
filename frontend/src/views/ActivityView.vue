<script setup lang="ts">
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import activityService from '../service/activity.service';
import CreateActivityForm from '../components/CreateActivityForm.vue';
import ActivityList from '../components/ActivityList.vue';

const router = useRouter();

const totalExperience = ref(0);
const currentLevel = ref(1);
const categories = ref<any[]>([]);
const feed = ref<any[]>([]);



const formatTime = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleDateString([], {hour: '2-digit', minute: '2-digit'});
};

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
}



const handleHome = () => {
  router.push('/')
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


      <CreateActivityForm @activity-created="loadStats"/>

      <ActivityList
      :categories="categories"
      @experience-added="loadStats"/>



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


.feed-section {
  margin-top: 32px;
}

.section-title {
  color: #f1f5f9;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}

.feed-list {
  display: flex;
  flex-direction: column;
  background: #1e293b;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid #334155;
}

.feed-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #334155
}

.feed-item:last-child {
  border-bottom: none;
}

.feed-time {
  color: #64748b;
  font-size: 12px;
  width: 50px;
  flex-shrink: 0;
}


.feed-content {
  display: flex;
  align-items: center;
  flex: 1;
  gap: 12px;
  padding-left: 12px;
}

.feed-dot {
  width: 8px;
  height: 8px;
  background: #38bdf8;
  border-radius: 50%;
  box-shadow: 0 0 8px rgba(56, 189, 248, 0.5);
}

.feed-details {
  display: flex;
  flex-direction: column;
}

.feed-activity-name {
  color: #f1f5f9;
  font-weight: 500;
  font-size: 14px;
}

.feed-units {
  color: #94a3b8;
  font-size: 12px;
}

.feed-xp {
  color: #10b981;
  font-weight: 700;
  font-size: 14px;
}





</style>

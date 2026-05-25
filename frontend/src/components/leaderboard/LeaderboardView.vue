<script setup lang="ts">
import  {ref, onMounted} from 'vue';
interface LeaderboardEntry {
    id: number;
    username: string;
    score:number;
}
const leaderboard = ref<LeaderboardEntry[]>([]);
const isLoading = ref<boolean>(true);
const errorMessage = ref<string | null>(null);

const fetchLeaderboard = async () => {
    try {
        const token = localStorage.getItem('user_token');
        const response = await fetch('http://localhost:8080/api/leaderboard/top?limit=10', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if(response.ok) {
            leaderboard.value = await response.json();
        } else {
            errorMessage.value = 'Failed to load leaderboard. The server returned an error..';
        }

    } catch (error) {
        console.error('Failed to load leaderboard', error);
        errorMessage.value = 'Server connection error.';
    } finally {
        isLoading.value = false;
    }
};

onMounted(() => {
    fetchLeaderboard()
});

const getRankBadge = (index: number) => {
    if (index === 0) return '🥇';
    if (index === 1) return '🥈';
    if (index === 2) return '🥉';
    return `#${index + 1}`;
};
</script>


<template>

    <div class="leaderboard-container">
    <div class="header-section">
      <h1 class="glow-title">Hall of Fame</h1>
      <p class="subtitle">Top 10 citizens of Elei</p>
    </div>

    <div v-if="isLoading" class="status-message error-text">
      <span class="loader-icon">⏰</span>
      Loading rankings...
    </div>

    <div v-else-if="leaderboard.length === 0 " class="status-message">
      No one is here yet. Be the first!
    </div>



    <TransitionGroup v-else name="list" tag="div" class="ranking-list">
      <div
        v-for="(player, index) in leaderboard"
        :key="player.id"
        class="rank-card"
        :class="{ 'rank-1': index === 0, 'rank-2': index === 1, 'rank-3': index === 2 }"
      >
        <div class="rank-position">
          <span class="badge">{{ getRankBadge(index) }}</span>
        </div>

        <div class="player-info">
          <span class="player-name">{{ player.username }}</span>
        </div>

        <div class="player-score">
          <span class="xp-value">{{ player.score }}</span>
          <span class="xp-label">XP</span>
        </div>
      </div>
    </TransitionGroup>

  </div>

</template>

<style scoped>
.leaderboard-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
  color: #e5e7eb;
}

.header-section {
  text-align: center;
  margin-bottom: 40px;
}

.glow-title {
  font-size: 2.5rem;
  color: #ffffff;
  text-shadow: 0 0 15px rgba(255, 255, 255, 0.3);
  margin: 0 0 8px 0;
}

.subtitle {
  color: #10b981;
  text-transform: uppercase;
  letter-spacing: 2px;
  font-size: 0.9rem;
  font-weight: 600;
}


.status-message {
  text-align: center;
  padding: 40px;
  background: rgba(31, 40, 51, 0.2);
  border-radius: 12px;
  color: #9ca3af;
  font-size: 1.1rem;
}

.error-text {
  color: #ef4444;
  border: 1px solid rgba(239, 68, 68, 0.2);
}

.loader-icon {
  display: inline-block;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  100% { transform: rotate(360deg); }
}

.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.rank-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: rgba(31, 40, 51, 0.4);
  border: 1px solid rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  padding: 16px 24px;
  transition: transform 0.2s ease, background 0.2s ease, box-shadow 0.2s ease;
}

.rank-card:hover {
  transform: translateX(5px);
  background: rgba(31, 40, 51, 0.8);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.rank-position {
  width: 50px;
  font-size: 1.5rem;
  font-weight: bold;
  color: #6b7280;
  display: flex;
  align-items: center;
}

.player-info {
  flex-grow: 1;
  margin-right: 16px;
}

.player-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #f3f4f6;
  letter-spacing: 0.02em;
}

.player-score {
  text-align: right;
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.xp-value {
  font-size: 1.25rem;
  font-weight: 700;
  color: #10b981;
}

.xp-label {
  font-size: 0.8rem;
  font-weight: 600;
  color: #9ca3af;
}


.rank-1 {
  background: linear-gradient(90deg, rgba(251, 191, 36, 0.1), rgba(31, 40, 51, 0.4));
  border-left: 4px solid #fbbf24;
}
.rank-1 .player-name { color: #fbbf24; }

.rank-2 {
  background: linear-gradient(90deg, rgba(156, 163, 175, 0.1), rgba(31, 40, 51, 0.4));
  border-left: 4px solid #9ca3af;
}

.rank-3 {
  background: linear-gradient(90deg, rgba(180, 83, 9, 0.1), rgba(31, 40, 51, 0.4));
  border-left: 4px solid #d97706;
}


.list-enter-active,
.list-leave-active {
  transition: all 0.4s ease;
}
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}
</style>

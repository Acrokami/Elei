<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import type { components } from '../../types/api-schemas';
import api from '../../service/api';
import { Client, type IMessage } from '@stomp/stompjs';

type QuestProgressDto = components['schemas']['QuestProgressDto'];

const quests = ref<QuestProgressDto[]>([]);
const isLoading = ref<boolean>(true);
const error = ref<string | null>(null);

let stompClient: Client | null = null;



// Token decoding function to get username
const getUsernameFromToken = (): string | null => {
  const token = localStorage.getItem('user_token');
  if (!token) {
    console.warn('[SYSTEM] Token not found in local storage.');
    return null;
  }
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    const payload = JSON.parse(jsonPayload);
    console.log('[SYSTEM] Decoded JWT payload:', payload);


    return payload.sub || payload.username || null;
  } catch (e) {
    console.error('[SYSTEM] Token decoding failed:', e);
    return null;
  }
};

const fetchQuests = async () => {
    try {
        isLoading.value = true;
        error.value = null;
        const response = await api.get('/quests');
        quests.value = response.data;
    } catch (e) {
        error.value = 'Failed to synchronize quests protocols';
        console.error('Failed to synchronize quests protocols', e);
    } finally {
        isLoading.value = false;
    }
};

const connectWebSocket = () => {
    const username = getUsernameFromToken();
    if (!username) return;

    stompClient = new Client({
        brokerURL: 'ws://localhost:8080/ws-notifications',
        connectHeaders: {
            Authorization: `Bearer ${localStorage.getItem('user_token')}`
        },
        reconnectDelay: 5000,
        onConnect: () => {
            console.log('[SYSTEM] Quest live-sync activated.');

            stompClient?.subscribe(`/topic/quests/${username}`, (message: IMessage) => {
                const updatedQuest: QuestProgressDto = JSON.parse(message.body);


                const index = quests.value.findIndex(q => q.questId === updatedQuest.questId);
                if (index !== -1) {
                    quests.value.splice(index, 1, updatedQuest);
                }
            });
        },
        onStompError: (frame) => {
            console.error('[SYSTEM] Broker error:', frame.headers['message']);
        }
    });
    stompClient.activate();
};

onMounted(() => {
  fetchQuests();
  connectWebSocket();
});

onUnmounted(() => {
  if(stompClient) {
    stompClient.deactivate();
  }
});

const calculateProgress = (current?: number, target?: number): number => {
    if (!current || !target) return 0;
    return Math.min((current / target) * 100, 100);
};
</script>

<template>
  <div class="protocols-wrapper">
    <div class="section-header">
      <div class="header-accent"></div>
      <h2>Active Protocols</h2>
    </div>

    <div v-if="isLoading" class="loading-state">
      <div class="spinner"></div>
      <span>Synchronizing systems...</span>
    </div>

    <div v-else-if="error" class="error-state">
      <span class="error-icon">⚠️</span> {{ error }}
    </div>

    <div v-else-if="quests.length === 0" class="empty-state glass-panel">
      <div class="empty-icon-wrapper">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 002-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
        </svg>
      </div>
      <h3 class="empty-title">No Active Protocols</h3>
      <p class="empty-desc">Your quest log is currently empty. The system will automatically assign new objectives based on your progression.</p>
    </div>

    <div v-else class="protocols-grid">
      <div
        v-for="(quest, index) in quests"
        :key="quest.questId ?? index"
        class="quest-card glass-panel"
        :class="{ 'is-completed': quest.isCompleted }"
      >
        <div class="status-icon">
          <svg v-if="quest.isCompleted" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M20 6L9 17l-5-5"></path>
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <polyline points="12 6 12 12 16 14"></polyline>
          </svg>
        </div>

        <h3 class="quest-title">{{ quest.title ?? '—' }}</h3>
        <p class="quest-desc">{{ quest.description ?? '—' }}</p>

        <div class="progress-section">
          <div class="progress-stats">
            <span class="xp-reward">+{{ quest.rewardXp ?? 0 }} XP</span>
            <span class="count-tracker">
              {{ Math.min(quest.currentCount ?? 0, quest.targetCount ?? 0) }} / {{ quest.targetCount ?? 0 }}
            </span>
          </div>

          <div class="progress-track">
            <div
              class="progress-fill"
              :style="{ width: `${calculateProgress(quest.currentCount, quest.targetCount)}%` }"
            ></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.protocols-wrapper {
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
  font-family: 'Inter', sans-serif;
}


.section-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.header-accent {
  width: 6px;
  height: 24px;
  background: #3b82f6;
  border-radius: 4px;
  box-shadow: 0 0 10px rgba(59, 130, 246, 0.5);
}

.section-header h2 {
  font-size: 20px;
  font-weight: 700;
  color: #f8fafc;
  letter-spacing: 0.02em;
  margin: 0;
}


.glass-panel {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.05);
  border-radius: 16px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}


.protocols-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}


.quest-card {
  position: relative;
  padding: 24px;
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

.quest-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, rgba(59, 130, 246, 0.5), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.quest-card:hover {
  transform: translateY(-4px);
  border-color: rgba(255, 255, 255, 0.1);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.2);
}

.quest-card:hover::before {
  opacity: 1;
}


.quest-title {
  font-size: 16px;
  font-weight: 600;
  color: #f8fafc;
  margin: 0 0 8px 0;
  padding-right: 32px;
}

.quest-desc {
  font-size: 13px;
  color: #94a3b8;
  margin: 0 0 24px 0;
  line-height: 1.5;
  display: -webkit-box;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex-grow: 1;
}


.status-icon {
  position: absolute;
  top: 24px;
  right: 24px;
  width: 20px;
  height: 20px;
  color: #64748b;
  transition: color 0.3s ease;
}


.progress-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: auto;
}

.progress-stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  font-weight: 600;
}

.xp-reward {
  color: #3b82f6;
}

.count-tracker {
  color: #cbd5e1;
}

.progress-track {
  width: 100%;
  height: 6px;
  background: rgba(15, 23, 42, 0.6);
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #2563eb, #3b82f6);
  border-radius: 4px;
  transition: width 1s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 0 10px rgba(59, 130, 246, 0.5);
}


.quest-card.is-completed {
  background: rgba(16, 185, 129, 0.03);
  border-color: rgba(16, 185, 129, 0.2);
}

.quest-card.is-completed:hover {
  box-shadow: 0 12px 40px rgba(16, 185, 129, 0.1);
}

.quest-card.is-completed::before {
  background: linear-gradient(90deg, transparent, rgba(16, 185, 129, 0.8), transparent);
}

.quest-card.is-completed .status-icon {
  color: #10b981;
}

.quest-card.is-completed .xp-reward {
  color: #10b981;
  text-shadow: 0 0 10px rgba(16, 185, 129, 0.3);
}

.quest-card.is-completed .progress-fill {
  background: linear-gradient(90deg, #059669, #10b981);
  box-shadow: 0 0 12px rgba(16, 185, 129, 0.6);
}


.loading-state, .error-state {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 40px;
  background: rgba(30, 41, 59, 0.2);
  border-radius: 12px;
  border: 1px dashed rgba(255, 255, 255, 0.1);
}

.loading-state span {
  color: #3b82f6;
  font-size: 14px;
  font-weight: 500;
}

.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(59, 130, 246, 0.2);
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.error-state {
  color: #f87171;
  font-size: 14px;
  background: rgba(248, 113, 113, 0.05);
  border-color: rgba(248, 113, 113, 0.2);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
  border-style: dashed;
  border-width: 1px;
  border-color: rgba(148, 163, 184, 0.2);
}

.empty-icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
  margin-bottom: 20px;
  border: 1px solid rgba(59, 130, 246, 0.2);
  box-shadow: 0 0 20px rgba(59, 130, 246, 0.1);
}

.empty-icon-wrapper svg {
  width: 32px;
  height: 32px;
}

.empty-title {
  font-size: 18px;
  font-weight: 600;
  color: #f8fafc;
  margin: 0 0 8px 0;
}

.empty-desc {
  font-size: 14px;
  color: #94a3b8;
  max-width: 400px;
  margin: 0;
  line-height: 1.5;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>

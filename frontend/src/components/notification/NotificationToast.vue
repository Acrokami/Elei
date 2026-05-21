<script setup lang="ts">
import {ref, onMounted, onUnmounted} from 'vue';
import { Client, type IMessage, type IFrame } from '@stomp/stompjs';

const notificationMessage = ref<string>('');
const isNotificationVisible = ref<boolean>(false);

let stompClient: Client | null = null;
let timeoutId: ReturnType<typeof setTimeout> | null = null;

const closeNotification = () => {
  isNotificationVisible.value = false;
}

onMounted(() => {
  stompClient = new Client({
    brokerURL: 'ws://localhost:8081/ws-notifications',
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,

    onConnect: () => {
      console.log('Connected to notification WebSocket server.');


      stompClient?.subscribe('/topic/levelup', (message: IMessage) => {
        console.log('Received real-time event:', message.body);

        notificationMessage.value = message.body;
        isNotificationVisible.value = true;


        if (timeoutId) {
          clearTimeout(timeoutId);
        }

        timeoutId = setTimeout(() => {
          isNotificationVisible.value = false;
        }, 5000);
      });
    },

    onStompError: (frame: IFrame) => {
      console.error('Broker reported error:', frame.headers['message']);
      console.error('Additional details:', frame.body);
    },
  });

  stompClient.activate();
});

onUnmounted(() => {
    if(timeoutId) clearTimeout(timeoutId);
    if(stompClient) stompClient.deactivate();
});

</script>

<template>
<Transition name="toast">
    <div v-if="isNotificationVisible" class="toast-card">
      <div class="toast-body">
        <div class="icon-wrapper">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-award">
            <circle cx="12" cy="8" r="7"></circle>
            <polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88"></polyline>
          </svg>
        </div>

        <div class="content-wrapper">
          <h4 class="toast-title">Achievement Unlocked</h4>
          <p class="toast-text">{{ notificationMessage }}</p>
        </div>

        <button @click="closeNotification" class="close-btn" aria-label="Close notification">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>
      </div>

      <div class="progress-bar"></div>
    </div>
  </Transition>
</template>

<style scoped>


.toast-card {
  position: fixed;
  top: 24px;
  right: 24px;
  width: 360px;
  background: rgba(20, 20, 25, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-left: 4px solid #10b981;
  border-radius: 12px;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.5), 0 10px 10px -5px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  z-index: 9999;
}

.toast-body {
  display: flex;
  align-items: center;
  padding: 16px;
  gap: 16px;
}


.icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
  border-radius: 8px;
  flex-shrink: 0;
  box-shadow: 0 0 15px rgba(16, 185, 129, 0.2);
}

.icon-wrapper svg {
  width: 22px;
  height: 22px;
}


.content-wrapper {
  flex-grow: 1;
}

.toast-title {
  margin: 0 0 4px 0;
  font-size: 15px;
  font-weight: 600;
  color: #ffffff;
  letter-spacing: 0.3px;
}

.toast-text {
  margin: 0;
  font-size: 13px;
  color: #9ca3af;
  line-height: 1.4;
}


.close-btn {
  background: none;
  border: none;
  color: #4b5563;
  cursor: pointer;
  padding: 4px;
  border-radius: 6px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
}

.close-btn:hover {
  color: #ffffff;
  background: rgba(255, 255, 255, 0.05);
}

.close-btn svg {
  width: 16px;
  height: 16px;
}

.progress-bar {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 3px;
  background: linear-gradient(90deg, #10b981, #059669);
  width: 100%;
  animation: countdown 5s linear forwards;
}

@keyframes countdown {
  from { width: 100%; }
  to { width: 0%; }
}

.toast-enter-active {
  transition: all 0.4s cubic-bezier(0.16, 1, 0.3, 1);
}

.toast-leave-active {
  transition: all 0.3s ease-in;
}

.toast-enter-from {
  transform: translateX(120%) scale(0.9);
  opacity: 0;
}

.toast-leave-to {
  transform: translateY(-20px);
  opacity: 0;
}
</style>

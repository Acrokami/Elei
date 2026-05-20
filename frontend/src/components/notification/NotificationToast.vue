<script setup lang="ts">
import {ref, onMounted, onUnmounted} from 'vue';
import { Client, type IMessage, type IFrame } from '@stomp/stompjs';

const notificationMessage = ref<string>('');
const isNotificationVisible = ref<boolean>(false);

let stompClient: Client | null = null;
let timeoutId: ReturnType<typeof setTimeout> | null = null;

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
    if(timeoutId) {
        clearTimeout(timeoutId)
    }
    if(stompClient) {
        stompClient.deactivate();
        console.log('Disconnected from WebSocket server.')
    }
})

</script>

<template>
<div>
    <div v-if="isNotificationVisible" class="notification-toast">
        <p> Achievement unlocked</p>
        <span>{{ notificationMessage }}</span>
    </div>
</div>
</template>

<style scoped>
.notification-toast {
  position: fixed;
  top: 20px;
  right: 20px;
  background-color: #4CAF50;
  color: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  z-index: 1000;
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from { transform: translateX(100%); opacity: 0; }
  to { transform: translateX(0); opacity: 1; }
}
</style>

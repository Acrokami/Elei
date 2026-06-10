<script setup lang="ts">
import { RouterView }from 'vue-router';
import { onMounted } from 'vue';
import AppFooter from './components/layouts/AppFooter.vue';
import NotificationToast from './components/notification/NotificationToast.vue';

onMounted(() => {
  const savedTheme = localStorage.getItem('elei_theme') || 'emerald';
  document.documentElement.setAttribute('data-theme', savedTheme);
  console.log(`[SYSTEM] Visual interface initialized with theme: ${savedTheme}`);
});
</script>



<template>
  <div class="app-root">
  <RouterView class="main-content"/>
  <AppFooter/>
  <NotificationToast/>
  </div>
</template>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  background-color: #0f172a;
}

#app {
  height: 100%;
}

.app-root {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  flex: 1;
}
</style>

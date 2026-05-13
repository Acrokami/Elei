<script setup lang="ts">
import { onMounted } from 'vue';
import {useRoute, useRouter} from 'vue-router';


const route = useRoute();
const router = useRouter();


onMounted(() => {
    const token = route.query.token as string;
    if(token) {
        localStorage.setItem('user_token', token)
        router.push('/');
    } else {
        console.error('Authorization error. Token not found')
        router.push('/login')
    }
});
</script>


<template>
    <div class="redirect-wrapper">
        <div class="loader-text">Syncing profile...</div>
    </div>
</template>

<style scoped>
.redirect-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #0f172a
}

.loader-text {
    color: #38bdf8;
    font-size: 18px;
    font-weight: 600;
    letter-spacing: 0.05em;
    animation: pulse 1.5s infinite ease-in-out
}

@keyframes pulse {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.5; }
}
</style>

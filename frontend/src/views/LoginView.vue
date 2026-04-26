<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import authService from '../service/auth.service';


const router = useRouter();
const errorMessage = ref('');

const username = ref('');
const password = ref('');


// TODO try .. catch 
const handleLogin = async() => {
    await authService.login(username.value, password.value)
}

</script>





<template>
    <div>
        <input v-model="username" type="text"/>
    </div>

    <div>
        <input v-model="password" type="password"/>
    </div>

    <button
     @click="handleLogin"
     type="submit">
        Login
    </button>
</template>


<style scoped>
</style>

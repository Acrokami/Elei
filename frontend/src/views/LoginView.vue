<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import authService from '../service/auth.service';


const router = useRouter();
const errorMessage = ref('');

const username = ref('');
const password = ref('');



const handleLogin = async() => {
     try {
        await authService.login(username.value, password.value);
        router.push('/')
     } catch (error) {
        errorMessage.value = 'Incorrect login/password or Server is Unavailable'
     }
}

</script>





<template>
 <div class="login-wrapper">
    <div class="login-card">

        <div class="login-header">
            <h2>Sign in</h2>
            <p>Enter your credentials to continue</p>
        </div>

      <div class="input-group">
        <label>Login</label>
        <input v-model="username" type="text" placeholder="Enter your login"/>
      </div>

      <div class="input-group">
        <label>Password</label>
        <input v-model="password" type="password" placeholder="Enter your password"/>
      </div>

      <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>

      <button class="login-btn" @click="handleLogin" type="submit">
        Login
      </button>
    </div>
</div>
</template>


<style scoped>

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.login-wrapper {
    height: 100vh;
    width: 100vw;
    display:flex;
    justify-content: center;
    align-items: center;
    background-color: #0f172a;
}

.login-card {
    width: 100%;
    max-width: 380px;
    display: flex;
    flex-direction: column;
    gap: 24px;
    padding: 40px;
    background: #0f172a;
    border: 1px solid #1e293b;
    border-radius: 16px;
}

.login-header h2 {
    color: #f1f5f9;
    font-size: 22px;
    font-weight: 600;
    margin-bottom:6px;
}

.login-header p {
  color: #cbd5e1;
  font-size: 15px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-group label {
  color: #e2e8f0;
  font-size: 14px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

input {
  width: 100%;
  padding: 11px 14px;
  background: #0f172a;
  border: 1px solid #1e293b;
  border-radius: 8px;
  font-size: 15px;
  color: #f1f5f9;
  outline: none;
  transition: border-color 0.2s;
}

input:focus {
  border-color: #3b82f6;
}

input::placeholder {
  color: #334155;
}

.login-btn {
  width: 100%;
  padding: 12px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.login-btn:hover {
  background-color: #1d4ed8;
}

.error-text {
    color: #f87171;
    font-size: 14px;
    margin: 0;
}


</style>

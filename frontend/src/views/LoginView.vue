<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import authService from '../service/auth.service';


const router = useRouter();
const errorMessage = ref('');

const username = ref('');
const password = ref('');
const apiUrl = import.meta.env.VITE_API_URL;


const handleLogin = async() => {
     try {
        await authService.login(username.value, password.value);
        router.push('/')
     } catch (error) {
        errorMessage.value = 'Incorrect login/password'
     }
}

const handleRegister = () => {
  router.push('/register');
}

</script>





<template>
 <div class="login-wrapper">
    <div class="ambient-glow glow-1"></div>
    <div class="ambient-glow glow-2"></div>

    <div class="login-card glass-panel">
        <div class="login-header">
            <div class="logo-mark">Elei<span>.</span></div>
            <h2>Sign in to your account</h2>
            <p>Enter your credentials to continue</p>
        </div>

        <div class="form-container">
          <div class="input-group">
            <label>Login</label>
            <div class="input-wrapper">
              <input v-model="username" type="text" placeholder="Enter your login"/>
            </div>
          </div>

          <div class="input-group">
            <label>Password</label>
            <div class="input-wrapper">
              <input v-model="password" type="password" placeholder="Enter your password"/>
            </div>
          </div>

          <Transition name="fade">
            <p v-if="errorMessage" class="error-text">
              <span class="error-icon">⚠️</span> {{ errorMessage }}
            </p>
          </Transition>

          <button class="login-btn" @click="handleLogin" type="submit">
            Login
          </button>
        </div>

        <div class="divider">
          <span>or continue with</span>
        </div>

        <a :href="`${apiUrl}/oauth2/authorization/github`" class="github-btn">
          <svg class="github-icon" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0024 12c0-6.63-5.37-12-12-12z"/>
          </svg>
          GitHub
        </a>

        <button class="register-btn" @click="handleRegister">
          Don't have an account? <span>Sign up</span>
        </button>
    </div>
 </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.login-wrapper {
    min-height: 100vh;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #0b1120;
    font-family: 'Inter', sans-serif;
    position: relative;
    overflow: hidden;
}


.ambient-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  z-index: 0;
  opacity: 0.3;
  pointer-events: none;
}
.glow-1 {
  width: 400px;
  height: 400px;
  background: rgba(59, 130, 246, 0.4);
  top: -100px;
  left: -100px;
}
.glow-2 {
  width: 300px;
  height: 300px;
  background: rgba(139, 92, 246, 0.3);
  bottom: -50px;
  right: -50px;
}


.glass-panel {
    background: rgba(30, 41, 59, 0.4);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
    border: 1px solid rgba(255, 255, 255, 0.08);
    box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.login-card {
    width: 100%;
    max-width: 400px;
    display: flex;
    flex-direction: column;
    padding: 40px;
    border-radius: 20px;
    position: relative;
    z-index: 1;
}

.logo-mark {
  font-size: 28px;
  font-weight: 700;
  letter-spacing: 0.05em;
  background: linear-gradient(to right, #ffffff, #94a3b8);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 24px;
  text-align: center;
}
.logo-mark span {
  color: #3b82f6;
  -webkit-text-fill-color: #3b82f6;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-header h2 {
    color: #f8fafc;
    font-size: 22px;
    font-weight: 600;
    margin-bottom: 8px;
    letter-spacing: -0.02em;
}

.login-header p {
  color: #94a3b8;
  font-size: 14px;
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-group label {
  color: #cbd5e1;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.02em;
}

.input-wrapper {
  position: relative;
}

input {
  width: 100%;
  padding: 12px 16px;
  background: rgba(15, 23, 42, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  font-size: 15px;
  color: #f1f5f9;
  outline: none;
  transition: all 0.3s ease;
  font-family: inherit;
}

input:focus {
  border-color: #3b82f6;
  background: rgba(15, 23, 42, 0.8);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

input::placeholder {
  color: #475569;
}

.error-text {
    color: #f87171;
    font-size: 13px;
    margin: -4px 0 0 0;
    display: flex;
    align-items: center;
    gap: 6px;
    background: rgba(248, 113, 113, 0.1);
    padding: 8px 12px;
    border-radius: 8px;
    border: 1px solid rgba(248, 113, 113, 0.2);
}

.login-btn {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);
  margin-top: 8px;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(37, 99, 235, 0.3);
}

.login-btn:active {
  transform: translateY(0);
}

.divider {
  display: flex;
  align-items: center;
  text-align: center;
  color: #64748b;
  font-size: 13px;
  margin: 24px 0;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.divider span {
  padding: 0 12px;
}

.github-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 12px;
  background-color: rgba(255, 255, 255, 0.05);
  color: #f8fafc;
  text-decoration: none;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.3s ease;
}

.github-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.github-icon {
  width: 18px;
  height: 18px;
}

.register-btn {
  width: 100%;
  padding: 12px;
  margin-top: 24px;
  background: transparent;
  color: #94a3b8;
  border: none;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.3s;
}

.register-btn span {
  color: #3b82f6;
  font-weight: 600;
}

.register-btn:hover {
  color: #cbd5e1;
}

.register-btn:hover span {
  color: #60a5fa;
  text-decoration: underline;
}


.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>

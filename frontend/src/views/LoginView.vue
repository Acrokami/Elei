<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import authService from '../service/auth.service';


const router = useRouter();
const route = useRoute();
const errorMessage = ref('');

const username = ref('');
const password = ref('');
const apiUrl = import.meta.env.VITE_API_URL;


onMounted(() => {
  const errorParam = route.query.error;
  if(errorParam === 'provider_conflict') {
    errorMessage.value = 'This email is already linked to another provider. Please use the correct login method.';
    router.replace({ query: {} });
  }
})

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


        <div class="social-login-stack">
          <a :href="`${apiUrl}/oauth2/authorization/github`" class="social-btn">
            <svg class="social-icon" width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0024 12c0-6.63-5.37-12-12-12z"/>
            </svg>
            <span>Continue with GitHub</span>
          </a>

          <a :href="`${apiUrl}/oauth2/authorization/google`" class="social-btn">
            <svg class="social-icon" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
              <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
              <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/>
              <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
            </svg>
            <span>Continue with Google</span>
          </a>
        </div>

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

-login-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  width: 100%
}

.social-login-stack {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
}

.social-btn {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 12px;
  background-color: rgba(15, 23, 42, 0.4);
  color: #f1f5f9;
  text-decoration: none;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.3s ease;
}

.social-btn:hover {
  background-color: rgba(255, 255, 255, 0.08);
  border-color: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.social-icon {
  position: absolute;
  left: 16px;
  width: 20px;
  height: 20px;
  display: flex;
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

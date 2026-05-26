<script setup lang="ts">
import { ref, reactive, type Ref, } from 'vue';
import { useRouter } from 'vue-router';
import authService from '../service/auth.service';



const router = useRouter();
const errorMessage = ref('');

const username = ref('');
const email = ref('');
const password = ref('');

const errors = reactive({
  username: '',
  email: '',
  password: ''
});



const validators = {
  username: (val: string) => {
    if(!val.trim()) return 'Username cannot be empty'
    if(val.length < 3 || val.length > 20) return 'Username must be between 3 and 20 characters'
    return ''
  },
  email: (val: string) => {
    if(!val.trim()) return 'Email cannot be empty'
    if(!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val)) return 'Invalid email format'
    return ''
  },
  password: (val: string) => {
    if(!val.trim()) return 'Password cannot be empty'
    if(val.length < 6) return 'Password must be at least 6 characters long'
    return ''
  }
};

type FieldName = 'username' | 'email' | 'password';
const fieldRefs: Record<FieldName, Ref<string>> = { username, email, password };

const validateField = (field: FieldName) => {
  errors[field] = validators[field](fieldRefs[field].value);
}

const validateAll = () => {
  let isValid = true
  for (const field of Object.keys(validators) as FieldName[]) {
    errors[field] = validators[field](fieldRefs[field].value)
    if (errors[field]) isValid = false
  }
  return isValid
}


const handleRegister = async() => {
     errorMessage.value = '';
     if(!validateAll()) return;
     try {
        await authService.register(username.value, email.value, password.value);
        await authService.login(username.value, password.value)
        router.push('/')
     } catch (error) {
        errorMessage.value = 'Register error'
     }
}


const handleLoginRedirect = () => {
  router.push('/login');
}

</script>

<template>

<div class="signup-wrapper">
    <div class="ambient-glow glow-1"></div>
    <div class="ambient-glow glow-2"></div>

    <div class="signup-card glass-panel">

        <div class="signup-header">
            <div class="logo-mark">Elei<span>.</span></div>
            <h2>Create an account</h2>
            <p>Join the community today</p>
        </div>

      <div class="form-container">
        <div class="input-group">
          <label>Username</label>
          <div class="input-wrapper">
            <input
              v-model="username"
              type="text"
              placeholder="Enter your username"
              :class="{'input-error': errors.username}"
              @blur="validateField('username')"
            />
          </div>
          <Transition name="fade">
            <span v-if="errors.username" class="field-error">
               <span class="error-icon">⚠️</span> {{ errors.username }}
            </span>
          </Transition>
        </div>

        <div class="input-group">
          <label>Email</label>
          <div class="input-wrapper">
            <input
              v-model="email"
              type="email"
              placeholder="Enter your email"
              :class="{'input-error': errors.email}"
              @blur="validateField('email')"
            />
          </div>
          <Transition name="fade">
            <span v-if="errors.email" class="field-error">
              <span class="error-icon">⚠️</span> {{ errors.email }}
            </span>
          </Transition>
        </div>

        <div class="input-group">
          <label>Password</label>
          <div class="input-wrapper">
            <input
              v-model="password"
              type="password"
              placeholder="Create a password"
              :class="{'input-error': errors.password}"
              @blur="validateField('password')"
            />
          </div>
          <Transition name="fade">
            <span v-if="errors.password" class="field-error">
              <span class="error-icon">⚠️</span> {{ errors.password }}
            </span>
          </Transition>
        </div>

        <Transition name="fade">
          <p v-if="errorMessage" class="global-error-text">
            <span class="error-icon">❌</span> {{ errorMessage }}
          </p>
        </Transition>

        <button class="signup-btn" @click="handleRegister" type="submit">
          Sign up
        </button>
      </div>

      <button class="login-redirect-btn" @click="handleLoginRedirect">
        Already have an account? <span>Sign in</span>
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

.signup-wrapper {
    min-height: 100vh;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #0b1120;
    font-family: 'Inter', sans-serif;
    position: relative;
    overflow: hidden;
    padding: 20px 0;
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
  background: rgba(139, 92, 246, 0.3);
  top: -50px;
  right: -100px;
}
.glow-2 {
  width: 300px;
  height: 300px;
  background: rgba(59, 130, 246, 0.4);
  bottom: -50px;
  left: -50px;
}


.glass-panel {
    background: rgba(30, 41, 59, 0.4);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
    border: 1px solid rgba(255, 255, 255, 0.08);
    box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.signup-card {
    width: 100%;
    max-width: 420px;
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
  margin-bottom: 20px;
  text-align: center;
}
.logo-mark span {
  color: #3b82f6;
  -webkit-text-fill-color: #3b82f6;
}

.signup-header {
  text-align: center;
  margin-bottom: 32px;
}

.signup-header h2 {
    color: #f8fafc;
    font-size: 22px;
    font-weight: 600;
    margin-bottom: 8px;
    letter-spacing: -0.02em;
}

.signup-header p {
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
  min-height: 80px;
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


.input-error {
  border-color: #f87171 !important;
  background: rgba(248, 113, 113, 0.05) !important;
}
.input-error:focus {
  box-shadow: 0 0 0 3px rgba(248, 113, 113, 0.2) !important;
}


.field-error {
  color: #fca5a5;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 6px;
  background: rgba(248, 113, 113, 0.1);
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid rgba(248, 113, 113, 0.2);
  margin-top: 4px;
}

.global-error-text {
    color: #f87171;
    font-size: 13px;
    margin: 0;
    display: flex;
    align-items: center;
    gap: 6px;
    background: rgba(248, 113, 113, 0.1);
    padding: 10px 12px;
    border-radius: 8px;
    border: 1px solid rgba(248, 113, 113, 0.2);
}

.signup-btn {
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
  margin-top: 4px;
}

.signup-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(37, 99, 235, 0.3);
}

.signup-btn:active {
  transform: translateY(0);
}

.login-redirect-btn {
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

.login-redirect-btn span {
  color: #3b82f6;
  font-weight: 600;
}

.login-redirect-btn:hover {
  color: #cbd5e1;
}

.login-redirect-btn:hover span {
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
  transform: translateY(-5px);
}
</style>

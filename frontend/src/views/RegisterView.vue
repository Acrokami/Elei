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

</script>





<template>
 <div class="signup-wrapper">
    <div class="signup-card">

        <div class="signup-header">
            <h2>Sign up</h2>
            <p>Create your account</p>
        </div>

      <div class="input-group">
        <label>Username</label>
        <input
         v-model="username"
         type="text"
         placeholder="Enter your username"
         :class="{'input-error': errors.username}"
         @blur="validateField('username')"
         />
         <span v-if="errors.username" class="field-error">{{ errors.username }}</span>
      </div>

      <div class="input input-group">
        <label>Email</label>
        <input
        v-model="email"
        type="email"
        placeholder="Enter your email"
        :class="{'input-error': errors.email}"
        @blur="validateField('email')"
        />
        <span v-if="errors.email" class="field-error">{{ errors.email }}</span>
      </div>

      <div class="input-group">
        <label>Password</label>
        <input
        v-model="password"
        type="password"
        placeholder="Enter your password"
        :class="{'input-error': errors.password}"
        @blur="validateField('password')"
        />
        <span v-if="errors.password" class="field-error">{{ errors.password }}</span>
      </div>

      <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>

      <button class="signup-btn" @click="handleRegister" type="submit">
        Sign up
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

.signup-wrapper {
    height: 100vh;
    width: 100vw;
    display:flex;
    justify-content: center;
    align-items: center;
    background-color: #0f172a;
}

.signup-card {
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

.signup-header h2 {
    color: #f1f5f9;
    font-size: 22px;
    font-weight: 600;
    margin-bottom:6px;
}

.signup-header p {
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

.input-error {
  border-color: #e53e3e !important;
}

.signup-btn {
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

.signup-btn:hover {
  background-color: #1d4ed8;
}

.field-error {
  color: #f87171;
  font-size: 0.78rem;
  margin-top: 4px;
  display: block
}

.error-text {
    color: #f87171;
    font-size: 14px;
    margin: 0;
}


</style>

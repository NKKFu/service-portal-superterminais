<template>
  <div class="login-wrapper">
    <div class="login-card glass-panel">
      <img src="/logo-fullwidth.png" alt="Company Logo" class="login-logo" />
      <h2 class="login-title">Bem-vindo</h2>
      <p class="login-subtitle">Faça login para continuar</p>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="input-group">
          <label for="username">Usuário</label>
          <input 
            type="text" 
            id="username" 
            v-model="form.username" 
            class="input-field" 
            placeholder="Digite seu usuário"
            required
          />
        </div>

        <div class="input-group password-group">
          <label for="password">Senha</label>
          <div class="password-input-wrapper">
            <input 
              :type="showPassword ? 'text' : 'password'" 
              id="password" 
              v-model="form.password" 
              class="input-field" 
              placeholder="Digite sua senha"
              required
            />
            <button type="button" class="eye-btn" @click="showPassword = !showPassword" aria-label="Toggle password visibility">
              <svg v-if="showPassword" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line></svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
            </button>
          </div>
        </div>

        <button type="submit" class="btn btn-primary login-btn" :disabled="loading">
          <span v-if="loading" class="spinner"></span>
          <span v-else>Entrar</span>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuth } from '../composables/useAuth';
import { useToast } from '../composables/useToast';

const router = useRouter();
const { loginSuccess } = useAuth();
const { error } = useToast();

const form = ref({ username: '', password: '' });
const loading = ref(false);
const showPassword = ref(false);

const handleLogin = async () => {
  loading.value = true;
  try {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': '*/*'
      },
      credentials: 'include',
      body: JSON.stringify(form.value)
    });

    if (response.ok) {
      loginSuccess();
      router.push('/');
    } else {
      error('Usuário ou senha inválidos');
    }
  } catch (err) {
    console.error(err);
    error('Erro ao conectar ao servidor');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--color-bg);
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 2.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-logo {
  height: 90px;
  margin-bottom: 1.5rem;
  object-fit: contain;
}

.login-title {
  font-size: 1.5rem;
  color: var(--color-secondary);
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.login-subtitle {
  color: var(--color-text-light);
  font-size: 0.875rem;
  margin-bottom: 2rem;
}

.login-form {
  width: 100%;
}

.login-btn {
  width: 100%;
  margin-top: 1rem;
  height: 48px;
  position: relative;
  overflow: hidden;
  font-size: 1rem;
  letter-spacing: 0.5px;
}

.login-btn:hover {
  box-shadow: 0 4px 25px rgba(0, 161, 155, 0.4);
}

.login-btn:disabled {
  cursor: not-allowed;
  opacity: 0.2;
  animation: pulsePoggers 1.5s infinite;
}

.spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255,255,255,0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s ease-in-out infinite;
}

.password-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.password-input-wrapper .input-field {
  padding-right: 2.5rem;
}

.eye-btn {
  position: absolute;
  right: 0.75rem;
  color: var(--color-text-light);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s;
}

.eye-btn:hover {
  color: var(--color-primary);
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

@keyframes pulsePoggers {
  0% { transform: scale(1); box-shadow: 0 0 0 0 rgba(0, 161, 155, 0.4); }
  50% { transform: scale(0.98); box-shadow: 0 0 0 10px rgba(0, 161, 155, 0); }
  100% { transform: scale(1); box-shadow: 0 0 0 0 rgba(0, 161, 155, 0); }
}
</style>

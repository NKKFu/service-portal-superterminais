<template>
  <div class="app-layout">
    <nav class="navbar glass-panel">
      <a class="nav-brand" href="/">
        <img src="/logo-fullwidth.png" alt="Company Logo" class="logo" />
      </a>
      <div class="nav-user">
        <span class="username">{{ username }}</span>
        <div class="user-menu-wrapper" ref="menuRef">
          <div class="profile-pic" @click="toggleTooltip" title="Menu de usuário">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
          </div>

          <div v-show="isTooltipOpen" class="user-tooltip glass-panel animate-fade-in">
            <button class="logout-btn" @click="handleLogout">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
              Sair
            </button>
          </div>
        </div>
      </div>
    </nav>
    <div class="layout-body">
      <aside :class="['sidebar', { 'sidebar-open': isSidebarOpen }]">
        <ul class="sidebar-menu">
          <li>
            <router-link to="/" class="menu-link" exact-active-class="active">Início</router-link>
          </li>
          <li v-if="isInternal">
            <router-link to="/companies" class="menu-link" exact-active-class="active">Empresas Cadastradas</router-link>
          </li>
          <li>
            <router-link to="/companies/register" class="menu-link" exact-active-class="active">Registrar Empresa</router-link>
          </li>
        </ul>
      </aside>
      <main class="main-content">
        <router-view class="animate-fade-in" />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuth } from '../composables/useAuth';

const router = useRouter();
const { username, isInternal, logout } = useAuth();
const isSidebarOpen = ref(true);
const isTooltipOpen = ref(false);

const toggleTooltip = (event: Event) => {
  event.stopPropagation();
  isTooltipOpen.value = !isTooltipOpen.value;
};

const menuRef = ref<HTMLElement | null>(null);

const handleOutsideClick = (e: MouseEvent) => {
  if (isTooltipOpen.value && menuRef.value && !menuRef.value.contains(e.target as Node)) {
    isTooltipOpen.value = false;
  }
};

onMounted(() => {
  window.addEventListener('click', handleOutsideClick);
});

onUnmounted(() => {
  window.removeEventListener('click', handleOutsideClick);
});

const handleLogout = () => {
  logout();
  router.push('/login');
};
</script>

<style scoped>
.app-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  z-index: 100;
  border-radius: 0;
  border-top: none;
  border-left: none;
  border-right: none;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.logo {
  height: 40px;
  object-fit: contain;
}

.hamburger-btn {
  color: var(--color-secondary);
  padding: 0.5rem;
  border-radius: var(--radius-sm);
}

.hamburger-btn:hover {
  background-color: var(--color-bg);
}

.nav-user {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.username {
  font-weight: 500;
  color: var(--color-text);
}

.profile-pic {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 40px;
  background-color: var(--color-primary);
  color: white;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 10px rgba(0, 161, 155, 0.3);
}

.profile-pic:hover {
  transform: scale(1.05);
  background-color: var(--color-primary-dark);
}

.user-menu-wrapper {
  position: relative;
}

.tooltip-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 100;
  background: transparent;
}

.user-tooltip {
  position: absolute;
  top: 120%;
  right: 0;
  min-width: 150px;
  padding: 0.5rem;
  z-index: 101;
}

.logout-btn {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  color: var(--color-error);
  font-weight: 500;
  border-radius: var(--radius-sm);
  transition: background-color 0.2s;
}

.logout-btn:hover {
  background-color: rgba(239, 68, 68, 0.1);
}

.layout-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.sidebar {
  width: 260px;
  background-color: var(--color-surface);
  border-right: 1px solid var(--color-border);
  transition: transform 0.3s ease;
  position: relative;
}

.sidebar-header {
  padding: 1.5rem;
  border-bottom: 1px solid var(--color-border);
  color: var(--color-secondary);
  font-weight: 600;
}

.sidebar-menu {
  list-style: none;
  padding: 1rem 0;
}

.menu-link {
  display: block;
  padding: 0.75rem 1.5rem;
  color: var(--color-text);
  font-weight: 500;
  transition: all 0.2s;
  border-left: 3px solid transparent;
}

.menu-link:hover, .menu-link.active {
  background-color: rgba(0, 161, 155, 0.05);
  color: var(--color-primary);
  border-left-color: var(--color-primary);
}

.main-content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
  background-color: var(--color-bg);
}
</style>

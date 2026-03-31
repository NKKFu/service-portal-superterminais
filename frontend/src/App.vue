<template>
  <router-view></router-view>

  <div class="toast-container">
    <div 
      v-for="toast in toasts" 
      :key="toast.id" 
      :class="['toast', toast.type]"
    >
      <div class="toast-body">
        <strong v-if="toast.title" class="toast-title">{{ toast.title }}</strong>
        <span class="toast-message">{{ toast.message }}</span>
      </div>
      <button @click="removeToast(toast.id)" style="margin-left: auto; flex-shrink: 0; color: var(--color-text-light);">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useToast } from './composables/useToast';
const { toasts, removeToast } = useToast();
</script>

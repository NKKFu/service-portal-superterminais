import { ref } from 'vue';

export type ToastType = 'success' | 'error' | 'info' | 'warning';

export interface Toast {
  id: number;
  message: string;
  type: ToastType;
  title?: string;
}

const toasts = ref<Toast[]>([]);
let nextId = 0;

export function useToast() {
  const showToast = (message: string, type: ToastType = 'info', title?: string, duration = 4000) => {
    const id = nextId++;
    toasts.value.push({ id, message, type, title });
    
    setTimeout(() => {
      removeToast(id);
    }, duration);
  };

  const removeToast = (id: number) => {
    const index = toasts.value.findIndex(t => t.id === id);
    if (index > -1) {
      toasts.value.splice(index, 1);
    }
  };

  const success = (message: string, title?: string) => showToast(message, 'success', title);
  const error = (message: string, title?: string) => showToast(message, 'error', title);
  const warning = (message: string, title?: string) => showToast(message, 'warning', title);

  return {
    toasts,
    showToast,
    removeToast,
    success,
    error,
    warning
  };
}

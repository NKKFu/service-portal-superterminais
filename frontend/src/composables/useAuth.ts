import { ref, computed } from 'vue';
import { jwtDecode } from 'jwt-decode';

interface UserPayload {
  sub: string;
  internal: boolean;
  exp: number;
}

const isAuth = ref(false);
const username = ref('');
const isInternal = ref(false);

const checkCookieAndRestoreToken = () => {
  const match = document.cookie.match(/(?:^|; )st-service-token=([^;]*)/);
  if (match) {
    const token = match[1];
    try {
      const decoded = jwtDecode<UserPayload>(token);
      if (decoded.exp * 1000 > Date.now()) {
        isAuth.value = true;
        username.value = decoded.sub || 'User';
        isInternal.value = decoded.internal || false;
        return true;
      }
    } catch(e) {
      console.error('Invalid token', e);
    }
  }
  return false;
};

export function useAuth() {
  const logout = () => {
    document.cookie = 'st-service-token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    isAuth.value = false;
    username.value = '';
    isInternal.value = false;
  };

  return {
    isAuth: computed(() => isAuth.value),
    username: computed(() => username.value),
    isInternal: computed(() => isInternal.value),
    checkCookieAndRestoreToken,
    loginSuccess: checkCookieAndRestoreToken,
    logout
  };
}

import { createRouter, createWebHistory } from 'vue-router';
import { useAuth } from '../composables/useAuth';

import MainLayout from '../layouts/MainLayout.vue';
import LoginView from '../views/LoginView.vue';
import HomeView from '../views/HomeView.vue';
import CompaniesList from '../views/CompaniesList.vue';
import RegisterCompany from '../views/RegisterCompany.vue';
import CompanyDetails from '../views/CompanyDetails.vue';
import EditCompany from '../views/EditCompany.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/',
      component: MainLayout,
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'home',
          component: HomeView,
        },
        {
          path: 'companies',
          name: 'companies',
          component: CompaniesList,
        },
        {
          path: 'companies/register',
          name: 'register-company',
          component: RegisterCompany,
        },
        {
          path: 'companies/:id',
          name: 'company-details',
          component: CompanyDetails,
        },
        {
          path: 'companies/:id/edit',
          name: 'edit-company',
          component: EditCompany,
        }
      ]
    }
  ],
});

router.beforeEach((to, _from, next) => {
  const { checkCookieAndRestoreToken } = useAuth();

  const hasToken = checkCookieAndRestoreToken();

  if (to.meta.requiresAuth && !hasToken) {
    next({ name: 'login' });
  } else if (to.name === 'login' && hasToken) {
    next({ name: 'home' });
  } else {
    next();
  }
});

export default router;

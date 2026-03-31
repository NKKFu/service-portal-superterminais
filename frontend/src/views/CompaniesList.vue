<template>
  <div class="page-container glass-panel">
    <div class="page-header">
      <h2>Empresas Cadastradas</h2>
      <button @click="router.push('/companies/register')" class="btn btn-primary">
        + Nova Empresa
      </button>
    </div>

    <div v-if="loading" class="state-container">
      Carregando...
    </div>

    <div v-else-if="errorMsg" class="state-container error-text">
      {{ errorMsg }}
    </div>

    <div v-else-if="companies.length === 0" class="state-container">
      Nenhuma empresa encontrada.
    </div>

    <div v-else class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Tipo</th>
            <th>Documento</th>
            <th>Perfil</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr 
            v-for="company in companies" 
            :key="company.id"
            class="row-item"
          >
            <td>
              <router-link :to="`/companies/${company.id}`" class="cell-link">{{ company.id }}</router-link>
            </td>
            <td class="primary-text">
               <router-link :to="`/companies/${company.id}`" class="cell-link">{{ company.name }}</router-link>
            </td>
            <td>
               <router-link :to="`/companies/${company.id}`" class="cell-link">{{ formatCompanyType(company.type) }}</router-link>
            </td>
            <td>
               <router-link :to="`/companies/${company.id}`" class="cell-link">
                 <span v-if="company.type === 'PESSOA_JURIDICA'">{{ company.cnpj || '-' }}</span>
                 <span v-else-if="company.type === 'PESSOA_FISICA'">{{ company.cpf || '-' }}</span>
                 <span v-else>{{ company.foreignDocument || '-' }}</span>
               </router-link>
            </td>
            <td>
               <router-link :to="`/companies/${company.id}`" class="cell-link">{{ formatProfileType(company.profileType) }}</router-link>
            </td>
            <td>
              <router-link :to="`/companies/${company.id}`" class="cell-link">
                <span :class="['bagde', company.approvedBy ? 'badge-success' : 'badge-warning']">
                  {{ company.approvedBy ? 'Aprovado' : 'Pendente' }}
                </span>
              </router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useToast } from '../composables/useToast';
import { formatCompanyType, formatProfileType } from '../utils/formatters';

const router = useRouter();
const { error } = useToast();

const companies = ref<any[]>([]);
const loading = ref(true);
const errorMsg = ref('');

const fetchCompanies = async () => {
  loading.value = true;
  errorMsg.value = '';
  try {
    const response = await fetch('/api/companies', {
      headers: { 'Accept': '*/*' },
      credentials: 'include'
    });
    
    if (response.ok) {
      const rawData = await response.json();
      companies.value = rawData.sort((a: any, b: any) => {
        const idA = Number(a.id) || 0;
        const idB = Number(b.id) || 0;
        return idB - idA;
      });
    } else {
      errorMsg.value = 'Failed to load companies.';
      error('Failed to load companies.');
    }
  } catch (err) {
    console.error(err);
    errorMsg.value = 'Server connection error.';
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchCompanies();
});
</script>

<style scoped>
.page-container {
  padding: 2rem;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  border-bottom: 2px solid var(--color-border);
  padding-bottom: 1rem;
}

.page-header h2 {
  color: var(--color-secondary);
  font-weight: 700;
}

.state-container {
  padding: 3rem;
  text-align: center;
  color: var(--color-text-light);
  font-weight: 500;
}

.error-text {
  color: var(--color-error);
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}

.data-table th, .data-table td {
  border-bottom: 1px solid var(--color-border);
}

.data-table th {
  padding: 1rem;
  font-weight: 600;
  color: var(--color-text-light);
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.row-item {
  transition: background-color 0.2s;
}

.row-item:hover {
  background-color: var(--color-bg);
}

.cell-link {
  display: block;
  padding: 1rem;
  text-decoration: none;
  color: inherit;
  width: 100%;
  height: 100%;
}

.primary-text {
  color: var(--color-primary);
  font-weight: 500;
}

.bagde {
  padding: 0.25rem 0.625rem;
  border-radius: 999px;
  font-size: 0.75rem;
  font-weight: 600;
}

.badge-success {
  background-color: rgba(16, 185, 129, 0.1);
  color: var(--color-success);
}

.badge-warning {
  background-color: rgba(245, 158, 11, 0.1);
  color: #d97706;
}
</style>

<template>
  <div class="page-container glass-panel">
    <div class="page-header">
      <div class="header-left">
        <h2>Detalhes da Empresa</h2>
        <span class="company-id">#{{ company?.id }}</span>
      </div>
      <div class="header-actions">
        <button @click="router.push(`/companies/${company?.id}/edit`)" class="btn btn-secondary-outline" v-if="company">
          Editar
        </button>
        <button @click="router.push('/companies')" class="btn btn-secondary-outline">
          Voltar
        </button>
      </div>
    </div>

    <div v-if="loading" class="state-container">
      Carregando...
    </div>
    
    <div v-else-if="!company" class="state-container error-text">
      Empresa não encontrada.
    </div>

    <div v-else class="details-content">
      <div class="info-group main-info">
        <h3>{{ company.name }}</h3>
        <span v-if="company.nickname" class="nickname">{{ company.nickname }}</span>
        
        <div class="flex-center-gap">
          <div v-if="company.approvedBy" class="badge-status approved">
            {{ `Aprovado por: ${company.approvedBy}` }}
          </div>
          <div v-else-if="isInternal && company.rejectedBy" class="badge-status rejected">
            {{ `Rejeitado por: ${company.rejectedBy}` }}
          </div>
          <div v-else class="badge-status pending">
            Aprovação Pendente
          </div>

          <template v-if="isInternal && !company.approvedBy && !company.rejectedBy">
            <button @click="handleApprove" class="btn btn-primary btn-sm" :disabled="approving || rejecting">
              {{ approving ? 'Aprovando...' : 'Aprovar' }}
            </button>
            <button @click="handleReject" class="btn btn-danger btn-sm" :disabled="approving || rejecting">
              {{ rejecting ? 'Rejeitando...' : 'Rejeitar' }}
            </button>
          </template>
        </div>
      </div>
      
      <div class="grid-details">
        <div class="detail-item">
          <span class="detail-label">Tipo</span>
          <span class="detail-value">{{ formatCompanyType(company.type) }}</span>
        </div>
        
        <div class="detail-item">
          <span class="detail-label">Documento</span>
          <span class="detail-value" v-if="company.type === 'PESSOA_JURIDICA'">CNPJ: {{ company.cnpj }}</span>
          <span class="detail-value" v-else-if="company.type === 'PESSOA_FISICA'">CPF: {{ company.cpf }}</span>
          <span class="detail-value" v-else>Estrangeiro: {{ company.foreignDocument }}</span>
        </div>

        <div class="detail-item">
          <span class="detail-label">Tipo de Perfil</span>
          <span class="detail-value">{{ formatProfileType(company.profileType) }}</span>
        </div>

        <div class="detail-item" v-if="company.companyIncome">
          <span class="detail-label">Renda da Empresa</span>
          <span class="detail-value">{{ formatCurrency(company.companyIncome) }}</span>
        </div>

        <div class="detail-item">
          <span class="detail-label">Documento</span>
          <a
            v-if="company.hasDocument"
            :href="`/api/companies/${company.id}/document`"
            target="_blank"
            rel="noopener noreferrer"
            class="detail-link document-link"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right: 6px; flex-shrink: 0">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline>
            </svg>
            {{ company.documentFileName || 'Ver Documento' }}
          </a>
          <span v-else class="detail-value" style="color: var(--color-text-light);">Nenhum documento anexado</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useToast } from '../composables/useToast';
import { useAuth } from '../composables/useAuth';
import { formatCompanyType, formatProfileType, formatCurrency } from '../utils/formatters';

const route = useRoute();
const router = useRouter();
const { error } = useToast();
const { isInternal } = useAuth();

const loading = ref(true);
const approving = ref(false);
const rejecting = ref(false);
const company = ref<any>(null);

const fetchCompanyDetails = async () => {
  const idValue = route.params.id;
  loading.value = true;
  
  try {
    const response = await fetch('/api/companies', {
      headers: { 'Accept': '*/*' },
      credentials: 'include'
    });
    
    if (response.ok) {
      const allCompanies = await response.json();
      company.value = allCompanies.find((c: any) => c.id == idValue) || null;
      if (!company.value) {
        error('Empresa não encontrada.');
      }
    } else {
      error('Falha ao buscar detalhes.');
    }
  } catch(err) {
    console.error(err);
    error('Erro ao conectar ao servidor.');
  } finally {
    loading.value = false;
  }
};

const handleApprove = async () => {
  approving.value = true;
  try {
    const response = await fetch(`/api/companies/${company.value.id}/approve`, {
      method: 'POST',
      headers: { 'Accept': '*/*' },
      credentials: 'include',
      body: ''
    });
    if (response.ok) {
      // Re-fetch to update the UI
      await fetchCompanyDetails();
    } else {
      error('Falha ao aprovar a empresa.');
    }
  } catch (err) {
    console.error(err);
    error('Erro ao conectar ao servidor.');
  } finally {
    approving.value = false;
  }
};

const handleReject = async () => {
  rejecting.value = true;
  try {
    const response = await fetch(`/api/companies/${company.value.id}/reject`, {
      method: 'POST',
      headers: { 'Accept': '*/*' },
      credentials: 'include'
    });
    if (response.ok) {
      await fetchCompanyDetails();
    } else {
      error('Falha ao rejeitar a empresa.');
    }
  } catch (err) {
    console.error(err);
    error('Erro ao conectar ao servidor.');
  } finally {
    rejecting.value = false;
  }
};

onMounted(() => {
  fetchCompanyDetails();
});
</script>

<style scoped>
.page-container {
  padding: 2rem;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2px solid var(--color-border);
  padding-bottom: 1.5rem;
  margin-bottom: 2rem;
}

.header-left {
  display: flex;
  align-items: baseline;
  gap: 1rem;
}

.header-left h2 {
  color: var(--color-secondary);
  font-weight: 700;
  margin: 0;
}

.company-id {
  color: var(--color-text-light);
  font-size: 1.125rem;
  font-weight: 500;
}

.header-actions {
  display: flex;
  gap: 1rem;
}

.btn-secondary-outline {
  padding: 0.5rem 1rem;
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
  background: white;
  color: var(--color-text);
  font-weight: 500;
  transition: all 0.2s;
}

.flex-center-gap {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.btn-sm {
  padding: 0.375rem 0.75rem;
  font-size: 0.875rem;
  height: auto;
}

.btn-secondary-outline:hover {
  background: var(--color-bg);
  border-color: var(--color-text-light);
}

.state-container {
  padding: 4rem 0;
  text-align: center;
  color: var(--color-text-light);
  font-weight: 500;
  font-size: 1.125rem;
}

.error-text {
  color: var(--color-error);
}

.main-info {
  margin-bottom: 2.5rem;
}

.main-info h3 {
  font-size: 2rem;
  color: var(--color-primary);
  margin-bottom: 0.25rem;
}

.nickname {
  display: block;
  font-size: 1.125rem;
  color: var(--color-text-light);
  margin-bottom: 1rem;
}

.badge-status {
  display: inline-block;
  padding: 0.375rem 0.875rem;
  border-radius: 999px;
  font-size: 0.875rem;
  font-weight: 600;
}

.badge-status.approved {
  background-color: rgba(16, 185, 129, 0.1);
  color: var(--color-success);
}

.badge-status.rejected {
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--color-error);
}

.badge-status.pending {
  background-color: rgba(245, 158, 11, 0.1);
  color: #d97706;
}

.btn-danger {
  background-color: var(--color-error);
  color: white;
}

.btn-danger:hover:not(:disabled) {
  opacity: 0.9;
}

.grid-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 2rem;
  background-color: var(--color-bg);
  padding: 2rem;
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border);
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.detail-label {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--color-secondary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.detail-value {
  font-size: 1.125rem;
  color: var(--color-text);
  font-weight: 500;
}

.detail-link {
  font-size: 1rem;
  color: var(--color-primary);
  font-weight: 500;
  display: inline-flex;
  align-items: center;
}

.document-link {
  font-size: 0.9375rem;
  border-radius: var(--radius-sm);
  transition: color 0.2s ease;
}

.document-link:hover {
  color: var(--color-primary-dark);
  text-decoration: underline;
}
</style>

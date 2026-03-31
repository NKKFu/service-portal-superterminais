<template>
  <div class="page-container glass-panel">
    <div class="page-header">
      <h2>Registrar Empresa</h2>
    </div>

    <form @submit.prevent="handleSubmit" class="registration-form">
      <div class="form-grid">
        <div class="input-group">
          <label for="name">Nome da Empresa</label>
          <input type="text" id="name" v-model="form.name" class="input-field" required />
        </div>
        
        <div class="input-group">
          <label for="nickname">Nome Fantasia / Apelido</label>
          <input type="text" id="nickname" v-model="form.nickname" class="input-field" />
        </div>

        <div class="input-group">
          <label for="type">Tipo de Pessoa</label>
          <select id="type" v-model="form.type" class="input-field" required>
            <option value="PESSOA_JURIDICA">Pessoa Jurídica</option>
            <option value="PESSOA_FISICA">Pessoa Física</option>
            <option value="PESSOA_ESTRANGEIRA">Pessoa Estrangeira</option>
          </select>
        </div>

        <div class="input-group" v-if="form.type === 'PESSOA_JURIDICA'">
          <label for="cnpj">CNPJ</label>
          <input 
            type="text" 
            id="cnpj" 
            v-model="form.cnpj" 
            class="input-field" 
            :class="{ 'input-error': cnpjError }" 
            placeholder="00.000.000/0001-00"
            @input="handleCnpjInput"
          />
          <span v-if="cnpjError" class="error-msg">{{ cnpjError }}</span>
        </div>

        <div class="input-group" v-if="form.type === 'PESSOA_FISICA'">
          <label for="cpf">CPF</label>
          <input 
            type="text" 
            id="cpf" 
            v-model="form.cpf" 
            class="input-field" 
            :class="{ 'input-error': cpfError }" 
            placeholder="000.000.000-00"
            @input="handleCpfInput"
          />
          <span v-if="cpfError" class="error-msg">{{ cpfError }}</span>
        </div>

        <div class="input-group" v-if="form.type === 'PESSOA_ESTRANGEIRA'">
          <label for="foreignDocument">Documento Estrangeiro</label>
          <input type="text" id="foreignDocument" v-model="form.foreignDocument" class="input-field" />
        </div>

        <div class="input-group">
          <label for="profileType">Tipo de Perfil</label>
          <select id="profileType" v-model="form.profileType" class="input-field" required>
            <option value="DISPATCHER">Despachante</option>
            <option value="BENEFICIARY">Beneficiário</option>
            <option value="CONSIGNEE">Consignatário</option>
            <option value="SHIPOWNER">Armador</option>
            <option value="FREIGHT_FORWARDER">Agente de Carga Transportadora</option>
            <option value="NEW_USER">Novo Usuário</option>
          </select>
        </div>

        <div class="input-group">
          <label for="companyIncome">Renda da Empresa</label>
          <div class="currency-input-wrapper">
            <span class="currency-prefix">R$</span>
            <input type="number" id="companyIncome" v-model.number="form.companyIncome" class="input-field has-prefix" step="0.01" />
          </div>
        </div>
      </div>

      <div class="file-upload-section">
        <label class="file-upload-label">Documento da Empresa <span class="required-mark">*</span></label>
        <div
          class="file-drop-zone"
          :class="{ 'has-file': selectedFile }"
          @click="triggerFileInput"
          @dragover.prevent
          @drop.prevent="handleFileDrop"
        >
          <input
            ref="fileInputRef"
            type="file"
            id="companyFile"
            accept=".pdf,.png,.jpg,.jpeg"
            style="display: none"
            @change="handleFileChange"
          />
          <div v-if="!selectedFile" class="drop-hint">
            <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="12" y1="12" x2="12" y2="18"></line><line x1="9" y1="15" x2="15" y2="15"></line>
            </svg>
            <p>Clique ou arraste um arquivo aqui</p>
            <small>PDF, PNG, JPG ou JPEG (máx. 10MB)</small>
          </div>
          <div v-else class="file-preview">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline>
            </svg>
            <div class="file-info">
              <span class="file-name">{{ selectedFile.name }}</span>
              <span class="file-size">{{ formatFileSize(selectedFile.size) }}</span>
            </div>
            <button type="button" class="remove-file-btn" @click.stop="clearFile">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
            </button>
          </div>
        </div>
      </div>
      
      <div class="form-actions">
        <button type="button" class="btn btn-secondary-outline" @click="handleCancel">
          Cancelar
        </button>
        <button type="submit" class="btn btn-primary" :disabled="loading || !!cpfError || !!cnpjError">
          <span v-if="loading">Salvando...</span>
          <span v-else>Salvar Empresa</span>
        </button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useAuth } from '../composables/useAuth';
import { useToast } from '../composables/useToast';
import { validateCPF, validateCNPJ, maskCPF, maskCNPJ } from '../utils/validators';

const router = useRouter();
const { isInternal } = useAuth();
const { success, error, warning } = useToast();

const loading = ref(false);
const selectedFile = ref<File | null>(null);
const fileInputRef = ref<HTMLInputElement | null>(null);

const ALLOWED_EXTENSIONS = ['pdf', 'png', 'jpg', 'jpeg'];

const cnpjError = ref('');
const cpfError = ref('');

const form = ref({
  name: '',
  type: 'PESSOA_JURIDICA',
  cnpj: '',
  cpf: '',
  foreignDocument: '',
  nickname: '',
  profileType: 'NEW_USER',
  companyIncome: 0
});

const formatFileSize = (bytes: number) => {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
};

const triggerFileInput = () => fileInputRef.value?.click();

const validateAndSetFile = (file: File) => {
  const ext = file.name.split('.').pop()?.toLowerCase() ?? '';
  if (!ALLOWED_EXTENSIONS.includes(ext)) {
    error('São válidos somente arquivos do tipo: pdf, png, jpg ou jpeg.', 'Arquivo Inválido');
    return;
  }
  selectedFile.value = file;
};

const handleFileChange = (e: Event) => {
  const target = e.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    validateAndSetFile(target.files[0]);
  }
};

const handleFileDrop = (e: DragEvent) => {
  if (e.dataTransfer?.files[0]) {
    validateAndSetFile(e.dataTransfer.files[0]);
  }
};

const clearFile = () => {
  selectedFile.value = null;
  if (fileInputRef.value) fileInputRef.value.value = '';
};

const handleCancel = () => {
  if (window.history.length > 1) {
    router.back();
  } else {
    router.push(isInternal.value ? '/companies' : '/');
  }
};

const handleCnpjInput = () => {
  form.value.cnpj = maskCNPJ(form.value.cnpj);
  if (form.value.cnpj.length === 18) {
    if (!validateCNPJ(form.value.cnpj)) {
      cnpjError.value = 'Dígito verificador do CNPJ inválido';
    } else {
      cnpjError.value = '';
    }
  } else if (form.value.cnpj.length > 0) {
    cnpjError.value = 'CNPJ incompleto';
  } else {
    cnpjError.value = '';
  }
};

const handleCpfInput = () => {
  form.value.cpf = maskCPF(form.value.cpf);
  if (form.value.cpf.length === 14) {
    if (!validateCPF(form.value.cpf)) {
      cpfError.value = 'Dígito verificador do CPF inválido';
    } else {
      cpfError.value = '';
    }
  } else if (form.value.cpf.length > 0) {
    cpfError.value = 'CPF incompleto';
  } else {
    cpfError.value = '';
  }
};

watch(() => form.value.type, (newType) => {
  if (newType !== 'PESSOA_JURIDICA') { cnpjError.value = ''; form.value.cnpj = ''; }
  if (newType !== 'PESSOA_FISICA') { cpfError.value = ''; form.value.cpf = ''; }
});

const handleSubmit = async () => {
  if (form.value.type === 'PESSOA_JURIDICA' && !form.value.cnpj) {
    cnpjError.value = 'CNPJ é obrigatório';
    return;
  }
  if (form.value.type === 'PESSOA_FISICA' && !form.value.cpf) {
    cpfError.value = 'CPF é obrigatório';
    return;
  }
  if (cnpjError.value || cpfError.value) return;

  if (!selectedFile.value) {
    warning('É necessário enviar os arquivos obrigatórios para prosseguir.', 'Aviso');
    return;
  }

  loading.value = true;
  try {
    const payload: Record<string, any> = { ...form.value };

    if (!payload.cnpj) delete payload.cnpj;
    if (!payload.cpf) delete payload.cpf;
    if (!payload.foreignDocument) delete payload.foreignDocument;
    if (!payload.nickname) delete payload.nickname;
    if (!payload.companyIncome) payload.companyIncome = 0;

    const formData = new FormData();
    formData.append('data', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
    formData.append('file', selectedFile.value);

    const response = await fetch('/api/companies', {
      method: 'POST',
      headers: { 'Accept': '*/*' },
      credentials: 'include',
      body: formData
    });

    if (response.ok) {
      const data = await response.json();
      success('Empresa cadastrada com sucesso');
      if (isInternal.value) {
        if (data && data.id) {
          router.push(`/companies/${data.id}`);
        } else {
          router.push('/companies');
        }
      } else {
        router.push('/');
      }
    } else {
      const result = await response.json();
      const msg = result.message || 'Erro ao cadastrar empresa.';
      if (msg === 'Arquivo duplicado') {
        error(msg, 'Arquivo duplicado');
      } else {
        error(msg);
      }
    }
  } catch (err) {
    console.error(err);
    error('Falha de conexão com o servidor.');
  } finally {
    loading.value = false;
  }
};
</script>


<style scoped>
.page-container {
  padding: 2rem;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 2rem;
  border-bottom: 2px solid var(--color-border);
  padding-bottom: 1rem;
}

.page-header h2 {
  color: var(--color-secondary);
  font-weight: 700;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.input-group {
  margin-bottom: 0;
  position: relative;
}

.currency-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.currency-prefix {
  position: absolute;
  left: 1rem;
  color: var(--color-text-light);
  font-weight: 500;
  pointer-events: none;
}

.input-field.has-prefix {
  padding-left: 2.5rem;
}

.input-error {
  border-color: var(--color-error) !important;
  background-color: rgba(239, 68, 68, 0.02) !important;
}

.input-error:focus {
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1) !important;
}

.error-msg {
  display: block;
  font-size: 0.75rem;
  color: var(--color-error);
  margin-top: 0.25rem;
  font-weight: 500;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 3rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--color-border);
}

.btn-secondary-outline {
  background: transparent;
  color: var(--color-text-light);
  border: 1px solid var(--color-border);
}

.btn-secondary-outline:hover {
  background: var(--color-bg);
  border-color: var(--color-text-light);
}

.input-group:nth-child(1),
.input-group:nth-child(2) {
  grid-column: span 2;
}

.file-upload-section {
  margin-top: 1.5rem;
}

.file-upload-label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--color-secondary);
  margin-bottom: 0.5rem;
}

.required-mark {
  color: var(--color-error);
}

.file-drop-zone {
  border: 2px dashed var(--color-border);
  border-radius: var(--radius-lg);
  padding: 2rem;
  cursor: pointer;
  transition: all 0.2s ease;
  background: var(--color-surface);
}

.file-drop-zone:hover {
  border-color: var(--color-primary);
  background: rgba(0, 161, 155, 0.02);
}

.file-drop-zone.has-file {
  border-style: solid;
  border-color: var(--color-primary);
  background: rgba(0, 161, 155, 0.03);
}

.drop-hint {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: var(--color-text-light);
  text-align: center;
}

.drop-hint p {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--color-text);
}

.drop-hint small {
  font-size: 0.75rem;
}

.file-preview {
  display: flex;
  align-items: center;
  gap: 1rem;
  color: var(--color-primary);
}

.file-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  flex: 1;
}

.file-name {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--color-text);
}

.file-size {
  font-size: 0.75rem;
  color: var(--color-text-light);
}

.remove-file-btn {
  padding: 0.375rem;
  border-radius: var(--radius-sm);
  color: var(--color-text-light);
  display: flex;
  align-items: center;
}

.remove-file-btn:hover {
  background: rgba(239, 68, 68, 0.1);
  color: var(--color-error);
}
</style>

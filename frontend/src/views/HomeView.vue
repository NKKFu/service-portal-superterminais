<template>
  <div class="welcome-container glass-panel">
    <div class="welcome-content">
      <h1 class="welcome-title">Bem-vindo</h1>
      <p class="welcome-description">
        Nosso sistema foi desenvolvido para facilitar a gestão de empresas.
        Navegue pelo menu lateral para acessar as funcionalidades disponíveis.
      </p>

      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon bg-primary-light">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path><polyline points="9 22 9 12 15 12 15 22"></polyline></svg>
          </div>
          <h3>Gestão de Empresas</h3>
          <p>Administre suas empresas em um painel intuitivo e moderno.</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon bg-secondary-light">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path></svg>
          </div>
          <h3>Segurança Garantida</h3>
          <p>Seus dados estão protegidos com o que há de mais moderno.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.welcome-container {
  padding: 2rem;
  margin: 0 auto;
}

.welcome-title {
  color: var(--color-primary);
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
}

.welcome-description {
  color: var(--color-text);
  font-size: 1.125rem;
  line-height: 1.6;
  margin-bottom: 3rem;
  max-width: 800px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.feature-card {
  padding: 1.5rem;
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border);
  background-color: var(--color-surface);
  transition: transform 0.2s, box-shadow 0.2s;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.feature-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
  color: white;
}

.bg-primary-light {
  background-color: var(--color-primary);
}

.bg-secondary-light {
  background-color: var(--color-secondary);
}

.feature-card h3 {
  font-size: 1.25rem;
  margin-bottom: 0.5rem;
  color: var(--color-text);
}

.feature-card p {
  color: var(--color-text-light);
  font-size: 0.875rem;
}
</style>

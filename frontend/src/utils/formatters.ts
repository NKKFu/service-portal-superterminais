export const formatProfileType = (val: string): string => {
  const profileMap: Record<string, string> = {
    'DISPATCHER': 'Despachante',
    'BENEFICIARY': 'Beneficiário',
    'CONSIGNEE': 'Consignatário',
    'SHIPOWNER': 'Armador',
    'FREIGHT_FORWARDER': 'Agente de Carga Transportadora',
    'NEW_USER': 'Novo Usuário'
  };
  return profileMap[val] || (val ? val.replace(/_/g, ' ') : '-');
};

export const formatCompanyType = (val: string): string => {
  const map: Record<string, string> = {
    'PESSOA_JURIDICA': 'Pessoa Jurídica',
    'PESSOA_FISICA': 'Pessoa Física',
    'PESSOA_ESTRANGEIRA': 'Pessoa Estrangeira'
  };
  return map[val] || val || '-';
};

export const formatCurrency = (val: number | null | undefined): string => {
  if (val === undefined || val === null) return 'R$ 0,00';
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val);
};

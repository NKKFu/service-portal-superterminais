## Portal de serviço ST [Cadastro de Empresa]

### Introdução

Este requisito descreve como o usuário externo realizará os procedimentos para executar o cadastro de empresa.

### Pré-condições

- O ator deve estar logado com o certificado digital para prosseguir com esta ação.

### Pós-condições

- Ter cadastro de empresa jurídica finalizado.

- Ter cadastro de empresa pessoa física finalizado.

- Ter cadastro de empresa pessoa estrangeira finalizado.

### Ator(es)

| Ator | Perfil | Tipo | Permissão(ões) |
| --- | --- | --- | --- |
| Usuário Externo | Despachante<br>Beneficiário<br>Consignatário<br>Armador<br>Agente de Carga Transportadora<br>Novo usuário | Externo | EMPRESA_CADASTRO |
| Usuário Interno |  | Interno | EMPRESA_MASTER<br>EMPRESA_LISTA<br>EMPRESA_EDICAO |

### Fluxo Principal – Pessoa Jurídica - Usuário Externo

Este fluxo inicia quando o ator seleciona Menu > Cadastro de Empresa.

1. O sistema disponibiliza a interface de Cadastro de Empresa;
1. O ator seleciona o Tipo Pessoa = Jurídica;
1. O ator preenche os campos obrigatórios (Razão Social, CNPJ, Nome Fantasia, Perfil);
1. O ator aciona Faturamento Direto, se desejar;
1. O ator anexa o documento comprobatório de acordo com o Perfil selecionado;
1. O ator aciona botão (+) e aciona Salvar;
1. O sistema registra o cadastro da empresa **[RN01][RN02]**;
1. O sistema exibe o modal Sucesso **[M01]**;
1. O ator aciona o botão de Fechar;
1. O sistema redireciona para interface de Dashboard;
1. O ator aguarda aprovação da empresa **[RN03]**.

### Fluxos Alternativos

#### **[FA01]** Cadastro Empresa - Pessoa Física - Usuário Externo

Este fluxo se inicia a partir do passo 2 do Fluxo Principal.

1. O ator seleciona o Tipo Pessoa = Física
1. O ator preenche os campos obrigatórios;
    - Informa a Nome;
    - Informa o CPF;
    - Informa o Nome Fantasia;
    - Seleciona o Perfil;
    - Informa os demais campos, se desejar;
1. O ator aciona Faturamento Direto, se desejar;
1. O ator anexa o documento comprobatório de acordo com o Perfil selecionado;
1. O ator aciona botão (+) e aciona o botão de Salvar;
1. O sistema registra o cadastro da empresa **[RN01][RN02]**;
1. O sistema exibe o modal Sucesso **[M01]**;
1. O ator aciona o botão de Fechar;
1. O sistema redireciona para interface de Dashboard;
1. O ator aguarda aprovação da empresa **[RN03]**.

#### **[FA02]** Cadastro Empresa - Pessoa Estrangeira - Usuário Externo 

Este fluxo se inicia a partir do passo 2 do Fluxo Principal.

1. O ator seleciona o Tipo Pessoa = Estrangeira;
1. O ator preenche os campos obrigatórios;
    - Informa a Razão Social;
    - Informa o Identificador estrangeiro;
    - Informa o Nome Fantasia;
    - Seleciona o Perfil;
    - Informa os demais campos, se desejar;
1. O ator aciona Faturamento Direto, se desejar;
1. O ator anexa o documento comprobatório de acordo com o Perfil selecionado;
1. O ator aciona botão (+) e aciona o botão de Salvar;
1. O sistema registra o cadastro da empresa **[RN01][RN02]**;
1. O sistema exibe o modal Sucesso **[M01]**;
1. O ator aciona o botão de Fechar;
1. O sistema redireciona para interface de Dashboard;
1. O ator aguarda aprovação da empresa **[RN03]**.

#### **[FA03]** Cadastro de Empresa - Usuário Interno

Este fluxo inicia quando o ator seleciona Administração de Empresas > Empresas.

1. O sistema disponibiliza a interface de Listagem de Empresas;
1. O ator aciona o botão (+);
1. O sistema disponibiliza a interface de Cadastro de Empresa;
1. Repetir os passos 2 até 9 do Fluxo Principal;
    - Repetir os passos 1 até 8 do Fluxo Alternativo **[FA01]**;
    - Repetir os passos 1 até 8 do Fluxo Alternativo **[FA02]**;
1. O sistema redireciona para interface Listagem de Empresas.

### Fluxos de Exceção

#### **[FE01]** Preencha com o valor mínimo

Este fluxo se inicia a partir do:

- passo 6 do Fluxo Principal;
- passo 5 do Fluxo Alternativo **[FA01]**;
- passo 5 do Fluxo Alternativo **[FA02]**;
    1. O sistema valida que o campo está preenchido com menos do valor mínimo de caracteres a serem inseridos, então emite a mensagem de alerta “Mínimo de [valor mínimo do campo] caracteres”.

#### **[FE02]** Campo CNPJ não preenchido com 14 caracteres

Este fluxo se inicia a partir do passo 6 do Fluxo Principal.

1. O sistema emite a mensagem de alerta “CNPJ inválido”.

#### **[FE03]** Campo CPF não preenchido com 11 caracteres

Este fluxo se inicia a partir do passo 6 do Fluxo Principal.

1. O sistema emite a mensagem de alerta “CPF inválido”.

#### **[FE04]** Campo CNPJ inválido (modal)

Este fluxo se inicia a partir do passo 6 do Fluxo Principal.

1. O sistema disponibiliza o modal de atenção **[M04]**.

#### **[FE05]** Campo CPF inválido (modal)

Este fluxo se inicia a partir do passo 6 do Fluxo Principal.

1. O sistema disponibiliza o modal de atenção **[M05]**.

#### **[FE07]** Campo Perfil não preenchido

Este fluxo se inicia a partir do passo 6 do Fluxo Principal.

1. O sistema emite a mensagem de alerta “Selecione um perfil para a empresa”.

#### **[FE08]** Campo Perfil não encontrado

Este fluxo se inicia a partir do passo 6 do Fluxo Principal.

1. O sistema disponibiliza modal de atenção **[M03].**

#### **[FE09]** Documento obrigatório não anexado

Este fluxo se inicia a partir do passo 6 do Fluxo Principal.

1. O sistema disponibiliza o modal de aviso **[M02]**

#### **[FE10]** Documento obrigatório com formato inválido

Este fluxo se inicia a partir do passo 5 do Fluxo Principal.

1. O sistema disponibiliza o modal de arquivo inválido **[M08]**.

#### **[FE11]** Documento obrigatório e opcional duplicados

Este fluxo se inicia a partir do passo 5 do Fluxo Principal.

1. O usuário anexa no campo Opcional o mesmo arquivo usado como documento obrigatório
1. O sistema disponibiliza o modal de arquivo duplicado **[M06]**.

### Regras de Negócio

**[RN01]** Caso o cadastro da empresa seja realizado por um usuário interno, o cadastro já deve ser aprovado automaticamente. E o usuário interno deve ser atribuir um usuário externo como responsável da empresa.

**[RN02]** Caso o cadastro da empresa seja realizado por um usuário externo, o cadastro deve aguardar aprovação.

**[RN03]** Caso o cadastro da empresa seja realizado por meio de integração com o Siscomex, após ser aprovado o cadastro, o usuário interno deve ser atribuir um usuário externo como responsável da empresa.

### Mensagens / Modais

| ID | Título | Mensagem | Ação |
| --- | --- | --- | --- |
| M01 | Sucesso | Empresa cadastrada com sucesso. | Fechar |
| M02 | Aviso | É necessário enviar os arquivos obrigatórios para prosseguir | Fechar |
| M03 | Atenção | Ocorreu um erro ao encontrar o perfil | Fechar |
| M04 | Atenção | CNPJ fornecido inválido | Fechar |
| M05 | Atenção | CPF inválido | Fechar |
| M06 | Arquivo duplicado | Arquivo duplicado | Fechar |
| M07 | Arquivo inválido | Tamanho de arquivo não suportado. | Fechar |
| M08 | Arquivo inválido | São válidos somente arquivos do tipo: pdf, png, jpg ou jpeg. | Fechar |
| M09 | Remover arquivo | Deseja realmente remover este arquivo? | Cancelar / Confirmar |
| M10 | Sucesso | Empresa aprovada com sucesso | Fechar |
| M11 | Reprovar Empresa - [Nome da Empresa] | [S0] | Voltar / Reprovar |
| M12 | Sucesso | Empresa reprovada com sucesso | Fechar |
| M13 | Sucesso | Empresa atualizada com sucesso | Fechar |

## Parte 02

Tabela FUNCIONARIOS

| Campo | Tipo | Tamanho | Referência | Índice |
| --- | --- | --- | --- | --- |
| ID_Func | Number | 9 | PK | Sim |
| Nome_Func | Varchar2 | 50 | | Sim |
| Id_FUN_Cargo | Number | 9 | FK | Sim |
| Id_FUN_Depto | Number | 9 | FK | Sim |
| Data_Contrata | Date | 8 | | Não |
| Data_Demis | Date | 8 | | Não |
| Salario | Number | 15,2 | | Sim |

Tabela CARGOS   

| Campo | Tipo | Tamanho | Referência | Índice |
| --- | --- | --- | --- | --- |
| Id_CAR_Cargo | Number | 9 | PK | Sim |
| Nome_CAR_Cargo | Varchar2 | 50 | | Sim |

Tabela DEPARTAMENTOS

| Campo | Tipo | Tamanho | Referência | Índice |
| --- | --- | --- | --- | --- |
| Id_DEP_Depto | Number | 9 | PK | Sim |
| Nome_DEP_Depto | Varchar2 | 50 | | Sim |


De acordo com as tabelas abaixo, desenvolva as Querys solicitadas:

1. Query para listar todos os funcionários com os nomes dos cargos e departamentos;
    > SELECT * FROM FUNCIONARIOS F
    > LEFT JOIN CARGOS C ON F.Id_FUN_Cargo = C.Id_CAR_Cargo
    > LEFT JOIN DEPARTAMENTOS D ON F.Id_FUN_Depto = D.Id_DEP_Depto;

1. Query para listar todos os funcionários que estão ativos na empresa;
    > SELECT * FROM FUNCIONARIOS F
    > WHERE F.Data_Demis IS NULL;

1. Query para listar todos os funcionários que trabalham na área de Controladoria;
    > SELECT * FROM FUNCIONARIOS F
    > LEFT JOIN DEPARTAMENTOS D ON F.Id_FUN_Depto = D.Id_DEP_Depto
    > WHERE D.Nome_DEP_Depto = 'Controladoria';

1. Query para listar todos os funcionários onde o salário seja superior a R$ 2.900,00;
    > SELECT * FROM FUNCIONARIOS F
    > WHERE F.Salario > 2900;

1. Query para listar a quantidade de pessoas em cada departamento.
    > SELECT D.Nome_DEP_Depto, COUNT(*) FROM FUNCIONARIOS F
    > LEFT JOIN DEPARTAMENTOS D ON F.Id_FUN_Depto = D.Id_DEP_Depto
    > GROUP BY D.Nome_DEP_Depto;

package com.st.portal.payload.request;

import com.st.portal.model.ECompanyType;
import com.st.portal.model.EProfileType;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CompanyRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotNull(message = "Tipo de empresa é obrigatório")
    private ECompanyType type;

    @CNPJ(message = "CNPJ inválido")
    private String cnpj;

    @CPF(message = "CPF inválido")
    private String cpf;

    private String foreignDocument;
    private String nickname;

    @NotNull(message = "Tipo de perfil é obrigatório")
    private EProfileType profileType;

    private BigDecimal companyIncome;
    private String documentUrl;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ECompanyType getType() { return type; }
    public void setType(ECompanyType type) { this.type = type; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getForeignDocument() { return foreignDocument; }
    public void setForeignDocument(String foreignDocument) { this.foreignDocument = foreignDocument; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public EProfileType getProfileType() { return profileType; }
    public void setProfileType(EProfileType profileType) { this.profileType = profileType; }
    public BigDecimal getCompanyIncome() { return companyIncome; }
    public void setCompanyIncome(BigDecimal companyIncome) { this.companyIncome = companyIncome; }
    public String getDocumentUrl() { return documentUrl; }
    public void setDocumentUrl(String documentUrl) { this.documentUrl = documentUrl; }
}

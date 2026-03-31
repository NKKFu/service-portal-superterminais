package com.st.portal.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonGetter;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECompanyType type;

    @Column(unique = true)
    private String cnpj;
    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
    private String foreignDocument;
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EProfileType profileType;

    private BigDecimal companyIncome;
    private String documentUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_user_id")
    @JsonIgnore
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejected_by_user_id")
    @JsonIgnore
    private User rejectedBy;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private CompanyDocument document;

    public Company() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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

    @JsonIgnore
    public User getApprovedBy() { return approvedBy; }
    public void setApprovedBy(User approvedBy) { this.approvedBy = approvedBy; }

    @JsonGetter("approvedBy")
    public String getApprovedByUsername() {
        return approvedBy != null ? approvedBy.getUsername() : null;
    }

    @JsonIgnore
    public User getRejectedBy() { return rejectedBy; }
    public void setRejectedBy(User rejectedBy) { this.rejectedBy = rejectedBy; }

    @JsonGetter("rejectedBy")
    public String getRejectedByUsername() {
        return rejectedBy != null ? rejectedBy.getUsername() : null;
    }

    public CompanyDocument getDocument() { return document; }
    public void setDocument(CompanyDocument document) { this.document = document; }

    @JsonGetter("hasDocument")
    public boolean hasDocument() { return document != null; }

    @JsonGetter("documentFileName")
    public String getDocumentFileName() { return document != null ? document.getFileName() : null; }
}

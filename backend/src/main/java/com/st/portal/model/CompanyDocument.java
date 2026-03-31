package com.st.portal.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "company_documents")
public class CompanyDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @JsonIgnore
    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] fileData;

    @Column(nullable = false, unique = true)
    private String fileHash;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    @JsonIgnore
    private Company company;

    public CompanyDocument() {}

    public CompanyDocument(String fileName, String fileType, byte[] fileData, String fileHash, Company company) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
        this.fileHash = fileHash;
        this.company = company;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
    public String getFileHash() { return fileHash; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}

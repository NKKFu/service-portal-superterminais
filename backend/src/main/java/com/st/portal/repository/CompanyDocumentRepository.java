package com.st.portal.repository;

import com.st.portal.model.CompanyDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyDocumentRepository extends JpaRepository<CompanyDocument, Long> {
    Optional<CompanyDocument> findByFileHash(String fileHash);
    Optional<CompanyDocument> findByCompanyId(Long companyId);
}

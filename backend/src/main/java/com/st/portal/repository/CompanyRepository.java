package com.st.portal.repository;

import com.st.portal.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCnpj(String cnpj);
    Optional<Company> findByCpf(String cpf);
    Optional<Company> findByForeignDocument(String foreignDocument);
}

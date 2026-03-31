package com.st.portal.controller;

import com.st.portal.model.Company;
import com.st.portal.model.CompanyDocument;
import com.st.portal.model.ECompanyType;
import com.st.portal.payload.request.CompanyRequest;
import com.st.portal.payload.response.MessageResponse;
import com.st.portal.repository.CompanyDocumentRepository;
import com.st.portal.repository.CompanyRepository;
import com.st.portal.repository.UserRepository;
import com.st.portal.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/companies")
@Tag(name = "Company")
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyDocumentRepository companyDocumentRepository;

    public CompanyController(CompanyRepository companyRepository, UserRepository userRepository,
                             CompanyDocumentRepository companyDocumentRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.companyDocumentRepository = companyDocumentRepository;
    }

    private String sha256Hex(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash file", e);
        }
    }

    private ResponseEntity<?> validateCompanyRequest(CompanyRequest request, Long companyId) {
        if (request.getType() == ECompanyType.PESSOA_JURIDICA && (request.getCnpj() == null || request.getCnpj().isEmpty())) {
            return ResponseEntity.badRequest().body(new MessageResponse("CNPJ é obrigatório para PESSOA JURIDICA"));
        }
        if (request.getType() == ECompanyType.PESSOA_FISICA && (request.getCpf() == null || request.getCpf().isEmpty())) {
            return ResponseEntity.badRequest().body(new MessageResponse("CPF é obrigatório para PESSOA FISICA"));
        }
        if (request.getType() == ECompanyType.PESSOA_ESTRANGEIRA && (request.getForeignDocument() == null || request.getForeignDocument().isEmpty())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Documento Estrangeiro é obrigatório para PESSOA ESTRANGEIRA"));
        }

        if (request.getCnpj() != null && !request.getCnpj().isEmpty()) {
            Optional<Company> existing = companyRepository.findByCnpj(request.getCnpj());
            if (existing.isPresent() && !existing.get().getId().equals(companyId)) {
                return ResponseEntity.badRequest().body(new MessageResponse("CNPJ já cadastrado!"));
            }
        }
        if (request.getCpf() != null && !request.getCpf().isEmpty()) {
            Optional<Company> existing = companyRepository.findByCpf(request.getCpf());
            if (existing.isPresent() && !existing.get().getId().equals(companyId)) {
                return ResponseEntity.badRequest().body(new MessageResponse("CPF já cadastrado!"));
            }
        }
        if (request.getForeignDocument() != null && !request.getForeignDocument().isEmpty()) {
            Optional<Company> existing = companyRepository.findByForeignDocument(request.getForeignDocument());
            if (existing.isPresent() && !existing.get().getId().equals(companyId)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Documento Estrangeiro já cadastrado!"));
            }
        }

        return null;
    }

    private void applyCompanyFields(Company company, CompanyRequest request) {
        company.setName(request.getName());
        company.setType(request.getType());
        company.setCnpj(request.getCnpj());
        company.setCpf(request.getCpf());
        company.setForeignDocument(request.getForeignDocument());
        company.setNickname(request.getNickname());
        company.setProfileType(request.getProfileType());
        company.setCompanyIncome(request.getCompanyIncome());
        company.setDocumentUrl(request.getDocumentUrl());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCompany(
            @RequestPart("data") @Valid CompanyRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("É necessário enviar os arquivos obrigatórios para prosseguir."));
        }

        ResponseEntity<?> validationError = validateCompanyRequest(request, null);
        if (validationError != null) return validationError;

        byte[] bytes;
        try { bytes = file.getBytes(); } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro ao processar o arquivo."));
        }
        String hash = sha256Hex(bytes);
        if (companyDocumentRepository.findByFileHash(hash).isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Arquivo duplicado"));
        }

        Company company = new Company();
        applyCompanyFields(company, request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            if (userDetails.isInternal()) {
                userRepository.findByUsername(userDetails.getUsername()).ifPresent(company::setApprovedBy);
            }
        }

        Company savedCompany = companyRepository.save(company);

        CompanyDocument doc = new CompanyDocument(file.getOriginalFilename(),
                file.getContentType(), bytes, hash, savedCompany);
        companyDocumentRepository.save(doc);
        savedCompany.setDocument(doc);

        return ResponseEntity.ok(savedCompany);
    }

    @GetMapping
    @PreAuthorize("hasRole('INTERNAL')")
    public ResponseEntity<List<Company>> listCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INTERNAL')")
    public ResponseEntity<?> editCompany(
            @PathVariable Long id,
            @RequestPart("data") @Valid CompanyRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        Optional<Company> companyData = companyRepository.findById(id);
        if (companyData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ResponseEntity<?> validationError = validateCompanyRequest(request, id);
        if (validationError != null) return validationError;

        Company company = companyData.get();
        applyCompanyFields(company, request);

        if (file != null && !file.isEmpty()) {
            byte[] bytes;
            try { bytes = file.getBytes(); } catch (Exception e) {
                return ResponseEntity.badRequest().body(new MessageResponse("Erro ao processar o arquivo."));
            }
            String hash = sha256Hex(bytes);

            Optional<CompanyDocument> existingByHash = companyDocumentRepository.findByFileHash(hash);
            if (existingByHash.isPresent() && !existingByHash.get().getCompany().getId().equals(id)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Arquivo duplicado"));
            }

            Optional<CompanyDocument> existingDoc = companyDocumentRepository.findByCompanyId(id);
            if (existingDoc.isPresent()) {
                CompanyDocument doc = existingDoc.get();
                doc.setFileName(file.getOriginalFilename());
                doc.setFileType(file.getContentType());
                doc.setFileData(bytes);
                doc.setFileHash(hash);
                companyDocumentRepository.save(doc);
            } else {
                CompanyDocument doc = new CompanyDocument(file.getOriginalFilename(),
                        file.getContentType(), bytes, hash, company);
                companyDocumentRepository.save(doc);
            }
        }

        return ResponseEntity.ok(companyRepository.save(company));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('INTERNAL')")
    public ResponseEntity<?> approveCompany(@PathVariable Long id) {
        Optional<Company> companyData = companyRepository.findById(id);
        if (companyData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Company company = companyData.get();
        if (company.getApprovedBy() != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Esta empresa já foi aprovada."));
        }
        if (company.getRejectedBy() != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Uma empresa rejeitada não pode ser aprovada."));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            userRepository.findByUsername(userDetails.getUsername()).ifPresent(company::setApprovedBy);
        }

        companyRepository.save(company);
        return ResponseEntity.ok(new MessageResponse("Empresa aprovada com sucesso por " + company.getApprovedBy().getUsername()));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('INTERNAL')")
    public ResponseEntity<?> rejectCompany(@PathVariable Long id) {
        Optional<Company> companyData = companyRepository.findById(id);
        if (companyData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Company company = companyData.get();
        if (company.getApprovedBy() != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Uma empresa aprovada não pode ser rejeitada."));
        }
        if (company.getRejectedBy() != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Esta empresa já foi rejeitada."));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            userRepository.findByUsername(userDetails.getUsername()).ifPresent(company::setRejectedBy);
        }

        companyRepository.save(company);
        return ResponseEntity.ok(new MessageResponse("Empresa rejeitada com sucesso por " + company.getRejectedBy().getUsername()));
    }

    @GetMapping("/{id}/document")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Optional<CompanyDocument> doc = companyDocumentRepository.findByCompanyId(id);
        if (doc.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CompanyDocument d = doc.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + d.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(d.getFileType() != null ? d.getFileType() : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(d.getFileData());
    }
}


package by.dziashko.frm.backend.repo;

import by.dziashko.frm.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
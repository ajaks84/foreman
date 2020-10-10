package by.dziashko.frm.backend.service;

import by.dziashko.frm.backend.entity.Company;
import by.dziashko.frm.backend.repo.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService {

  private CompanyRepository companyRepository;

  public CompanyService(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public List<Company> findAll() {
    return companyRepository.findAll();
  }

  public Map<String, Integer> getStats() {
    HashMap<String, Integer> stats = new HashMap<>();
    findAll().forEach(company -> stats.put(company.getName(), company.getEmployees().size()));
    return stats;
  }

}
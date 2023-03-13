package com.example.msi.service.impl;

import com.example.msi.domains.Company;
import com.example.msi.exceptions.ExceptionUtils;
import com.example.msi.exceptions.MSIException;
import com.example.msi.models.company.*;
import com.example.msi.repository.CompanyRepository;
import com.example.msi.service.CompanyService;
import com.example.msi.shared.Constant;
import com.example.msi.utils.ExcelUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
  private final CompanyRepository repository;

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 10;

  public static Pageable getPageable(Integer page, Integer size) {
    page = page != null ? page : DEFAULT_PAGE_NUMBER;
    size = size != null ? size : DEFAULT_PAGE_SIZE;
    return PageRequest.of(page, size);
  }

  @Override
  public Page<Company> getAllCompany(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public Company getCompanyById(int id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public Company addCompany(CreateCompanyDTO payload) {
    var entity = Company.getInstance(payload);
    return repository.save(entity);
  }

  @Override
  public Optional<Company> updateCompany(@NonNull UpdateCompanyDTO payload) {
    var id=payload.getId();
    return repository.findById(id).map(e ->{
      e.update(payload);
      return repository.save(e);
    });
  }

  @Override
  public List<Company> searchCompanyByName(@NonNull String name) {
    return repository.getCompanyByName(name);
  }

  @Override
  public void deleteCompany(int id) {
    repository.deleteById(id);
  }

  @Override
  public Object export(HttpServletRequest request, Object req) throws MSIException {
    CompanyReqDTO reqDTO = (CompanyReqDTO) req;
    reqDTO.setPageable(
        PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Constant.ID).ascending()));

    Page<Company> page = this.getAllCompany(reqDTO.getPageable());
    List<Company> list = page.getContent();
    if (list.isEmpty()) {
      throw new MSIException(
          ExceptionUtils.E_EXPORT_COMPANY,
          ExceptionUtils.messages.get(ExceptionUtils.E_EXPORT_COMPANY));
    }
    // chuyển sang list obj
    List<Object> dataExport = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataExport.add(new CompanyExportDTO(i + 1, list.get(i)));
    }
    // tạo workbook
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (Workbook workbook = ExcelUtils.executeExport(dataExport);
         bos) {
      workbook.write(bos);
    } catch (Exception e) {
      throw new MSIException(
          ExceptionUtils.E_EXPORT_EXCEL,
          ExceptionUtils.messages.get(ExceptionUtils.E_EXPORT_EXCEL));
    }
    return bos.toByteArray();
  }
}

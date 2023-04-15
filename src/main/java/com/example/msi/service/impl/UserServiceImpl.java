package com.example.msi.service.impl;

import com.example.msi.domains.Teacher;
import com.example.msi.domains.User;
import com.example.msi.models.teacher.UpdateTeacherDTO;
import com.example.msi.models.user.*;
import com.example.msi.repository.TeacherRepository;
import com.example.msi.shared.Constant;
import com.example.msi.shared.ValidateError;
import com.example.msi.shared.enums.Role;
import com.example.msi.repository.UserRepository;
import com.example.msi.response.Data;
import com.example.msi.security.CustomUserDetails;
import com.example.msi.service.MailService;
import com.example.msi.service.UserService;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.shared.utils.ExcelUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.msi.shared.Constant.DATETIME_FORMATTER_2;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;
  private final TeacherRepository teacherService;

  // JWTAuthenticationFilter sẽ sử dụng hàm này
  @Transactional
  public UserDetails loadUserById(Integer id) {
    User user = repository.findById(id).orElseThrow(
        () -> new UsernameNotFoundException("User not found with id : " + id)
    );

    return new CustomUserDetails(user);
  }

  @Override
  public Data register(CreateUserDTO userRegister, StringBuffer siteURL) throws IllegalAccessException, MessagingException {
    return registerUser(userRegister, siteURL, Role.STUDENT);
  }

  @Override
  public Data registerTeacherAccount(CreateUserDTO userRegister, StringBuffer siteURL) throws IllegalAccessException, MessagingException {
    return registerUser(userRegister, siteURL, Role.TEACHER);
  }

  private Data registerUser(CreateUserDTO userRegister, StringBuffer siteURL, Role role) throws IllegalAccessException, MessagingException {
    Optional<User> optional = repository.findByEmail(userRegister.getEmail());
    if (optional.isPresent()) {
      throw new IllegalAccessException("Email đã tồn tại trong hệ thống");
    }
    var user = User.getInstance(userRegister);
    user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
    user.setEnabled(false);
    user.setRole(role);
    user.setVerificationCode(RandomString.make(64));
    StringBuilder url = new StringBuilder("159.65.4.245/login?verify=");

    Map<String, Object> props = new HashMap<>();
    props.put("email", Arrays.stream(user.getEmail().split("@")).findFirst());
    props.put("url", url.append(user.getVerificationCode()).toString());

    mailService.sendMail(props, user.getEmail(), "sendMail", "Xác thực tài khoản");
    repository.save(user);

    if (role == Role.TEACHER) {
      teacherService.save(
          Teacher.getInstance(
              UpdateTeacherDTO.getInstance(
                  new Teacher(
                      0,
                      user.getId(),
                      null,
                      null,
                      null,
                      false,
                      null
                  )
              ),
              user
          )
      );
    }

    return new Data(UserDTO.getInstance(user));
  }


  @Override
  public Data verify(String verificationCode) throws IllegalAccessException {
    Optional<User> optionalUser = repository.findByVerificationCode(verificationCode);
    if (optionalUser.isEmpty()) {
      throw new IllegalAccessException("Verification Code không tồn tại");
    }
    User user = optionalUser.get();
    user.setEnabled(true);
    repository.save(user);
    return new Data(null);
  }

  @Override
  public Data updatePassword(UpdatePasswordUserDTO updatePasswordUserDTO) throws IOException {
    Optional<User> optionalUser = repository.findById(updatePasswordUserDTO.getId());
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (passwordEncoder.matches(updatePasswordUserDTO.getPassword(), user.getPassword())) {
        String encryptedPassword = passwordEncoder.encode(updatePasswordUserDTO.getNewPassword());
        user.setPassword(encryptedPassword);
        user.setUpdatePasswordToken(null);
        repository.save(user);
        return new Data(null);
      }
      throw new IOException("Mật khẩu không đúng");
    } else {
      throw new IOException("Không có dữ liệu của người dùng này");
    }
  }

  @Override
  public Data forgotPassword(String mail) throws MessagingException {
    Optional<User> optionalUser = repository.findByEmail(mail);
    if (optionalUser.isEmpty()) return new Data(null);
    String pass = RandomString.make(10);
    User user = optionalUser.get();
    user.setPassword(passwordEncoder.encode(pass));
    repository.save(user);
    Map<String, Object> props = new HashMap<>();
    props.put("email", user.getEmail());
    props.put("pass", pass);

    mailService.sendMail(props, user.getEmail(), "forgotPassword", "Quên mật khẩu");
    return new Data(null);
  }

  @Override
  public Optional<User> userAccessInformation() throws IllegalAccessException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalAccessException("Chưa có tài khoản đăng nhập");
    }
    String email = authentication.getName();
    return repository.findByEmail(email);
  }

  @Override
  public Data update(UpdateUserDTO updateUser) throws IOException {
    User user = repository.findById(updateUser.getId())
        .orElseThrow(() -> new IOException("id này không tồn tại"));
    user.setDob(updateUser.getDob());
    user.setFullName(updateUser.getFullName());
    user.setPhoneNumber(updateUser.getPhoneNumber());
    repository.save(user);
    return new Data(user);
  }

  @Override
  public Optional<User> findByEmail(String mail) {
    return repository.findByEmail(mail);
  }

  @Override
  public Page<User> findAll(@NonNull SearchUserDTO filter) {
    var spec = filter.getSpecification();
    var pageable = filter.getPageable();
    return repository.findAll(spec, pageable);
  }

  @Override
  public Optional<User> changeEnable(@NonNull Integer userId) {
    Optional<User> optionalUser = repository.findById(userId);
    optionalUser.ifPresent(user -> {
      user.setEnabled(!user.isEnabled());
      repository.save(user);
    });
    return optionalUser;
  }

  @Override
  public Optional<User> findById(int id) {
    return repository.findById(id);
  }

  @Override
  public List<User> findAllByRole(Role role) {
    return repository.findAllByRole(role);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    return new CustomUserDetails(user);
  }

  @Override
  public byte[] templateDownload(HttpServletRequest request) throws IOException {
    // Số lượng bản ghi được cấu hình chp phép import
//    var totalImport = new AtomicInteger(10000);

    var columns = Constant.INCOME_TEACHER_ACCOUNT_IMPORT_HEADER;
    var workbook = new XSSFWorkbook();
    XSSFSheet realSheet = workbook.createSheet("Danh sách Import");

    // Style phần header
    ExcelUtils.setHeader(columns, workbook, realSheet);
    var bos = new ByteArrayOutputStream();
    workbook.write(bos);
    return bos.toByteArray();
  }

  @Override
  public String importFile(MultipartFile file, HttpServletRequest request) throws IOException, MSIException {
    //Check chưa đính kèm file
    if (file.isEmpty()) {
      throw new MSIException(
          ExceptionUtils.E_FILE_IS_EMPTY,
          ExceptionUtils.messages.get(ExceptionUtils.E_FILE_IS_EMPTY));
    }
    double fileSize = file.getBytes().length;
    if (fileSize / (1024 * 1024) > 3) {
      throw new MSIException(
          ExceptionUtils.E_FILE_TOO_LARGE_THAN_DEFAULT_IMPORT_3MB,
          ExceptionUtils.messages.get(ExceptionUtils.E_FILE_TOO_LARGE_THAN_DEFAULT_IMPORT_3MB));
    }
    // Số lượng bản ghi được cấu hình chp phép import
    var totalImport = new AtomicInteger(10000);

    //Kiểm tra file đẩy lên có đúng form
    var workbook = ExcelUtils.checkFileExcel(file);

    //Đọc dữ liệu sheet đầu tiên
    var sheet = workbook.getSheetAt(0);
    var rowHeader = sheet.getRow(0);

    // Validate form file excel như file mẫu (hoặc theo file danh sách lỗi)
    var headerSamples = Constant.INCOME_TEACHER_ACCOUNT_IMPORT_HEADER;
    List<String> headerData = new ArrayList<>();
    for (Cell cell : rowHeader) {
      headerData.add(cell.getStringCellValue());
    }
    boolean checkFile = headerSamples.equals(headerData);

    // check file không đúng định dạng
    if (BooleanUtils.isFalse(checkFile)) {
      throw new MSIException(
          ExceptionUtils.E_FILE_IS_NOT_FORMAT_CORRECT,
          ExceptionUtils.messages.get(ExceptionUtils.E_FILE_IS_NOT_FORMAT_CORRECT));
    }
    // check file đẩy quá số lượng dữ liệu cho phép tron phần cấu hính
    int totalImportFile = sheet.getLastRowNum();
    if (totalImportFile > totalImport.get()) {
      throw new MSIException(
          ExceptionUtils.E_FILE_DATA_EXCEED_NUMBER_PERMITTED,
          ExceptionUtils.buildMessage(
              ExceptionUtils.E_FILE_DATA_EXCEED_NUMBER_PERMITTED, totalImport.get()));
    }
    // Xử lý dữ liệu trong file import
    List<IncomeUserCreateDTO> allDataImport = new ArrayList<>();
    var message = this.getDataFromFileImport(sheet, allDataImport);

    workbook.close();
    // validate duplicate
    var duplicates =
        allDataImport.parallelStream()
            .filter(n -> StringUtils.isNotBlank(n.getCheckDuplicate()))
            .collect(Collectors.groupingBy(IncomeUserCreateDTO::getCheckDuplicate));
    duplicates.entrySet().parallelStream()
        .forEach(
            item -> {
              if (item.getValue().size() > 1) {
                item.getValue()
                    .forEach(
                        n ->
                            n.getStrError()
                                .add(
                                    ValidateError.builder()
                                        .code(Constant.F_DUPLICATE)
                                        .errorMessage("Dữ liệu trùng với dữ liệu trong file")
                                        .build()));
              }
            });
    //    Lưu dữ liệu
    var dataInt = allDataImport.parallelStream()
        .map(User::new)
        .collect(Collectors.toList());

    Optional.ofNullable(dataInt).orElseGet(Collections::emptyList).forEach(
        user -> {
          try {
            registerTeacherAccount(CreateUserDTO.getInstance(user), request.getRequestURL());
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          } catch (MessagingException e) {
            throw new RuntimeException(e);
          }
        }
    );

    if (message.trim().isEmpty()) {
      return Constant.IMPORT_SUCCESS;
    } else {
      return message.substring(0, message.length() - 2);
    }
  }

  private String getDataFromFileImport(Sheet sheet, List<IncomeUserCreateDTO> allData) {
    StringBuilder error = new StringBuilder();
    int rowTotal = sheet.getLastRowNum();
    //  Đọc dữ liệu trong file bỏ dòng header(dòng 1).
    IncomeUserCreateDTO item;
    var dataFormatter = new DataFormatter();
    for (var i = 1; i <= rowTotal; i++) {
      // lấy giá trị từng row
      var row = sheet.getRow(i);
      // Bỏ qua dòng nào trống
      if (ExcelUtils.isRowEmpty(row)) {
        continue;
      }
      item = new IncomeUserCreateDTO();
      // STT
      item.setNumberSort(i);
      // set data
      this.setDataFromFile(item, dataFormatter, row);

      allData.add(item);
    }
    return error.toString();
  }

  // set data import file
  private void setDataFromFile(
      IncomeUserCreateDTO item, DataFormatter dataFormatter, Row row) {
    item.setName(
        row.getCell(0) != null ? dataFormatter.formatCellValue(row.getCell(0)).trim() : null);
    item.setPhoneNumber(
        row.getCell(1) != null ? dataFormatter.formatCellValue(row.getCell(1)).trim() : null);
    item.setEmail(
        row.getCell(2) != null ? dataFormatter.formatCellValue(row.getCell(2)).trim() : null);
    item.setDob(
        row.getCell(3) != null ? LocalDate.parse(dataFormatter.formatCellValue(row.getCell(3)).trim(), DATETIME_FORMATTER_2) : null);
  }

  private boolean validateEmail(String email) {
    return Pattern.compile(Constant.EMAIL_REGEX).matcher(email).matches();
  }
}

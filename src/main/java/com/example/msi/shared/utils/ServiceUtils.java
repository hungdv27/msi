package com.example.msi.shared.utils;

import com.example.msi.service.*;
import com.example.msi.shared.ApplicationContextHolder;

import static com.example.msi.shared.ApplicationContextHolder.getBean;

public class ServiceUtils {
  private ServiceUtils() {
  }

  public static StudentService getStudentService() {
    return SingletonHelper.STUDENT_SERVICE;
  }

  public static InternshipApplicationService getInternshipApplicationService() {
    return SingletonHelper.INTERNSHIP_APPLICATION_SERVICE;
  }

  public static ReportFileService getReportFileService() {
    return SingletonHelper.REPORT_FILE_SERVICE;
  }

  public static InternshipProcessService getInternshipProcessService() {
    return SingletonHelper.INTERNSHIP_PROCESS_SERVICE;
  }

  public static FileService getFileService() {
    return SingletonHelper.FILE_SERVICE;
  }

  public static PostFileService getPostFileService() {
    return SingletonHelper.POST_FILE_SERVICE;
  }

  public static CompanyService getCompanyService() {
    return SingletonHelper.COMPANY_SERVICE;
  }

  public static ReportService getReportService() {
    return SingletonHelper.REPORT_SERVICE;
  }

  public static TeacherService getTeacherService() {
    return SingletonHelper.TEACHER_SERVICE;
  }

  public static UserService getUserService() {
    return SingletonHelper.USER_SERVICE;
  }

  private static class SingletonHelper {
    private static final StudentService STUDENT_SERVICE = getBean(StudentService.class);
    private static final InternshipApplicationService INTERNSHIP_APPLICATION_SERVICE = getBean(InternshipApplicationService.class);
    private static final InternshipProcessService INTERNSHIP_PROCESS_SERVICE = getBean(InternshipProcessService.class);
    private static final ReportFileService REPORT_FILE_SERVICE = ApplicationContextHolder.getBean(ReportFileService.class);
    private static final FileService FILE_SERVICE = ApplicationContextHolder.getBean(FileService.class);
    private static final PostFileService POST_FILE_SERVICE = ApplicationContextHolder.getBean(PostFileService.class);
    private static final CompanyService COMPANY_SERVICE = ApplicationContextHolder.getBean(CompanyService.class);
    private static final ReportService REPORT_SERVICE = ApplicationContextHolder.getBean(ReportService.class);
    private static final TeacherService TEACHER_SERVICE = ApplicationContextHolder.getBean(TeacherService.class);
    private static final UserService USER_SERVICE = ApplicationContextHolder.getBean(UserService.class);
  }
}

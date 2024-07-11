package vn.edu.hcmuaf.fit.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.backend.util.MailStructure;
import vn.edu.hcmuaf.fit.backend.model.Employee;
import vn.edu.hcmuaf.fit.backend.model.ForgotPassword;
import vn.edu.hcmuaf.fit.backend.repository.EmployeeRepository;
import vn.edu.hcmuaf.fit.backend.repository.ForgotPasswordRepository;
import vn.edu.hcmuaf.fit.backend.service.ForgotPasswordService;
import vn.edu.hcmuaf.fit.backend.service.MailService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordServiceImpl(EmployeeRepository employeeRepository, ForgotPasswordRepository forgotPasswordRepository) {
        this.employeeRepository = employeeRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    @Override
    public void verifyEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email);

        int otp = otpGenerator();
        MailStructure mailStructure = new MailStructure();
        mailStructure.setSubject("Đặt lại mật khẩu");
        mailStructure.setContent("<html><body>Xin chào bạn,<br/>" + "\n" +
                "Vui lòng nhập mã OTP này để đặt lại mật khẩu:" + "\n" +
                "<span style='font-size: 18px;'><b>" + otp + "</b></span></body></html>");

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .employee(employee)
                .createdAt(LocalDateTime.now())
                .build();

        MailService.sendMail(email, mailStructure);

        try {
            forgotPasswordRepository.save(fp);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verifyOtp(int otp, String email) {
        Employee employee = employeeRepository.findByEmail(email);

        forgotPasswordRepository.findByOtpAndEmployee(otp, employee).orElseThrow(() ->
                new RuntimeException("Invalid OTP"));
    }

    private int otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}

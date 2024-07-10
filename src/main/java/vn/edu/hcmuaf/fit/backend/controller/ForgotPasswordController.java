package vn.edu.hcmuaf.fit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmuaf.fit.backend.model.ForgotPassword;
import vn.edu.hcmuaf.fit.backend.service.ForgotPasswordService;

@RestController
@RequestMapping("/api/forgot-password")
public class ForgotPasswordController {
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> verifyEMail(@PathVariable String email) {
        forgotPasswordService.verifyEmail(email);
        return ResponseEntity.ok("Mã OTP đã được gửi đến email của bạn.");
    }

    @PostMapping("/verify-otp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable int otp, @PathVariable String email) {
        forgotPasswordService.verifyOtp(otp, email);
        return ResponseEntity.ok("Xác thực OTP thành công!");
    }
}

package vn.edu.hcmuaf.fit.backend.service;

public interface ForgotPasswordService {
    void verifyEmail(String email);
    void verifyOtp(int otp, String email);
}

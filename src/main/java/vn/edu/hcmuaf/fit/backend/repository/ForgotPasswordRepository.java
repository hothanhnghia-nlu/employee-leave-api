package vn.edu.hcmuaf.fit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.backend.model.Employee;
import vn.edu.hcmuaf.fit.backend.model.ForgotPassword;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.employee = ?2")
    Optional<ForgotPassword> findByOtpAndEmployee(int otp, Employee employee);
}

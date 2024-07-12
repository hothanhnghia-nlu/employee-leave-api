package vn.edu.hcmuaf.fit.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class LeaveApplicationsDTO {
    private int id;
    private int userId;
    private EmployeeDTO employee;
    private String reason;
    private LocalDate from;
    private LocalDate to;
    private int status;
    private String reasonReject;
    private int receivedTo;
    private BossDTO handleBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LeaveApplicationsDTO() {
    }
}

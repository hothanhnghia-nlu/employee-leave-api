package vn.edu.hcmuaf.fit.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.backend.dto.BossDTO;
import vn.edu.hcmuaf.fit.backend.dto.EmployeeDTO;
import vn.edu.hcmuaf.fit.backend.dto.LeaveApplicationsDTO;
import vn.edu.hcmuaf.fit.backend.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.backend.model.Employee;
import vn.edu.hcmuaf.fit.backend.model.LeaveApplications;
import vn.edu.hcmuaf.fit.backend.repository.EmployeeRepository;
import vn.edu.hcmuaf.fit.backend.repository.LeaveAppsRepository;
import vn.edu.hcmuaf.fit.backend.service.LeaveAppsService;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveAppsServiceImpl implements LeaveAppsService {
    @Autowired
    private LeaveAppsRepository leaveAppsRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeService;

    public LeaveAppsServiceImpl(LeaveAppsRepository leaveAppsRepository, EmployeeRepository employeeRepository) {
        this.leaveAppsRepository = leaveAppsRepository;
        this.employeeRepository = employeeRepository;
    }

    // Create a new leave application
    @Override
    public LeaveApplications saveLeaveApps(int employeeId, LeaveApplicationsDTO leaveAppsDTO) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Employee", "Id", leaveAppsDTO.getId()));

        LeaveApplications leaveApplications = new LeaveApplications();
        leaveApplications.setId(leaveAppsDTO.getId());
        leaveApplications.setEmployee(employee);
        leaveApplications.setReason(leaveAppsDTO.getReason());
        leaveApplications.setFrom(leaveAppsDTO.getFrom());
        leaveApplications.setTo(leaveAppsDTO.getTo());
        leaveApplications.setHandleBy(employee.getBossId());
        leaveApplications.setStatus(2);
        leaveApplications.setCreatedAt(LocalDateTime.now());

        return leaveAppsRepository.save(leaveApplications);
    }

    @Override
    public List<LeaveApplicationsDTO> getAllLeaveApp() {
        List<LeaveApplications> leaveApplications = leaveAppsRepository.findAll();

        return leaveApplications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private LeaveApplicationsDTO convertToDTO(LeaveApplications leaveApps) {
        BossDTO bossDTO = null;
        EmployeeDTO employeeDTO = null;

        if (leaveApps.getHandleBy() != null) {
            bossDTO = new BossDTO(
                    leaveApps.getHandleBy().getId(),
                    leaveApps.getHandleBy().getEmail(),
                    leaveApps.getHandleBy().getFullName(),
                    leaveApps.getHandleBy().getPosition()
            );
        }

        if (leaveApps.getEmployee() != null) {
            employeeDTO = new EmployeeDTO(
                    leaveApps.getEmployee().getId(),
                    leaveApps.getEmployee().getEmail(),
                    leaveApps.getEmployee().getFullName(),
                    leaveApps.getEmployee().getPosition()
            );
        }

        LeaveApplicationsDTO leaveAppsDTO = new LeaveApplicationsDTO();
        leaveAppsDTO.setId(leaveApps.getId());
        leaveAppsDTO.setUserId(leaveApps.getEmployee().getId());
        leaveAppsDTO.setReason(leaveApps.getReason());
        leaveAppsDTO.setFrom(leaveApps.getFrom());
        leaveAppsDTO.setTo(leaveApps.getTo());
        leaveAppsDTO.setStatus(leaveApps.getStatus());
        leaveAppsDTO.setReasonReject(leaveApps.getReasonReject());
        leaveAppsDTO.setCreatedAt(leaveApps.getCreatedAt());
        leaveAppsDTO.setUpdatedAt(leaveApps.getUpdatedAt());
        leaveAppsDTO.setEmployee(employeeDTO);
        leaveAppsDTO.setHandleBy(bossDTO);

        return leaveAppsDTO;
    }

    @Override
    public LeaveApplications getLeaveAppsByID(int id) {
        return leaveAppsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("leaveApp", "Id", id));
    }

    @Override
    public List<LeaveApplications> getLeaveAppsByEmployeeId(int employeeId) {
        return leaveAppsRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<LeaveApplications> getLeaveAppsByHandleById(int handleBy) {
        return leaveAppsRepository.findByHandleById(handleBy);
    }

    // Approve leave application from boss
    @Override
    public LeaveApplications approveLeaveAppsByID(int id, LeaveApplicationsDTO leaveApps) {
        LeaveApplications existingLeaveApp = leaveAppsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Leave Application", "Id", id));

        existingLeaveApp.setReasonReject(leaveApps.getReasonReject());
        existingLeaveApp.setStatus(leaveApps.getStatus());
//        existingLeaveApp.setHandleBy();
        LeaveApplications leaveApplications = getLeaveAppsByID(id);
        Employee employee = employeeService.getEmployeeByID(leaveApplications.getEmployee().getId());
        if(leaveApps.getStatus()==1){
            Period period = Period.between(leaveApplications.getFrom(),leaveApplications.getTo() );
            int days = period.getDays();
            if(days>employee.getDayOffRemaining()){
                leaveApps.setStatus(0);
                return leaveAppsRepository.save(existingLeaveApp);
            }
            employee.setDayOffRemaining(employee.getDayOffRemaining()-days);
            employeeRepository.save(employee);
        }
        existingLeaveApp.setUpdatedAt(LocalDateTime.now());

        return leaveAppsRepository.save(existingLeaveApp);
    }

    @Override
    public void deleteLeaveAppsByID(int id) {
        leaveAppsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Leave Application", "Id", id));

        leaveAppsRepository.deleteById(id);

    }

}

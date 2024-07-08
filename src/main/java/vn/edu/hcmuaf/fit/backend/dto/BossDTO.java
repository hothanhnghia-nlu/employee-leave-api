package vn.edu.hcmuaf.fit.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BossDTO {
    private int id;
    private String email;
    private String fullName;
    private String position;

    public BossDTO(int id, String email, String fullName, String position) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.position = position;
    }
}

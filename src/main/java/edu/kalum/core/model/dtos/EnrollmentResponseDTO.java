package edu.kalum.core.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDTO implements Serializable {
    private int statusCode;
    private String message;
}

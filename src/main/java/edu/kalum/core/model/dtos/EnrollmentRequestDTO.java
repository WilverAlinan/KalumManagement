package edu.kalum.core.model.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequestDTO implements Serializable {
    @NotNull
    private String noExpediente;
    @NotNull
    private String ciclo;
    @NotNull
    private int mesInicioPago;
    @NotNull
    private String carreraId;
}

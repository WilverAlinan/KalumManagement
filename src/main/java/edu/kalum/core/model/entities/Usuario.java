package edu.kalum.core.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "El username no debe de ser vacio")
    @Column(name = "username")
    private String username;
    @NotNull(message = "El password no debe de ser vacio")
    @Column(name = "password")
    private String password;
    @Column(name = "enabled")
    private boolean enabled;
    @NotNull(message = "El Nombre no debe de ser vacio")
    @Column(name = "nombres")
    private String nombres;
    @NotNull(message = "El apellido no debe de ser vacio")
    @Column(name = "apellidos")
    private String apellidos;
    @Email(message = "El correo electronico no es valido")
    @Column(name = "email", unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id","role_id"})})

    private List<Role> roles;

}

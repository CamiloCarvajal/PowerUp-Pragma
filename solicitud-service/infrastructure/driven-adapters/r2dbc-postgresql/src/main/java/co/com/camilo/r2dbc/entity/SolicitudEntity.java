package co.com.camilo.r2dbc.entity;

import co.com.camilo.model.solicitud.Estado;
import co.com.camilo.model.solicitud.Prestamo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table(name = "solicitud")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudEntity {

    @Id
    @Column("id_solicitud")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Long monto;

    private int plazo;

    private String email;

    @Column("id_estado")
    private int estado;

    @Column("id_tipo_prestamo")
    private int prestamo;
}

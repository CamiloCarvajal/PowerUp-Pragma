package co.com.camilo.r2dbc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table("tipo_prestamo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoEntity {

    @Id
    @Column("id_tipo_prestamo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String nombre;

    @Column("monto_minimo")
    private long montoMinimo;

    @Column("monto_maximo")
    private long montoMaximo;

    @Column("tasa_interes")
    private double tasaInteres;

    @Column("validacion_automatica")
    private Boolean validacionAutomatica;
}

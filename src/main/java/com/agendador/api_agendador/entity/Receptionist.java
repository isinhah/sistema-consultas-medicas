package com.agendador.api_agendador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_receptionists")
public class Receptionist implements Serializable {

    @Id
    private Long id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(unique = true, nullable = false, name = "registration_number")
    private String registrationNumber;

    @OneToMany(mappedBy = "receptionist", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Appointment> confirmedAppointments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Receptionist that = (Receptionist) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

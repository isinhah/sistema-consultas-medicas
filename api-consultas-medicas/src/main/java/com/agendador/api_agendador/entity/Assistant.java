package com.agendador.api_agendador.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_assistants")
public class Assistant implements Serializable {

    @Id
    private Long id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @Column(unique = true, nullable = false, name = "registration_number")
    private String registrationNumber;

    @OneToMany(mappedBy = "assistant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Appointment> confirmedAppointments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Assistant that = (Assistant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

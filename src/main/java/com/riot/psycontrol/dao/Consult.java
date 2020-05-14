package com.riot.psycontrol.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"patient"})
@ToString(exclude={"patient"})
@JsonIgnoreProperties({"patient"})
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="consult")
public class Consult extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="reason")
    private String reason;
    @Column(name="description")
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

}

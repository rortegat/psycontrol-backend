package com.riot.psycontrol.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="patient")
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name="mobile")
    private String mobile;

    /*@ManyToOne
    @JoinColumn(name="user_id")
    private User user;*/

    /*@OneToMany(fetch = FetchType.LAZY, mappedBy="patient")
    private Set<Consult> consults = new HashSet<Consult>();*/


}

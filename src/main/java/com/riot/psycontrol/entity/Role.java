package com.riot.psycontrol.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "rolename", unique = true)
    private String rolename;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_privilege",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "privilege_id")})
    private List<Privilege> privileges;
}

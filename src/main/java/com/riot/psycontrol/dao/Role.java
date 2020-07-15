package com.riot.psycontrol.dao;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="role")
public class Role {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="rolename")
    private String rolename;


}

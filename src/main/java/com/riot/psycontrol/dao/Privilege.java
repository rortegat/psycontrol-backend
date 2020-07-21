package com.riot.psycontrol.dao;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "privilege")
public class Privilege {
    @Id
    @Column(name = "privilege_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer privilege_id;

    @Column(name = "privilege_name")
    private String privilegename;
}

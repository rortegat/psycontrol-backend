package com.riot.psycontrol.dao;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="file")
public class DaoFile extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="filename")
    private String filename;
    @Column(name="content_type")
    private String contentType;
    @Column(name="size")
    private long size;
    @Column(name="path")
    private String path;

}

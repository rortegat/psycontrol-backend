package com.riot.psycontrol.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "file")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class DaoFile extends Auditable<String> {

    @Id
    @Column
    private String id;
    @Column(name = "filename")
    private String filename;
    @Column(name = "type")
    private String contentType;
    @Column(name = "size")
    private long size;
    @Column(name = "path")
    private String path;

}

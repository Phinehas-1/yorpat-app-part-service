package dev.bigdecimal.yorpat.api.partservice;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "part")
@Data
public class PartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;
    private String partName;
    private String partRole;
    private Long programId;
    @Column(name = "programDesc")
    private String programName;
    private Date programDate;
}

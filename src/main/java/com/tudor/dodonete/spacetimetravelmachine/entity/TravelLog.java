package com.tudor.dodonete.spacetimetravelmachine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@ApiModel(value = "Travel Log Details")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"place", "date"})
})
public class TravelLog implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String place;
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(
            name = "pgi",
            referencedColumnName = "pgi"
    )
    private Person person;
}

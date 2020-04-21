package com.tudor.dodonete.spacetimetravelmachine.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@ApiModel(value = "Travel Log Details")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"pgi", "travelLocation", "travelDate"})
})
@SequenceGenerator(name = "sq_travel_log", initialValue = 50, allocationSize = 100)
public class TravelLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_travel_log")
    private Long id;

    @Size(min = 2, message = "There is no planet with a single letter, that would be just sad")
    @ApiModelProperty(notes = "planet location name should at least be 2 characters long")
    private String travelLocation;

    @Temporal(TemporalType.DATE)
    private Date travelDate;

    @ManyToOne
    @JoinColumn(
            name = "pgi",
            referencedColumnName = "pgi"
    )
    private Person person;
}

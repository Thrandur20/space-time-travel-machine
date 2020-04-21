package com.tudor.dodonete.spacetimetravelmachine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tudor.dodonete.spacetimetravelmachine.customValidation.PGIConstraint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "Person details")
@SequenceGenerator(name = "sq_person", initialValue = 50, allocationSize = 100)
@Entity
public class Person implements Serializable {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_person")
    private Long id;

    @Column(length = 10, unique = true)
    @NaturalId
    @PGIConstraint(message = "the PGI entered does not correspond with the Galactic Federation Regulations")
    @ApiModelProperty(notes = "the Size of the PGI does not correspond with the Galactic Federation Regulations")
    private String pgi;

    @Size(min = 2, message = "the First Name of the galactic resident should have at least 2 characters")
    @ApiModelProperty(notes = "the First Name of the galactic resident should have at least 2 characters")
    private String firstName;

    @Size(min = 2, message = "the Last Name of the galactic resident should have at least 2 characters")
    @ApiModelProperty(notes = "the Last Name of the galactic resident should have at least 2 characters")
    private String lastName;

    @JsonIgnore
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<TravelLog> travelLogList;
}

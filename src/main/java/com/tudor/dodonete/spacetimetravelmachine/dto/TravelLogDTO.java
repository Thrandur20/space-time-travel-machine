package com.tudor.dodonete.spacetimetravelmachine.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Validated
public class TravelLogDTO {
    @Size(min = 2, message = "There is no planet with a single letter, that would be just sad")
    private String travelLocation;
    private Date travelDate;
    private String pgi;

    public TravelLogDTO(String travelLocation, Date travelDate, String pgi) {
        this.travelLocation = travelLocation;
        this.travelDate = travelDate;
        this.pgi = pgi;
    }
}

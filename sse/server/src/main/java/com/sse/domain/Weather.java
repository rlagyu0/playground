package com.sse.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long weatherId;
    private int temperature;
    private int humidity;

    public Weather() {}
    public Weather(int temperature, int humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

}

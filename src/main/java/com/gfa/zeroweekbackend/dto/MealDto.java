package com.gfa.zeroweekbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MealDto {
    private String name;
    private Integer calories;
    private LocalDate date;

    public MealDto(String name, int calories) {
        this.name = name;
        this.calories = calories;
        this.date = LocalDate.now();
    }
}
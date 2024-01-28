package com.gfa.zeroweekbackend.services;

import com.gfa.zeroweekbackend.dto.DateDto;
import com.gfa.zeroweekbackend.dto.MealDto;
import com.gfa.zeroweekbackend.models.Meal;

import java.util.List;

public interface MealService {

    Meal addMeal(MealDto mealDto);

    List<Meal> listMeals();

    Meal updateMeal(Long id, String name, Integer calories);

    boolean deleteMealById(Long id);

    Meal getMeal(Long id);

    int countCaloriesInDay(DateDto dateDto);
}
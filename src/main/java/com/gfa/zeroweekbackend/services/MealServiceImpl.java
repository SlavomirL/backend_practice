package com.gfa.zeroweekbackend.services;

import com.gfa.zeroweekbackend.dto.DateDto;
import com.gfa.zeroweekbackend.dto.MealDto;
import com.gfa.zeroweekbackend.models.Meal;
import com.gfa.zeroweekbackend.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public Meal addMeal(MealDto mealDto) {
        Meal meal = new Meal();
        if(mealDto == null) {
            return null;
        }
        if(mealDto.getName() == null || mealDto.getCalories() == 0) {
            return null;
        }
        meal.setName(mealDto.getName());
        meal.setCalories(mealDto.getCalories());
        meal.setDate(mealDto.getDate());
        mealRepository.save(meal);
        return meal;
    }

    @Override
    public List<Meal> listMeals() {
        return mealRepository.findAll();
    }

    @Override
    public Meal updateMeal(Long id, String name, Integer calories) {
        Optional<Meal> mealOptional = mealRepository.findById(id);
        if(mealOptional.isPresent() && name != null && calories != null) {
            Meal meal = mealOptional.get();
            meal.setName(name);
            meal.setCalories(calories);
            mealRepository.save(meal);
            return meal;
        }
        return null;
    }

    @Override
    public boolean deleteMealById(Long id) {
        Optional<Meal> mealOptional = mealRepository.findById(id);
        if(mealOptional.isPresent()) {
            mealRepository.delete(mealOptional.get());
            return true;
        }
        return false;
    }

    @Override
    public Meal getMeal(Long id) {
        Optional<Meal> mealOptional = mealRepository.findById(id);
        if(mealOptional.isPresent()) {
            return mealOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public int countCaloriesInDay(DateDto dateDto) {
        if(!isValidDateDto(dateDto) || dateDto == null) {
            return -1;
        }
        int calories = 0;
        for (Meal meal : mealRepository.findAll()) {
            LocalDate mealDate = meal.getDate();
            if (mealDate.toString().equals(dateDto.getDate())) {
                calories += meal.getCalories();
            }
        }
        return calories;
    }

    public boolean isValidDateDto(DateDto dateDto) {
        if (dateDto == null || dateDto.getDate() == null) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter.parse(dateDto.getDate());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
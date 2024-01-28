package com.gfa.zeroweekbackend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gfa.zeroweekbackend.dto.DateDto;
import com.gfa.zeroweekbackend.dto.MealDto;
import com.gfa.zeroweekbackend.dto.ResultMessageDto;
import com.gfa.zeroweekbackend.models.Meal;
import com.gfa.zeroweekbackend.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
public class APIMealController {

    private final MealService mealService;

    @Autowired
    public APIMealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping("/api/add")
    public ResponseEntity<?> addMeal(@RequestBody MealDto mealDto) {
        Meal addedMeal = mealService.addMeal(mealDto);
        ResultMessageDto resultMessageDto = new ResultMessageDto();
        if(addedMeal == null) {
            resultMessageDto.setMessage("Please specify name and calories");
            return ResponseEntity.badRequest().body(resultMessageDto);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/api/list-all")
    public ResponseEntity<?> listAllMeals() throws JsonProcessingException {
        List<Meal> meals = mealService.listMeals();
        ResultMessageDto resultMessageDto = new ResultMessageDto();
        if(meals == null) {
            resultMessageDto.setMessage("No meals found");
            return ResponseEntity.ok().body(resultMessageDto);
        } else {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setDateFormat(new SimpleDateFormat("dd-MMM-yyyy"));
            String resultMessage = objectMapper.writeValueAsString(meals);
            return ResponseEntity.ok(resultMessage);
        }
    }

    @PutMapping("/api/update/{id}")
    public ResponseEntity<?> updateMeal(@PathVariable Long id,
                                        @RequestBody MealDto mealDto) {
        ResultMessageDto resultMessageDto = new ResultMessageDto();
        if(mealService.updateMeal(id, mealDto.getName(), mealDto.getCalories()) == null) {
            resultMessageDto.setMessage("meal cannot be updated");
            return ResponseEntity.badRequest().body(resultMessageDto);
        } else {
            resultMessageDto.setMessage("meal updated successfully");
            return ResponseEntity.ok(resultMessageDto);
        }
    }

    @DeleteMapping("api/delete/{id}")
    public ResponseEntity<?> deleteMeal(@PathVariable Long id) {
        ResultMessageDto resultMessageDto = new ResultMessageDto();
        if(mealService.deleteMealById(id)) {
            resultMessageDto.setMessage("meal deleted successfully");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(resultMessageDto);

        } else {
            resultMessageDto.setMessage("deletion failed");
            return ResponseEntity.badRequest().body(resultMessageDto);
        }
    }

    @GetMapping("/api/select/{id}")
    public ResponseEntity<?> selectMeal(@PathVariable Long id) throws JsonProcessingException {
        ResultMessageDto resultMessageDto = new ResultMessageDto();
        Meal meal = mealService.getMeal(id);
        if(meal == null) {
            resultMessageDto.setMessage("Meal with this ID cannot be found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMessageDto);
        } else {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setDateFormat(new SimpleDateFormat("dd-MMM-yyyy"));
            String resultMessage = objectMapper.writeValueAsString(meal);
            return ResponseEntity.ok(resultMessage);
        }
    }

    @GetMapping("/api/calories")
    public ResponseEntity<?> countCaloriesInDay(@RequestBody(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateDto dateDto) {

        ResultMessageDto resultMessageDto = new ResultMessageDto();
        int calories = mealService.countCaloriesInDay(dateDto);
        if(calories == - 1) {
            resultMessageDto.setMessage("Please specify date in format yyyy-MM-dd");
            return ResponseEntity.badRequest().body(resultMessageDto);
        }
        return ResponseEntity.ok(calories);
    }
}
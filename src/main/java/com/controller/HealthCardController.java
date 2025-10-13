package com.controller;

import com.model.HealthCard;
import com.repository.HealthCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthCardController {
    
    @Autowired
    private HealthCardRepository healthCardRepository;
    
    // Save health card data
    @PostMapping("/save")
    public String saveHealthCard(@RequestBody HealthCard healthCard) {
        try {
            healthCardRepository.save(healthCard);
            return "Data saved successfully! ID: " + healthCard.getId();
        } catch (Exception e) {
            return "Error saving data: " + e.getMessage();
        }
    }
    
    // Get all health cards
    @GetMapping("/cards")
    public List<HealthCard> getAllCards() {
        return healthCardRepository.findAll();
    }
    
    // Get health card by ID
    @GetMapping("/card/{id}")
    public HealthCard getCardById(@PathVariable Long id) {
        return healthCardRepository.findById(id).orElse(null);
    }
    
    // Delete health card
    @DeleteMapping("/delete/{id}")
    public String deleteCard(@PathVariable Long id) {
        try {
            healthCardRepository.deleteById(id);
            return "Card deleted successfully!";
        } catch (Exception e) {
            return "Error deleting card: " + e.getMessage();
        }
    }
}
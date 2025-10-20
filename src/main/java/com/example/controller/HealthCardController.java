package com.example.controller;

import com.example.model.HealthCard;
import com.example.repository.HealthCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthCardController {
    
    @Autowired
    private HealthCardRepository healthCardRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    // Save health card data
    @PostMapping("/save")
    public String saveHealthCard(@RequestBody HealthCard healthCard) {
        try {
            // Generate card number if not provided
            if (healthCard.getCardNumber() == null || healthCard.getCardNumber().isEmpty()) {
                String cardNumber = generateCardNumber();
                healthCard.setCardNumber(cardNumber);
            }
            healthCardRepository.save(healthCard);
            return "Data saved successfully! Card Number: " + healthCard.getCardNumber();
        } catch (Exception e) {
            return "Error saving data: " + e.getMessage();
        }
    }
    
    // Generate unique card number
    private String generateCardNumber() {
        long count = healthCardRepository.count();
        return String.format("HC-%06d", count + 1);
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
    
    // Update health card
    @PutMapping("/update/{id}")
    public String updateHealthCard(@PathVariable Long id, @RequestBody HealthCard healthCard) {
        try {
            if (healthCardRepository.existsById(id)) {
                healthCard.setId(id);
                healthCardRepository.save(healthCard);
                return "Data updated successfully! ID: " + id;
            } else {
                return "Error: Health card with ID " + id + " not found!";
            }
        } catch (Exception e) {
            return "Error updating data: " + e.getMessage();
        }
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
    
    // Export single health card as PDF in card shape
    @GetMapping("/export/pdf/{id}")
    public ResponseEntity<byte[]> exportCardToPdf(@PathVariable Long id) {
        try {
            HealthCard card = healthCardRepository.findById(id).orElse(null);
            if (card == null) {
                return ResponseEntity.notFound().build();
            }
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PDDocument document = new PDDocument();
            
            // Create card-sized page (credit card dimensions: 3.375" x 2.125" at 72 DPI)
            float cardWidth = 3.375f * 72;  // 243 points
            float cardHeight = 2.125f * 72; // 153 points
            PDRectangle cardSize = new PDRectangle(cardWidth, cardHeight);
            PDPage page = new PDPage(cardSize);
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Draw card border
            contentStream.setStrokingColor(Color.DARK_GRAY);
            contentStream.setLineWidth(2);
            contentStream.addRect(5, 5, cardWidth - 10, cardHeight - 10);
            contentStream.stroke();
            
            // Header section with background
            contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
            contentStream.addRect(10, cardHeight - 40, cardWidth - 20, 30);
            contentStream.fill();
            
            // Header text
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(15, cardHeight - 32);
            contentStream.showText("HEALTH CARD");
            contentStream.endText();
            
            // Card number (top right)
            String cardNum = card.getCardNumber() != null ? card.getCardNumber() : "HC-" + String.format("%06d", card.getId());
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(cardWidth - 80, cardHeight - 32);
            contentStream.showText(cardNum);
            contentStream.endText();
            
            // Patient name (prominent)
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(15, cardHeight - 55);
            contentStream.showText(card.getName().toUpperCase());
            contentStream.endText();
            
            // Patient details in two columns
            contentStream.setFont(PDType1Font.HELVETICA, 9);
            float leftColumn = 15;
            float rightColumn = cardWidth / 2 + 5;
            float yPos = cardHeight - 75;
            
            // Left column
            addCardLine(contentStream, "AGE: " + card.getAge(), leftColumn, yPos);
            yPos -= 12;
            addCardLine(contentStream, "GENDER: " + card.getGender(), leftColumn, yPos);
            yPos -= 12;
            addCardLine(contentStream, "PHONE: " + card.getPhone(), leftColumn, yPos);
            
            // Right column
            yPos = cardHeight - 75;
            addCardLine(contentStream, "EMAIL:", rightColumn, yPos);
            yPos -= 8;
            contentStream.setFont(PDType1Font.HELVETICA, 7);
            addCardLine(contentStream, card.getEmail(), rightColumn, yPos);
            yPos -= 12;
            
            contentStream.setFont(PDType1Font.HELVETICA, 9);
            
            // Address at bottom (smaller font)
            contentStream.setFont(PDType1Font.HELVETICA, 7);
            addCardLine(contentStream, "ADDRESS: " + card.getAddress(), 15, 25);
            
            // Footer
            contentStream.setFont(PDType1Font.HELVETICA, 6);
            addCardLine(contentStream, "Generated: " + java.time.LocalDateTime.now().toString().substring(0, 16), 15, 10);
            
            contentStream.close();
            document.save(baos);
            document.close();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "health_card_" + card.getId() + ".pdf");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(baos.toByteArray());
                
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Refresh table numbering - reset card numbers sequentially
    @PostMapping("/refresh-numbering")
    public String refreshTableNumbering() {
        try {
            List<HealthCard> allCards = healthCardRepository.findAll();
            
            // Sort by ID to maintain order
            allCards.sort((a, b) -> Long.compare(a.getId(), b.getId()));
            
            // Regenerate card numbers sequentially
            for (int i = 0; i < allCards.size(); i++) {
                HealthCard card = allCards.get(i);
                String newCardNumber = String.format("HC-%06d", i + 1);
                card.setCardNumber(newCardNumber);
                healthCardRepository.save(card);
            }
            
            return "Table numbering refreshed successfully! " + allCards.size() + " cards renumbered.";
        } catch (Exception e) {
            return "Error refreshing table numbering: " + e.getMessage();
        }
    }
    
    
    // Delete all health cards and reset numbering
    @DeleteMapping("/reset-all")
    @Transactional
    public String resetAllCards() {
        try {
            // Delete all records
            healthCardRepository.deleteAll();
            
            // Reset auto-increment to 1 after deleting all records
            entityManager.createNativeQuery("ALTER TABLE health_cards AUTO_INCREMENT = 1").executeUpdate();
            
            // Flush to ensure changes are committed
            entityManager.flush();
            entityManager.clear();
            
            return "✅ Database completely cleared! All health cards deleted and ID counter reset to 1. Ready for fresh start!";
        } catch (Exception e) {
            return "❌ Error clearing database: " + e.getMessage();
        }
    }
    
    // Get database status for verification
    @GetMapping("/database-status")
    public String getDatabaseStatus() {
        try {
            long count = healthCardRepository.count();
            return "Database Status: " + count + " records found. " + 
                   (count == 0 ? "✅ Database is clean and ready!" : "📊 Database contains data.");
        } catch (Exception e) {
            return "❌ Error checking database status: " + e.getMessage();
        }
    }
    
    // Helper method to add text lines to card PDF
    private void addCardLine(PDPageContentStream contentStream, String text, float x, float y) throws Exception {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
}

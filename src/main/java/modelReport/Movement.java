/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelReport;

import java.time.LocalDateTime;

/**
 *
 * @author villa
 */
public class Movement {

    private int idMovement;
    private LocalDateTime date;
    private String categoryType;   
    private double amount;
    private String description;
    private String categoryName;

    public Movement(int idMovement, LocalDateTime date, String categoryType,
                    double amount, String description, String categoryName) {

        this.idMovement = idMovement;
        this.date = date;
        this.categoryType = categoryType;
        this.amount = amount;
        this.description = description;
        this.categoryName = categoryName;
    }

    public int getIdMovement() { return idMovement; }
    public LocalDateTime getDate() { return date; }
    public String getCategoryType() { return categoryType; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getCategoryName() { return categoryName; }
}
package com.example.GoalsModule.Model;
import java.math.BigDecimal;

/**
 * Represents a financial target within a user account.
 * @author Jose Pablo Martinez
 * @version 1.0
 */

public class Goal {

    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private String description;

    public Goal() {
        this.currentAmount = BigDecimal.ZERO;
    }

    public Goal(String name, BigDecimal targetAmount, String description) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.description = description;
        this.currentAmount = BigDecimal.ZERO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " - Target: " + targetAmount;
    }
}
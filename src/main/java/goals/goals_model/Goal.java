package goals.goals_model;

import java.math.BigDecimal;

/**
 * Represents a financial goal within a user account.
 * 
 * @author Jose Pablo Martinez
 * @version 1.0
 */

public class Goal {

    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private String description;
    private boolean notificadaCompleta;

    public Goal() {
        this.currentAmount = BigDecimal.ZERO;
        this.notificadaCompleta = false;
    }

    public Goal(String name, BigDecimal targetAmount, String description) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.description = description;
        this.currentAmount = BigDecimal.ZERO;
        this.notificadaCompleta = false;
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

    public boolean isNotificadaCompleta() {
        return notificadaCompleta;
    }

    public void setNotificadaCompleta(boolean notificadaCompleta) {
        this.notificadaCompleta = notificadaCompleta;
    }

    @Override
    public String toString() {
        return name + " - Target: " + targetAmount;
    }
}
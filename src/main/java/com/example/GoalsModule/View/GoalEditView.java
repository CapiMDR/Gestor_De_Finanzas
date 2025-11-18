package com.example.GoalsModule.View;

import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * This class serves as a temporary substitute for the actual Swing/GUI view
 * (GoalEditView). Its purpose is to define the public contract (methods)
 * that the GoalsController interacts with, allowing the Controller logic
 * to be developed, compiled, and unit-tested without depending on the
 * graphical user interface implementation.
 * It does not contain any GUI elements (like JDialog or JTextFields).
 *
 * @author Jose Pablo Martinez
 */

public class GoalEditView {

    //Dummy constructor
    public GoalEditView() {
        System.out.println("DEBUG: GoalEditView Stub Initialized.");
    }


    public void populateFields(String name, BigDecimal targetAmount, String description) {
    }

 
    public void addSaveListener(ActionListener listener) {
    }


    public String getEditedName() {
        return "Dummy Edited Name";
    }

    public BigDecimal getEditedTarget() {
        return new BigDecimal("5000.00"); //dummy value
    }


    public String getEditedDescription() {
        return "Dummy Edited Description";
    }

    //Simulates showing the dialog/window to the user

    public void showDialog() {
        System.out.println("DEBUG: GoalEditView shown.");
    }

    //Simulares closinf the dialog
    public void closeDialog() {
        System.out.println("DEBUG: GoalEditView closed.");
    }
}
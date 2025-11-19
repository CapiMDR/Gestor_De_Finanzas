package reminder_view;

import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.TreeSet;

import reminder_controller.RemindersController;

import reminder_model.ReminderObserver;
import reminder_model.Reminder;
import reminder_model.RemindersModel;

public class RemindersView implements ReminderObserver {
    private Scanner scanner = new Scanner(System.in);
    private final RemindersModel model;
    private final RemindersController controller;

    public RemindersView(RemindersController controller, RemindersModel model) {
        this.model = model;
        this.controller = controller;
        this.model.addObserver(this);
    }

    public void showMenu() {
        int option = 0;

        while (option != -1) {
            refreshConsole();

            System.out.println();
            System.out.println("Ingrese una opción:");
            System.out.println("1 - Crear recordatorio");
            System.out.println("2 - Eliminar recordatorio");
            System.out.println("3 - Editar recordatorio");
            System.out.println("-1 - Salir");
            System.out.print("Opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                option = 0;
            }

            switch (option) {
                case 1:
                    createReminder();
                    break;
                case 2:
                    deleteReminder();
                    break;
                case 3:
                    editReminder();
                    break;
                case -1:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
    }

    public void observe() {
        refreshConsole();
    }

    public void addActionListener(ActionListener listener) {
        // TODO: Agregar listeners a los botones de esta vista cuando los haya
    }

    private void createReminder() {
        System.out.print("Nombre: ");
        String name = scanner.nextLine();

        System.out.print("Mensaje: ");
        String message = scanner.nextLine();

        System.out.print("Fecha (dd/MM/yyyy HH:mm) (-1 para omitir): ");
        String dateText = scanner.nextLine();

        if (!dateText.equals("-1")) {
            controller.handleReminderAddition(name, message, dateText);
        } else {
            controller.handleReminderAddition(name, message);
        }

    }

    private void deleteReminder() {
    }

    private void editReminder() {
    }

    private void refreshConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("-------- LISTA DE RECORDATORIOS --------");

        // La vista puede acceder a los datos del modelo sin pasar por el controlador
        TreeSet<Reminder> reminders = model.getReminders();

        if (reminders.isEmpty()) {
            System.out.println("(No hay recordatorios registrados)");
            return;
        }

        int i = 1;
        for (Reminder r : reminders) {
            System.out.println(i + ". " + r);
            i++;
        }
    }
}

package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;

import java.util.Scanner;

public class TextMenu {
    private IStmt selected = null;

    public void run() {
        Scanner keyboard = new Scanner(System.in);
        while (true) {
            System.out.println("1. Select a program");
            System.out.println("2. Run the selected program");
            System.out.println("0. Exit");
            System.out.print("Option: ");
            if (!keyboard.hasNextLine())
                return;
            String option = keyboard.nextLine().strip();
            switch (option) {
                case "1" -> selectProgram(keyboard);
                case "2" -> runProgram();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void selectProgram(Scanner keyboard) {
        for (int i = 0; i < Examples.ALL.length; i++)
            System.out.println((i + 1) + ": " + Examples.ALL[i]);
        System.out.print("Program number: ");
        String text = keyboard.nextLine().strip();
        try {
            int n = Integer.parseInt(text);
            if (n < 1 || n > Examples.ALL.length) {
                System.out.println("There is no program " + n);
                return;
            }
            selected = Examples.ALL[n - 1];
            System.out.println("Selected: " + selected);
        } catch (NumberFormatException e) {
            System.out.println("Please type a number");
        }
    }

    private void runProgram() {
        if (selected == null) {
            System.out.println("Select a program first");
            return;
        }
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), selected);
        Controller controller = new Controller(new Repository(state), true);
        try {
            controller.allStep();
        } catch (MyException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

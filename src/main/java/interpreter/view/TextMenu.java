package interpreter.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

// Lab 6 (A3): the menu is built from Command objects (it replaces the menu of A2)
public class TextMenu {
    private final Map<String, Command> commands = new LinkedHashMap<>();   // keeps the order

    // Lab 6 (A3): adds one option to the menu
    public void addCommand(Command c) {
        commands.put(c.getKey(), c);
    }

    // Lab 6 (A3): prints all the options, one per line
    private void printMenu() {
        for (Command c : commands.values())
            System.out.printf("%4s : %s%n", c.getKey(), c.getDescription());
    }

    // Lab 6 (A3): shows the menu again and again, reads an option and executes its command
    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Input the option: ");
            if (!scanner.hasNextLine())
                return;
            Command c = commands.get(scanner.nextLine().strip());
            if (c == null) {
                System.out.println("Invalid option");
                continue;
            }
            c.execute();
        }
    }
}
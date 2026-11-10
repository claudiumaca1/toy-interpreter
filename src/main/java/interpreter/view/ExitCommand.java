package interpreter.view;

// Lab 6 (A3): the menu option that ends the program
public class ExitCommand extends Command {
    public ExitCommand(String key, String description) {
        super(key, description);
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}
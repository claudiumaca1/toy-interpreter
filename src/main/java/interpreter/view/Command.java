package interpreter.view;

// Lab 6 (A3): a menu option (Command pattern): it has a key, a description and an execute method
public abstract class Command {
    private final String key;
    private final String description;

    protected Command(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public abstract void execute();

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
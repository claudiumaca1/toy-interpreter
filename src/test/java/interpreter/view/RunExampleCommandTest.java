package interpreter.view;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// Lab 6 (A3): tests for the command classes of the menu
class RunExampleCommandTest {
    @TempDir
    Path dir;

    @Test
    void keyAndDescriptionAreStored() {
        Command c = new ExitCommand("0", "exit");
        assertEquals("0", c.getKey());
        assertEquals("exit", c.getDescription());
    }

    @Test
    void anExampleRunsOnlyOnce() throws Exception {
        PrgState state = PrgState.of(Examples.EX1);
        var controller = new Controller(new Repository(state, dir.resolve("log.txt").toString()), false);
        Command c = new RunExampleCommand("1", "ex1", controller);
        PrintStream old = System.out;
        var captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            c.execute();
            c.execute();                       // the second time nothing is executed
        } finally {
            System.setOut(old);
        }
        assertEquals(1, state.getOut().size());
        assertTrue(captured.toString().contains("already been executed"));
    }

    @Test
    void anErrorOfTheProgramIsShownNotThrown() {
        PrgState state = PrgState.of(Examples.fileExample(dir.resolve("missing.in").toString()));
        var controller = new Controller(new Repository(state, dir.resolve("log.txt").toString()), false);
        PrintStream old = System.out;
        var captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            new RunExampleCommand("4", "ex4", controller).execute();
        } finally {
            System.setOut(old);
        }
        assertTrue(captured.toString().startsWith("Error: openRFile"));
    }
}
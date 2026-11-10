package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> states = new ArrayList<>();
    private final String logFilePath;                        // Lab 6 (A3): the log file

    // Lab 6 (A3): the constructor now also gets the path of the log file
    public Repository(PrgState state, String logFilePath) {
        states.add(state);
        this.logFilePath = logFilePath;
    }

    @Override
    public PrgState getCrtPrg() {
        return states.getFirst();
    }

    // Lab 6 (A3): writes the current state at the end of the log file (append mode). The file
    // is opened and closed at every call (try-with-resources), so nothing is lost on an error.
    @Override
    public void logPrgStateExec() throws MyException {
        try (var logFile = new PrintWriter(
                new BufferedWriter(new FileWriter(logFilePath, true)))) {   // true = append
            logFile.println(getCrtPrg().toLogString());
        } catch (IOException e) {
            throw new RepositoryException("cannot write the log file " + logFilePath
                    + ": " + e.getMessage());
        }
    }
}
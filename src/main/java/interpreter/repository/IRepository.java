package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

public interface IRepository {
    PrgState getCrtPrg();

    // Lab 6 (A3): appends the current program state to the log file
    void logPrgStateExec() throws MyException;
}
package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.StringValue;

import java.io.IOException;

// Lab 5 (A3), rule S10
public record CloseRFileStmt(IExp exp) implements IStmt {
    // Lab 5 (A3): closes the reader of the file given by exp and removes it from the FileTable
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(exp.eval(state.getSymTable()) instanceof StringValue fileName))
            throw new StatementException("closeRFile: " + exp + " is not a string");
        var fileTable = state.getFileTable();
        if (!fileTable.isDefined(fileName))
            throw new StatementException("closeRFile: " + fileName + " is not open");
        try {
            fileTable.lookup(fileName).close();
        } catch (IOException e) {
            throw new StatementException("closeRFile: cannot close " + fileName
                    + ": " + e.getMessage());
        }
        fileTable.remove(fileName);
        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}
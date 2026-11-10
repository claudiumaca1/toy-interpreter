package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Lab 5 (A3), rule S8
public record OpenRFileStmt(IExp exp) implements IStmt {
    // Lab 5 (A3): evaluates exp (must be a string), opens that file with a BufferedReader
    // and adds fileName -> reader to the FileTable
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(exp.eval(state.getSymTable()) instanceof StringValue fileName))
            throw new StatementException("openRFile: " + exp + " is not a string");
        var fileTable = state.getFileTable();
        if (fileTable.isDefined(fileName))
            throw new StatementException("openRFile: " + fileName + " is already open");
        try {
            // no try-with-resources: the file must stay open until closeRFile
            var reader = new BufferedReader(new FileReader(fileName.val()));
            fileTable.put(fileName, reader);
        } catch (IOException e) {
            throw new StatementException("openRFile: cannot open " + fileName
                    + ": " + e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp + ")";
    }
}
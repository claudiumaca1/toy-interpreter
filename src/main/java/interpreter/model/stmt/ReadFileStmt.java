package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

// Lab 5 (A3), rule S9
public record ReadFileStmt(IExp exp, String varName) implements IStmt {
    // Lab 5 (A3): reads the next line of the file given by exp and stores it as an int in
    // varName (0 at the end of the file). Java exceptions become StatementExceptions.
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(varName))
            throw new StatementException("readFile: " + varName + " is not declared");
        if (!(symTable.lookup(varName) instanceof IntValue))
            throw new StatementException("readFile: " + varName + " is not of type int");
        if (!(exp.eval(symTable) instanceof StringValue fileName))
            throw new StatementException("readFile: " + exp + " is not a string");
        var fileTable = state.getFileTable();
        if (!fileTable.isDefined(fileName))
            throw new StatementException("readFile: " + fileName + " is not open");
        BufferedReader reader = fileTable.lookup(fileName);
        try {
            String line = reader.readLine();
            int n = 0;                       // end of file (null) gives 0
            if (line != null)
                n = Integer.parseInt(line.strip());
            symTable.update(varName, new IntValue(n));
        } catch (IOException e) {
            throw new StatementException("readFile: cannot read " + fileName
                    + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new StatementException("readFile: a line of " + fileName
                    + " is not an integer");
        }
        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}
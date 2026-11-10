package interpreter.model;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;   // Lab 5 (A3): the open files
    private final IStmt originalProgram;

    // Lab 5 (A3): the constructor now also gets the FileTable
    public PrgState(IStack<IStmt> exeStack, IDictionary<String, IValue> symTable,
                    IList<IValue> out,
                    IDictionary<StringValue, BufferedReader> fileTable,
                    IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.originalProgram = program;
        exeStack.push(program);
    }

    // Lab 5 (A3): creates a state with empty structures; use it instead of "new PrgState(...)"
    public static PrgState of(IStmt program) {
        return new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),
                new MyDictionary<>(), program);
    }

    public IStack<IStmt> getExeStack() {
        return exeStack;
    }

    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    // Lab 5 (A3): getter for the FileTable
    public IDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    // Lab 5 (A3): setter for the FileTable
    public void setFileTable(IDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    // Lab 5 (A3): changed - also shows the names of the open files
    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\nSymTable: " + symTable + "\nOut: " + out
                + "\nFileTable: " + fileTable.toMap().keySet() + "\n";
    }
}
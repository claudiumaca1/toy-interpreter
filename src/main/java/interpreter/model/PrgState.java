package interpreter.model;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.util.TreeMap;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;   // Lab 5 (A3): the open files
    private IHeap heap;                                            // Lab 7 (A4): the heap
    private final IStmt originalProgram;

    // Lab 5 (A3): the constructor now also gets the FileTable
    // Lab 7 (A4): ... and the Heap
    public PrgState(IStack<IStmt> exeStack, IDictionary<String, IValue> symTable,
                    IList<IValue> out,
                    IDictionary<StringValue, BufferedReader> fileTable,
                    IHeap heap,
                    IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = program;
        exeStack.push(program);
    }

    // Lab 5 (A3): creates a state with empty structures; use it instead of "new PrgState(...)"
    // Lab 7 (A4): changed - the new state also gets an empty heap
    public static PrgState of(IStmt program) {
        return new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),
                new MyDictionary<>(), new MyHeap(), program);
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

    // Lab 7 (A4): getter for the Heap
    public IHeap getHeap() {
        return heap;
    }

    // Lab 7 (A4): setter for the Heap
    public void setHeap(IHeap heap) {
        this.heap = heap;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    // Lab 5 (A3): changed - also shows the names of the open files
    // Lab 7 (A4): changed - also shows the heap
    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\nSymTable: " + symTable + "\nOut: " + out
                + "\nFileTable: " + fileTable.toMap().keySet() + "\nHeap: " + heap + "\n";
    }

    // Lab 6 (A3): the text written in the log file - the sections ExeStack, SymTable, Out and
    // FileTable, each element on its own line (the top of the stack first)
    // Lab 7 (A4): changed - a fifth section Heap, one "address --> value" line per location,
    // sorted by address (TreeMap) so that two logs of the same program are identical
    public String toLogString() {
        var sb = new StringBuilder();
        sb.append("ExeStack:\n");
        exeStack.toList().forEach(s -> sb.append(s).append('\n'));
        sb.append("SymTable:\n");
        symTable.toMap().forEach((k, v) ->
                sb.append(k).append(" --> ").append(v).append('\n'));
        sb.append("Out:\n");
        out.toList().forEach(v -> sb.append(v).append('\n'));
        sb.append("FileTable:\n");
        fileTable.toMap().keySet().forEach(f -> sb.append(f.val()).append('\n'));
        sb.append("Heap:\n");
        new TreeMap<>(heap.getContent()).forEach((a, v) ->
                sb.append(a).append(" --> ").append(v).append('\n'));
        return sb.toString();
    }
}
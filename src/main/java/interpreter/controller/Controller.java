package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

import static interpreter.controller.GarbageCollector.getAddrFromSymTable;
import static interpreter.controller.GarbageCollector.safeGarbageCollector;

public class Controller {
    private final IRepository repo;
    private final boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public PrgState oneStep(PrgState state) throws MyException {
        var stack = state.getExeStack();
        if (stack.isEmpty())
            throw new StatementException("execution stack is empty");
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    // Lab 6 (A3): changed - the state is also written in the log file, at the beginning
    // and after every step
    // Lab 7 (A4): changed - after every step the garbage collector removes the unreachable
    // heap locations; the state is logged before and after the collection
    // Lab 8 (A4): changed - the safe collector replaces the unsafe one (Ex9 now prints 20)
    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();
        repo.logPrgStateExec();
        display(prg);
        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            repo.logPrgStateExec();
            prg.getHeap().setContent(safeGarbageCollector(  // Lab 8 (A4): the safe collector
                    getAddrFromSymTable(prg.getSymTable().toMap().values()),
                    prg.getHeap().getContent()));
            repo.logPrgStateExec();
            display(prg);
        }
    }

    private void display(PrgState prg) {
        if (displayFlag)
            System.out.println(prg);
    }
}
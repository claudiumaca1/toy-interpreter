package interpreter.repository;

import interpreter.model.PrgState;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> states = new ArrayList<>();

    public Repository(PrgState state) {
        states.add(state);
    }

    @Override
    public PrgState getCrtPrg() {
        return states.get(0);
    }
}

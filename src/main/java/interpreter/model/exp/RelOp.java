package interpreter.model.exp;

// Lab 5 (A3): the six relational operators <, <=, ==, !=, >, >=
public enum RelOp {
    LT("<"), LE("<="), EQ("=="), NE("!="), GT(">"), GE(">=");

    private final String symbol;

    RelOp(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
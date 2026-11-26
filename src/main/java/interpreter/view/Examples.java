package interpreter.view;

import interpreter.model.exp.*;
import interpreter.model.stmt.*;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.type.StringType;
import interpreter.model.value.StringValue;

public class Examples {
    // Ex1: int v; v = 2; print(v)
    public static final IStmt EX1 = new CompStmt(new VarDeclStmt("v", new IntType()),
            new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                    new PrintStmt(new VarExp("v"))));

    // Ex2: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)
    public static final IStmt EX2 = new CompStmt(new VarDeclStmt("a", new IntType()),
            new CompStmt(new VarDeclStmt("b", new IntType()),
                    new CompStmt(new AssignStmt("a",
                            new ArithExp(ArithOp.ADD, new ValueExp(new IntValue(2)),
                                    new ArithExp(ArithOp.MUL, new ValueExp(new IntValue(3)),
                                            new ValueExp(new IntValue(5))))),
                            new CompStmt(new AssignStmt("b",
                                    new ArithExp(ArithOp.ADD, new VarExp("a"),
                                            new ValueExp(new IntValue(1)))),
                                    new PrintStmt(new VarExp("b"))))));

    // Ex3: bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)
    public static final IStmt EX3 = new CompStmt(new VarDeclStmt("a", new BoolType()),
            new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                            new CompStmt(new IfStmt(new VarExp("a"),
                                    new AssignStmt("v", new ValueExp(new IntValue(2))),
                                    new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                    new PrintStmt(new VarExp("v"))))));

    // Lab 6 (A3): the file example with the name of the file as parameter, so that the tests
    // can run it on temporary files
    // string varf; varf = "<fileName>"; openRFile(varf); int varc; readFile(varf, varc);
    // print(varc); readFile(varf, varc); print(varc); closeRFile(varf)
    public static IStmt fileExample(String fileName) {
        return new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue(fileName))),
                        new CompStmt(new OpenRFileStmt(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFileStmt(new VarExp("varf"))))))))));
    }

    // Lab 6 (A3): Ex4 - the file example on the file test.in (it prints 15, 50)
    public static final IStmt EX4 = fileExample("test.in");

    // Lab 6 (A3): Ex5: int a; int b; a = 5; b = 7; (if a < b then print(a) else print(b))
    public static final IStmt EX5 = new CompStmt(new VarDeclStmt("a", new IntType()),
            new CompStmt(new VarDeclStmt("b", new IntType()),
                    new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(5))),
                            new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(7))),
                                    new IfStmt(new RelationalExp(RelOp.LT, new VarExp("a"), new VarExp("b")),
                                            new PrintStmt(new VarExp("a")),
                                            new PrintStmt(new VarExp("b")))))));

    // Lab 7 (A4): Ex6: Ref int v; new(v, 20); Ref Ref int a; new(a, v); print(v); print(a)
    // at the end: Heap {1 -> 20, 2 -> (1, int)}, Out {(1, int), (2, Ref(int))}
    public static final IStmt EX6 =
            new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                    new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                            new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                    new CompStmt(new NewStmt("a", new VarExp("v")),
                                            new CompStmt(new PrintStmt(new VarExp("v")),
                                                    new PrintStmt(new VarExp("a")))))));

    // Lab 7 (A4): Ex7: Ref int v; new(v, 20); Ref Ref int a; new(a, v);
    // print(rH(v)); print(rH(rH(a)) + 5)
    // at the end: Heap and SymTable as in Ex6, Out {20, 25}
    public static final IStmt EX7 =
            new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                    new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                            new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                    new CompStmt(new NewStmt("a", new VarExp("v")),
                                            new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                    new PrintStmt(new ArithExp(ArithOp.ADD,
                                                            new ReadHeapExp(new ReadHeapExp(new VarExp("a"))),
                                                            new ValueExp(new IntValue(5)))))))));

    // Lab 7 (A4): Ex8: Ref int v; new(v, 20); print(rH(v)); wH(v, 30); print(rH(v) + 5)
    // at the end: Heap {1 -> 30}, SymTable {v -> (1, int)}, Out {20, 35}
    public static final IStmt EX8 =
            new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                    new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                            new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                    new CompStmt(new WriteHeapStmt("v", new ValueExp(new IntValue(30))),
                                            new PrintStmt(new ArithExp(ArithOp.ADD,
                                                    new ReadHeapExp(new VarExp("v")),
                                                    new ValueExp(new IntValue(5))))))));

    // Lab 7 (A4): Ex9: Ref int v; new(v, 20); Ref Ref int a; new(a, v); new(v, 30);
    // print(rH(rH(a)))
    // at the end: Out {20} - but only with a correct (safe) garbage collector
    public static final IStmt EX9 =
            new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                    new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                            new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                    new CompStmt(new NewStmt("a", new VarExp("v")),
                                            new CompStmt(new NewStmt("v", new ValueExp(new IntValue(30))),
                                                    new PrintStmt(new ReadHeapExp(new ReadHeapExp(new VarExp("a")))))))));

    // Lab 7 (A4): Ex10: int v; v = 4; (while (v > 0) (print(v); v = v - 1)); print(v)
    // at the end: Out {4, 3, 2, 1, 0}
    public static final IStmt EX10 =
            new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(4))),
                            new CompStmt(new WhileStmt(
                                    new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                                    new CompStmt(new PrintStmt(new VarExp("v")),
                                            new AssignStmt("v", new ArithExp(ArithOp.SUB,
                                                    new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                    new PrintStmt(new VarExp("v")))));
}
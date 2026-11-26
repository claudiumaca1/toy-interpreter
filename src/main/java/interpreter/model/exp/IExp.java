package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.IValue;

public interface IExp {
    // Lab 7 (A4): changed - eval also gets the heap, because rH reads it
    IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException;
}
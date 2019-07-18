package org.deeplearning4j.classes;

public interface SomeIterator {
    void casualMethod();

    boolean hasNext();

    // i just dont have DataSet announced at this level
    Object next();

    <T> Double transform(T t);
}

package org.deeplearning4j.classes;

import lombok.val;

public class UseClass {
    public static void main(String[] args) throws Exception {
        SomeIterator iterator = new IteratorImpl();
        val string = "my string";
        iterator.transform(string);
    }
}

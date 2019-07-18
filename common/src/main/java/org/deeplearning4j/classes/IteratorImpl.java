package org.deeplearning4j.classes;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.File;

@Slf4j
public class IteratorImpl implements SomeIterator {
    @Override
    public void casualMethod() {

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }

    @Override
    public <T> Double transform(T t) {
        if (t instanceof File) {
            log.info("T path: {}", ((File) t).getAbsolutePath());
        } else if (t instanceof String) {
            log.info("S content: {}", ((String) t));
        }
        return null;
    }

}

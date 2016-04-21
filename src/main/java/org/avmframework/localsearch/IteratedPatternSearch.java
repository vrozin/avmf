package org.avmframework.localsearch;

import org.avmframework.TerminationException;
import org.avmframework.objective.ObjectiveValue;

public class IteratedPatternSearch extends PatternSearch {

    protected void performSearch() throws TerminationException {
        ObjectiveValue next = objFun.evaluate(vector), last;

        do {
            initialize();
            if (establishDirection()) {
                patternSearch();
            }

            last = next;
            next = objFun.evaluate(vector);
        } while (next.betterThan(last));
    }
}
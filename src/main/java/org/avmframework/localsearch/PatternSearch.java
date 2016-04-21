package org.avmframework.localsearch;

import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.TerminationException;
import org.avmframework.objective.ObjectiveValue;

public class PatternSearch extends LocalSearch {

    protected RandomGenerator rg = null;

    protected ObjectiveValue initial, last, next;
    protected int k, x, dir, lastX, nextX;

    public PatternSearch() {
    }

    public PatternSearch(RandomGenerator rg) {
        this.rg = rg;
    }

    protected void performSearch() throws TerminationException {
        initialize();
        if (establishDirection()) {
            patternSearch();
        }
    }

    protected void initialize() throws TerminationException {
        initial = objFun.evaluate(vector);
        k = 1;
        x = var.getValue();
        dir = 0;
    }

    protected boolean establishDirection() throws TerminationException {
        // evaluate left move
        var.setValue(x - k);
        ObjectiveValue left = objFun.evaluate(vector);

        // evaluate right move
        var.setValue(x + k);
        ObjectiveValue right = objFun.evaluate(vector);

        // find the best direction
        boolean leftBetter = left.betterThan(initial);
        boolean rightBetter = right.betterThan(initial);
        if (leftBetter && rightBetter) {
            if (rg == null) {
                dir = -1;
            } else {
                dir = rg.nextBoolean() ? -1 : 1;
            }
        } else if (leftBetter) {
            dir = -1;
        } else if (rightBetter) {
            dir = 1;
        } else {
            dir = 0;
        }

        // set x and the variable according to the best outcome
        x += dir * k;
        var.setValue(x);

        // set last and next objective values
        last = initial;
        if (dir == -1) {
            next = left;
        } else if (dir == 1) {
            next = right;
        } else if (dir == 0) {
            next = initial;
        }

        return dir != 0;
    }

    protected void patternSearch() throws TerminationException {

        while (next.betterThan(last)) {
            last = next;

            // make the pattern move
            k *= var.getAccelerationFactor();
            x += k * dir;
            var.setValue(x);

            // evaluate the move
            next = objFun.evaluate(vector);

            // if no better, reset x and the variable
            if (!next.betterThan(last)) {
                x -= k * dir;
                var.setValue(x);
            }
        }
    }
}

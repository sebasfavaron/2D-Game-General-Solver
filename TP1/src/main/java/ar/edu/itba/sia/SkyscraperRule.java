package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.awt.*;
import java.util.Optional;

public class SkyscraperRule implements Rule {

    private Point cell1, cell2;

    public SkyscraperRule(Point cell1, Point cell2) {
        this.cell1 = cell1;
        this.cell2 = cell2;
    }

    /**
     * Provides the cost of the rule.
     *
     * @return The cost of the rule.
     */
    public Integer getCost() {
        return 8;
    }

    /**
     * Provides the name of the rule so it can be printed on the solution representation.
     *
     * @return The name of the rule.
     */
    public String getName() {
        return "Swap [" + cell1.x + ", " + cell1.y  + "] with [" + cell2.x + ", " + cell2.y + "]";
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Computes the next state based on the previous one. The next state is the result of applying the rule to the previous state.
     *
     * @param state
     *            The previous state of the problem.
     * @return The next state of the problem, or NULL if it can't be applied to such state.
     */
    public Optional<State> apply(State state) {
        SkyscraperState skyscraperState = (SkyscraperState) state;
        return Optional.of(skyscraperState.swap(cell1, cell2));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SkyscraperRule rule = (SkyscraperRule) obj;
        return (rule.cell1.equals(this.cell1) && rule.cell2.equals(this.cell2)) || (rule.cell1.equals(this.cell2) && rule.cell2.equals(this.cell1));
    }
}


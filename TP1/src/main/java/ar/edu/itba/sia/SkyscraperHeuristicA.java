package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

public class SkyscraperHeuristicA implements Heuristic {

    /**
    * Computes the value of the Heuristic for the given state.
    *
    * @param state
    *            The state where the Heuristic should be computed.
    * @return The value of the Heuristic.
    */
    @Override
    public Integer getValue(State state) {
        SkyscraperState s = (SkyscraperState) state;
        return s.valueOfHeuristicA();
    }
}

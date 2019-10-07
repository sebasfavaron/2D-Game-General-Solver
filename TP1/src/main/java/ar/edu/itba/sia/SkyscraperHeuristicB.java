package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.State;

public class SkyscraperHeuristicB implements Heuristic {
    @Override
    public Integer getValue(State state) {
        SkyscraperState s = (SkyscraperState) state;

        return s.valueOfHeruisticB() ;
    }
}

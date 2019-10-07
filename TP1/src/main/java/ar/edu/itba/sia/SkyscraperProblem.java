package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.awt.*;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

public class SkyscraperProblem implements Problem {

    /* Using builder pattern to initialize the Singleton */

    public static class Builder {
        private Restrictions restrictions;
        private int boardSize;
        private Optional<SkyscraperState> initialState = Optional.empty();
        private  boolean addition = false;

        public Builder withBoardSize(int boardSize) {
            this.boardSize = boardSize;
            return this;
        }

        public Builder withRestrictions(Restrictions restrictions) {
            this.restrictions = restrictions;
            return this;
        }

        public Builder startingFromState(SkyscraperState initialState) {
            this.initialState = Optional.of(initialState);
            return this;
        }
        public Builder withAdditionRules(){
            this.addition = true;
            return this;
        }


        public SkyscraperProblem build() {
            if(boardSize == 0 || restrictions == null) {
                throw new IllegalStateException("Missing board size, or restrictions");
            }

            SkyscraperProblem problem = new SkyscraperProblem(boardSize, restrictions);
            initialState.ifPresent(state -> problem.initialState = state);
            SkyscraperProblem.singleInstance = problem;
            if(addition){
                problem.rules = problem.generateRulesAddition();
                problem.initialState = new SkyscraperState(Board.emptyBoard(boardSize));
            }
            return problem;
        }

    }


    /* Internal problem properties */

    private final Restrictions restrictions;
    private final int boardSize;
    private List<Rule> rules;
    private SkyscraperState initialState;


    /* Singleton instance */

    private static SkyscraperProblem singleInstance = null;


    /* Singleton constructor */

    private SkyscraperProblem(int boardSize, Restrictions restrictions) {
        this.boardSize = boardSize;
        this.rules = generateRules(boardSize);
        this.restrictions = restrictions;
        this.initialState = new SkyscraperState(boardSize);
    }

    public static SkyscraperProblem getInstance() {
        if(singleInstance == null) {
            throw new IllegalStateException("Instance not created yet");
        }

        return singleInstance;
    }


    /**
     * Provides the initial state for the GPS to start from.
     *
     * @return The initial state of the problem to be solved.
     */
    public State getInitState() {
        return initialState;
    }



    /**
     * Given a state, resolves if it is a solution to the problem.
     *
     * @param state The state to establish if it is a goal state.
     * @return TRUE if the state is a goal state, FALSE otherwise.
     */

    public boolean isGoal(State state){
        SkyscraperState skyscraperState = (SkyscraperState) state;
        return skyscraperState.satisfiesRestrictions();
    }


    /**
     * Provides the list of all the rules that the problem involves. These rules are state independent.
     *
     * @return The initial state of the problem to be solved.
     */
    public List<Rule> getRules() {
        return rules;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }


    private List<Rule> generateRules(int boardSize) {
        List<Rule> rules = new ArrayList<>();

        //Legacy rule generation that generates every horizontal swap possible (gave an impossibly wide tree). 50 swaps in total
        /*for(int i = 0 ; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                for(int k = j+1; k < boardSize; k++) {
                  rules.add( new SkyscraperRule( new Point(i, j), new Point(i, k) ) );
                }
            }
        }*/

        //Current rule generation (only adjacent horizontal swaps). 20 swaps in total
        for(int j = 0; j < boardSize; j++) {
            for(int k = 0; k < boardSize-1; k++) {
                rules.add( new SkyscraperRule( new Point(j, k), new Point(j, k+1) ) );
            }
        }

        return rules;
    }

    private  List<Rule> generateRulesAddition(){
        List<Rule> rules = new ArrayList<>();
        for(int i = 0 ; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                for(int k = 0; k < boardSize; k++) {
                    rules.add( new SkycraperRuleAddition( k+1 ,new Point(i, j)) );
                }
            }
        }
        return rules;
    }
}

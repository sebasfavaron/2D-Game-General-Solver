package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.awt.*;
import java.util.Optional;

public class SkycraperRuleAddition implements Rule {


    private int number;
    private Point cell;


    public SkycraperRuleAddition(int number, Point cell) {
        this.number = number;
        this.cell = cell;
    }

    @Override
    public Integer getCost() {
        return 1;
    }

    @Override
    public String getName() {
        return "Instert: "+number+" in cell: ["+cell.x+", "+cell.y+"]";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public Optional<State> apply(State s) {
        SkyscraperState state = (SkyscraperState)s;
        Board board = state.getBoard();

        if(board.getCell(cell.x,cell.y) !=0){
            return Optional.empty();
        }
        SkyscraperState newState = state.insertNumber(cell,number);

        if(!newState.isValid()){
            return Optional.empty();
        }

        return Optional.of(newState);
    }
}

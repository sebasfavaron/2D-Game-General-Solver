package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.api.State;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class SkyscraperState implements State {

    private final Board board;

    /* Constructor generates random state */
    public SkyscraperState(int boardSize) {
        this.board = new Board(boardSize);
    }

    public SkyscraperState(Board board) {
        this.board = board;
    }


    /**
     * Compares self to another state to determine whether they are the same or not.
     *
     * @param o
     *          The state to compare to.
     * @return true if self is the same as the state given, false if they are different.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkyscraperState state = (SkyscraperState) o;
        return board.equals(state.board);
    }

    @Override
    public int hashCode() {
        return board.hashCode();
    }



    /**
     * Provides the representation of the state so it can be printed on the solution representation.
     *
     * @return The STRING representation of the state.
     */
    public String getRepresentation() {
        return board.toString();
    }

    @Override
    public String toString() {
        return board.toString();
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Checks the amount of "observers" that can't see the right amount of buildings and the amount of columns that
     * repreats a number
     *
     * @return if there is any violation
     */
    public boolean satisfiesRestrictions() {
        List<int[]> columns = board.getColumns();
        List<int[]> rows = board.getRows();
        Restrictions restrictions = SkyscraperProblem.getInstance().getRestrictions();

        if((linesFailRestrictions(columns, restrictions.getUp(), restrictions.getBottom()) > 0) || (linesFailRestrictions(rows, restrictions.getLeft(), restrictions.getRight()) > 0)) {
            return false;
        }

        for (int[] column : columns) {
            if(numbersRepeat(column)) {
                return false;
            }
        }

        return true;
    }


    public int valueOfHeuristicA() {
        int amountOfColumnsWrong = 0;

        List<int[]> columns = board.getColumns();
        List<int[]> rows = board.getRows();
        Restrictions restrictions = SkyscraperProblem.getInstance().getRestrictions();

        boolean observersWrong = linesFailRestrictions(columns, restrictions.getUp(), restrictions.getBottom()) > 0 || linesFailRestrictions(rows, restrictions.getLeft(), restrictions.getRight()) > 0;

        for (int[] column : columns) {
            amountOfColumnsWrong += numbersRepeat(column) ? 1 : 0;
        }
        if(amountOfColumnsWrong != 0) {
            amountOfColumnsWrong -= 1;
        }

        return amountOfColumnsWrong + (amountOfColumnsWrong > 1? -1 : 0) + (observersWrong? 1 : 0);
    }

    public int valueOfHeruisticB() {
        List<int[]> columns = board.getColumns();
        List<int[]> rows = board.getRows();
        Restrictions restrictions = SkyscraperProblem.getInstance().getRestrictions();

        int obsWrong = linesFailRestrictions(rows, restrictions.getLeft(), restrictions.getRight());
        obsWrong += linesFailRestrictions(columns, restrictions.getUp(), restrictions.getBottom());

        for (int[] column : columns){
            if(numbersRepeat(column)){
                obsWrong += 1;
            }
        }

        return obsWrong;

    }
    /**
     * Check if any restriction in the first and last lists are being violated (failed)
     */
    private int linesFailRestrictions(List<int[]> lines, List<Integer> left, List<Integer> right) {
        int obsWrong = 0;
        for(int i = 0; i < lines.size(); i++){
            int[] line = lines.get(i);
            obsWrong += lineFailsRestrictions(line, left.get(i), right.get(i));
        }
        return obsWrong;
    }


    /**
     *  Checks if the amount of buildings seen are the ones they should be
     */
    private int lineFailsRestrictions(final int[] line, final int leftRestriction, final int rightRestriction) {
        int maxBuilding = 0;
        int amountSeen = 0;
        int observersWrong = 0;

        if(leftRestriction != 0) { //checking first constraint
            for(int i = 0; i < line.length && amountSeen <= leftRestriction; i++) {
                if(line[i] > maxBuilding) {
                    amountSeen++;
                    maxBuilding = line[i];
                }
            }
            if(amountSeen != leftRestriction) {
                observersWrong += 1;
            }
        }

        maxBuilding = 0;
        amountSeen = 0;

        if(rightRestriction != 0) { //checking second constraint backwards
            for(int i = line.length - 1; i >= 0; i--) {
                if(line[i] > maxBuilding) {
                    amountSeen++;
                    maxBuilding = line[i];
                }
            }
            if(amountSeen != rightRestriction) {
                observersWrong += 1;
            }
        }

        return observersWrong;
    }

    public boolean isValid(){
        List<int[]> columns = board.getColumns();
        List<int[]> rows = board.getRows();

        Restrictions restrictions = SkyscraperProblem.getInstance().getRestrictions();

        for(int i=0;i<board.getSize();i++){
            if(!isValidLine(columns.get(i),restrictions.getUp().get(i),restrictions.getBottom().get(i))) return false;

            if(!isValidLine(rows.get(i),restrictions.getLeft().get(i),restrictions.getRight().get(i))) return false;
        }
        return true;
    }


    public boolean isValidLine(final int[]line, int first, int last){
        if(numbersRepeat(line)){
            return false;
        }
        if(isFull(line) && lineFailsRestrictions(line, first, last) > 0){
            return false;
        }
        return true;

    }

    private boolean isFull(int[] line){
        for(int i = 0; i < line.length ; i++) {
               if(line[i]==0){
                   return false;
               }
            }
        return true;
    }



    /**
     * For line to be a goal Line it will need to have all cells with a value from 1 to size, and each one will need
     *  to be different from each other
     */
    private boolean numbersRepeat(final int[] line) {
        boolean markedArray[] = new boolean[line.length];//BY default are initialized in false

        for(int i = 0; i < line.length; i++) {
            int value = line[i];

            if(value != 0){
                if (markedArray[value-1] ) {
                    return true; // number is repeated in the line
                }

                markedArray[value-1] = true;
            }

        }

        return false;
    }


    /**
     *  State is immutable, so swap cells builds a new board and returns new state
     **/
    public SkyscraperState swap(Point cell1, Point cell2) {
        Board newBoard = board.clone();
        newBoard.swap(cell1, cell2);
        return new SkyscraperState(newBoard);
    }

    public SkyscraperState insertNumber(Point cell, int number){
        Board newBoard = board.insert(cell,number);
        return new SkyscraperState(newBoard);
    }

}
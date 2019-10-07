package ar.edu.itba.sia;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {

    /**
     * Matrix representing the actual game board
     * If a cell in the matrix contains a 0 indicates that the cell is empty
     * If cell is filled it contains a number between 1 and size
     */
    private final byte [][] board;
    private final short  size;


    public Board(int size) {
        List<Integer> numbers = new ArrayList<>();
        for (int i =1;i<size+1;i++){
            numbers.add(i);
        }

        this.size = (short) size;
        board = new byte[size][size];

        for(int i = 0; i < size; i++) {
            Collections.shuffle(numbers);
            for(short j = 0; j < size; j++) {
                board[i][j] = (byte) (int)( numbers.get(j));
            }
        }
    }

    public Board(int size, byte[][]board) {
        this.size = (short) size;
        this.board = board;
    }

    public Integer getCell(int row, int column) {
        return (int)board[row][column];
    }

    public int getSize() {
        return size;
    }

    /**
     * Returns a copy of the columns in the matrix
     */
    public List<int[]> getColumns() {
        List<int[]> columns = new ArrayList<>(size);

        for(int i = 0; i < size; i++) {
            int[] column = new int[size];
            for (int j = 0; j < size; j++) {
                column[j] = board[j][i];
            }
            columns.add(column);
        }

        return columns;
    }

    /**
     *  Returns a copy of rows in the matrix
     */
    public List<int[]> getRows() {
        List<int[]> rows = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            int[] row = new int[size];
            for (int j = 0; j < size; j++) {
                row[j] = board[i][j];
            }
            rows.add(row);
        }
        return rows;
    }

    public Board insert(Point cell, int number){
        Board newBoard = this.clone();
        newBoard.board[cell.x][cell.y] = (byte) number;
        return newBoard;
    }

    public void swap(Point c1, Point c2) {
        byte temp = board[c1.x][c1.y];
        board[c1.x][c1.y] = board[c2.x][c2.y];
        board[c2.x][c2.y] = temp;
    }

    public Board clone() {
        return new Board(size, cloneMatrix());
    }

    private byte[][] cloneMatrix(){
        byte newMatrix[][] = new byte[size][size];
        for(int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                newMatrix[i][j]=board[i][j];
            }
        }
        return newMatrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return size == board1.size && compareMatrix(((Board) o).board);
    }

    public boolean compareMatrix(byte[][] otherBoard){
        for(int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                if(board[i][j]!=otherBoard[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);

        for(int i=0;i<size;i++){
            result = 31 * result + Arrays.hashCode(board[i]);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < size; i++){
            builder.append(Arrays.toString(board[i]));
            builder.append("\n");
        }
        return builder.toString();
    }

    public static Board emptyBoard(int size){

        byte[][] matrix = new byte[size][size];
        for(int i=0;i<size;i++){
            for(int j=0; j<size; j++){
                matrix[i][j]=0;
            }
        }
        return new Board(size,matrix);
    }
}

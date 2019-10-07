package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.api.Rule;

import java.util.Arrays;
import java.util.List;

public class Utils {

    public static int[][] solvedMatrix(){
        int [][] boardMatrix = {
                {1,2,5,3,4},
                {2,3,4,1,5},
                {5,1,3,4,2},
                {3,4,2,5,1},
                {4,5,1,2,3}
        };

        return boardMatrix;
    }

    public static int[][] orderdMatrix(){
        int [][] boardMatrix = {
                {1,2,3,4,5},
                {1,2,3,4,5},
                {1,2,3,4,5},
                {1,2,3,4,5},
                {1,2,3,4,5}
        };
        return boardMatrix;
    }

    public static int[][] orderdMatrixInverted(){
        int [][] boardMatrix = {
                {1,1,1,1,1},
                {2,2,3,2,2},
                {3,3,3,3,3},
                {4,4,4,4,4},
                {5,5,5,5,5}
        };
        return boardMatrix;
    }




//    public static void test(){
//        final List<Integer> up, down, left, right;
//        up = Arrays.asList(new Integer[]{3,4,1,3,2});
//        down = Arrays.asList(new Integer[]{2,1,5,2,2});
//        left = Arrays.asList(new Integer[]{3,4,1,3,2});
//        right = Arrays.asList(new Integer[]{2,1,3,2,2});
//
//        final Restrictions restrictions = new Restrictions(up, right, down, left);
//
//        SkyscraperProblem problem = new SkyscraperProblem.Builder()
//                .withBoardSize(5)
//                .withRestrictions(restrictions)
//                .build();
//
//        System.out.println("Nmber of rules:" + problem.getRules().size());
//        for(Rule rule : problem.getRules()){
//            System.out.println(rule);
//        }
//
//        System.out.println(problem.getInitState().getRepresentation());
//
//        int [][] boardMatrix = {
//                {1,2,5,3,4},
//                {2,3,4,1,5},
//                {5,1,3,4,2},
//                {3,4,2,5,1},
//                {4,5,1,2,3}
//        };
//
//        Board board = new Board(5, boardMatrix);
//        SkyscraperState solvedState = new SkyscraperState(board);
//
//        SkyscraperProblem problemSolved = new SkyscraperProblem.Builder()
//                .withBoardSize(5)
//                .withRestrictions(restrictions)
//                .startingFromState(solvedState)
//                .build();
//
//
//        System.out.println("ORIGINAL");
//        System.out.println(problemSolved.getInitState().getRepresentation());
//
//
//        System.out.println("ROWS");
//        for(int[] line: board.getRows()){
//            System.out.println(Arrays.toString(line));
//        }
//        System.out.println();
//        System.out.println("COLUMNS");
//
//        for(int[] line: board.getColumns()){
//            System.out.println(Arrays.toString(line));
//        }
//        System.out.println();
//
//
//        System.out.println("BOARD");
//        System.out.println(board);
//        System.out.println("\n Board cloned");
//        System.out.println(board.clone());
//
//        System.out.println(board.equals(board.clone()));
//
//
//
//        System.out.println(problemSolved.isGoal(problemSolved.getInitState()));
//    }





}

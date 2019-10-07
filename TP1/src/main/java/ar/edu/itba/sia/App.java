package ar.edu.itba.sia;

import ar.edu.itba.sia.gps.GPSEngine;
import ar.edu.itba.sia.gps.SearchStrategy;
import ar.edu.itba.sia.gps.api.Heuristic;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

/**
 * Main class that runs GPS
 */
public class App 
{
    public static void main( String[] args )
    {
        if(args.length != 5) {
            System.out.println("Must provide 5 arguments: timesToRun inputJsonFile outputFile searchStrategy(bfs|dfs|iddfs|greedy|astar) heuristic(A|B)\n");
            return;
        }
        int timesToRun = 0;
        try {
            timesToRun = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid number for the times to run. Arguments format is:\ntimesToRun inputJsonFile outputFile searchStrategy(bfs|dfs|iddfs|greedy|astar) heuristic(A|B)");
            System.exit(-1);
        }
        LinkedList<Result> results = new LinkedList<>();
        Path file = Paths.get(args[2]);
        boolean aRunFailed = false;
        String sStrategyString = args[3].toLowerCase();
        SearchStrategy strategy = SearchStrategy.GREEDY;
        switch (sStrategyString) {
            case "bfs":    strategy = SearchStrategy.BFS; break;
            case "dfs":    strategy = SearchStrategy.DFS; break;
            case "iddfs":  strategy = SearchStrategy.IDDFS; break;
            case "greedy": strategy = SearchStrategy.GREEDY; break;
            case "astar":  strategy = SearchStrategy.ASTAR; break;
            default: strategy = SearchStrategy.GREEDY;
                     System.out.println("No valid search strategy found, set to Greedy by default.");
        }
        String fileName = args[1];
        String heuristicString = args[4].toLowerCase();
        Heuristic heuristic;
        switch (heuristicString) {
            case "a": heuristic = new SkyscraperHeuristicA(); break;
            default: heuristic = new SkyscraperHeuristicB(); break;
        }
        for (int i = 0; i < timesToRun; i++) {
            Result result = runGPS(fileName, strategy, heuristic);
            results.add(result);
            if(result.failed) {
                aRunFailed = true;
                break;
            }
            System.out.println("---------------------------------------");
        }
        if(aRunFailed) {
            System.out.println("A run failed");
        } else {
            Result averageResult = new Result(results);
            System.out.println("\n\nAll runs were completed successfully. List of averages: ");
            String string = new StringBuilder()
                    .append("\nFile name: ")
                    .append(fileName)
                    .append("\nSearch strategy: ")
                    .append(strategy)
                    .append("\nSolution depth: ")
                    .append(averageResult.solutionDepth)
                    .append("\nTotal cost: ")
                    .append(averageResult.totalCost)
                    .append("\nNodes Visited: ")
                    .append(averageResult.visitedNodes)
                    .append("\nExplosions: ")
                    .append(averageResult.explosions)
                    .append("\nFrontier Nodes: ")
                    .append(averageResult.frontierNodes)
                    .append("\nTime elapsed (ms): ")
                    .append(averageResult.totalTimeElapsed/(long)averageResult.amountOfRuns)
                    .toString();
            System.out.println(string);
            LinkedList<String> l = new LinkedList<>();
            l.add(string);
            try {
                if(Files.exists(file)) {
                    Files.write(file, l, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
                } else {
                    Files.write(file, l, Charset.forName("UTF-8"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Runs a GPS (General Problem Solver) for a Skyscraper game.
     * @param gameJsonFile Json file describing observers restrictions and board size
     * @param searchStrategy The search strategy to be used in the GPS
     * @return Result, even if the search failed
     */
    private static Result runGPS(String gameJsonFile, SearchStrategy searchStrategy, Heuristic heuristic) {
        DataLoader data = new DataLoader(gameJsonFile);

        SkyscraperProblem problem = new SkyscraperProblem.Builder()
                .withBoardSize(data.getSize())
                .withRestrictions(data.getRestrictions())
                .build();

        GPSEngine engine = new GPSEngine(problem, searchStrategy, heuristic);

        long startTime = System.nanoTime();
        try {
            engine.findSolution();
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        }
        long endTime = System.nanoTime();

        //System.out.println("Rules (para debugging solamente): " + problem.getRules());
        if(engine.isFinished() && engine.isFailed()){
            System.out.println("Find solution failed");
        }else if(engine.isFinished() && !engine.isFailed()){
            System.out.println("Found solution");
        }
        System.out.println(engine);
        //engine.getNodesVisited().forEach(System.out::println);
        long runTime = (endTime - startTime) / 1000000;
        System.out.println("Time elapsed (milliseconds): " + runTime);

        return new Result(engine.getSolutionNode().getDepth(), engine.getSolutionNode().getCost(), engine.getBestCosts().size(), engine.getExplosionCounter(), runTime, engine.isFailed(),engine.getFrontierNodes());
    }

    private static class Result {

        private float solutionDepth, totalCost, visitedNodes, explosions, frontierNodes;
        private long totalTimeElapsed;
        private int amountOfRuns;
        private boolean failed;

        public Result(float solutionDepth, float totalCost, float visitedNodes, float explosions, long totalTimeElapsed, boolean failed, float frontierNodes) {
            this.solutionDepth = solutionDepth;
            this.totalCost = totalCost;
            this.visitedNodes = visitedNodes;
            this.explosions = explosions;
            this.totalTimeElapsed = totalTimeElapsed;
            this.amountOfRuns = 1;
            this.frontierNodes = frontierNodes;
            this.failed = failed;
        }

        public Result(List<Result> results) {
            float solutionDepth = 0, totalCost = 0, visitedNodes = 0, explosions = 0, frontierNodes = 0;
            long totalTimeElapsed = 0;
            boolean failed = false;
            for (Result result : results) {
                solutionDepth += result.solutionDepth;
                totalCost += result.totalCost;
                visitedNodes += result.visitedNodes;
                frontierNodes += result.frontierNodes;
                explosions += result.explosions;
                totalTimeElapsed += result.totalTimeElapsed;
                if(result.failed) {
                    failed = true;
                }
            }

            this.amountOfRuns = results.size();
            this.solutionDepth = solutionDepth/results.size();
            this.totalCost = totalCost/results.size();
            this.visitedNodes = visitedNodes/results.size();
            this.frontierNodes =  frontierNodes/results.size();
            this.explosions = explosions/results.size();
            this.totalTimeElapsed = totalTimeElapsed;
            this.failed = failed;
        }
    }
}

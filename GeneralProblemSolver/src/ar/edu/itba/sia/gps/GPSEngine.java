package ar.edu.itba.sia.gps;

import java.util.*;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;


public class GPSEngine {

	private Deque<GPSNode> open;
	private Map<State, Integer> bestCosts;
	private Problem problem;
	private long explosionCounter;
	private long frontierCounter;
	private long IDDFSDepth;
	private Set<State> IDDFSLimitedStates;
	private boolean finished;
	private boolean failed;
	private GPSNode solutionNode;
	private Optional<Heuristic> heuristic;


	// Use this variable in open set order.
	private SearchStrategy strategy;

	public GPSEngine(Problem problem, SearchStrategy strategy, Heuristic heuristic) {
		this.bestCosts = new HashMap<>();
		this.problem = problem;
		this.strategy = strategy;
		this.heuristic = Optional.ofNullable(heuristic);
		this.explosionCounter = 0;
		this.IDDFSDepth = 0;
		this.frontierCounter = 0;
		this.finished = false;
		this.failed = false;

		IDDFSLimitedStates = new HashSet<>();

		pickOpenCollection(strategy);
	}

	private void pickOpenCollection(SearchStrategy strategy) {
	    if(strategy == SearchStrategy.ASTAR) {
	       this.open = new MyPriorityDeque<>(Comparator.comparingInt(GPSNode::getEstimtedCost));

        } else {
	        this.open = new LinkedList<>();
        }
    }

	public void findSolution() throws OutOfMemoryError {
        restartEngine();


		while (open.size() > 0 ) {
			GPSNode currentNode = open.remove();
			if (problem.isGoal(currentNode.getState())) {
				finished = true;
				solutionNode = currentNode;
				frontierCounter += open.size();
				return;
			}
            explode(currentNode);

		}


		if(IDDFSLimitedStates.size()>0){
			IDDFSDepth++;
			restartEngine();
			this.findSolution();

		}else {
			failed = true;
			finished = true;
			System.out.println("Failed, total nodes: " + bestCosts.size());
		}

	}

	private void explode(GPSNode node) {
		explosionCounter++;

		switch (strategy) {
			case BFS:
				addBFSNodes(node);
                break;
            case DFS:
                addDFSNodes(node);
                break;
            case IDDFS:
                addIDDFSNodes(node);
                break;
            case GREEDY:
                addGreedyNodes(node);
                break;
            case ASTAR:
                addAStarNodes(node);
                break;
		}
	}

	private void addBFSNodes(GPSNode currentNode) {
        for(Rule rule : problem.getRules()) {
            rule.apply( currentNode.getState() ).ifPresent(state -> {
                if(!bestCosts.containsKey(state)) {
                    GPSNode newNode = new GPSNode(state, currentNode.getCost() + rule.getCost(), rule);
                    newNode.setParent(currentNode);
                    open.addLast(newNode);
                    bestCosts.put(state, newNode.getCost()); // Unused cost
                }
            });
        }
    }

    private void addDFSNodes(GPSNode currentNode) {
        for(Rule rule : problem.getRules()) {
            rule.apply( currentNode.getState() ).ifPresent(state -> {
                if(!bestCosts.containsKey(state)) {
                    GPSNode newNode = new GPSNode(state, currentNode.getCost() + rule.getCost(), rule);
                    newNode.setParent(currentNode);
                    open.addFirst(newNode);
                    bestCosts.put(state, newNode.getCost()); // Unused cost
                }
            });
        }
    }

//    private void addIDDFSNodes(GPSNode currentNode) {
//	    /*if(currentNode.getDepth() >= IDDFSDepth) {
//			IDDFSLimitedStates.add(currentNode.getState());
//	        return;
//        }
//
//        IDDFSLimitedStates.remove(currentNode.getState());
//
//        addDFSNodes(currentNode);*/
//
//	    for(Rule rule : problem.getRules()) {
//	        rule.apply(currentNode.getState()).ifPresent(state -> {
//                if (!bestCosts.containsKey(state)) {
//                    if (currentNode.getDepth() < IDDFSDepth) {
//                        GPSNode node = new GPSNode(state, currentNode.getCost() + rule.getCost(), rule);
//                        node.setParent(currentNode);
//                        open.addFirst(node);
//                        bestCosts.put(state, node.getCost());
//                        IDDFSLimitedStates.remove(state);
//                    } else {
//                        IDDFSLimitedStates.add(state);
//                    }
//                }
//	        });
//        }
//
//    }

	private void addIDDFSNodes(GPSNode currentNode){

		problem.getRules().forEach(rule ->
			rule.apply(currentNode.getState())
					.map(childState -> new GPSNode(childState, currentNode.getCost() + rule.getCost(), rule))
					.filter(childNode -> !bestCosts.containsKey(childNode.getState())||
						Optional.ofNullable(bestCosts.get(childNode.getState()))
								.filter(otherCost->otherCost>childNode.getCost())
								.isPresent()
					)
					.ifPresent(childNode -> {
						if (currentNode.getDepth() + 1 > IDDFSDepth) {//Do not add the child to the openList as it has more depth than the one desired
							IDDFSLimitedStates.add(childNode.getState());//As the child was not added the algorithm should restart with a greater IDDFSDepth
						} else {
							IDDFSLimitedStates.remove(childNode.getState());
							childNode.setParent(currentNode);
							open.addFirst(childNode);
							bestCosts.put(childNode.getState(), childNode.getCost());
						}
			})
		);



	}

        private void restartIDDFS() {
	    if(strategy != SearchStrategy.IDDFS) {
	        return;
        }
        if(open.size() > 0) {
            return;
        }
        if(IDDFSLimitedStates.isEmpty()) {
        	System.out.println("ACAAAAAAA");
        	return;
		}

        restartEngine();
        IDDFSDepth++;
    }

    private void restartEngine() {
	    open.clear();
	    bestCosts.clear();
	    IDDFSLimitedStates.clear();
        GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null);
        open.add(rootNode);
        bestCosts.put(rootNode.getState(), 0);
        failed = false;
        finished = false;
    }

    private void addGreedyNodes(GPSNode currentNode) {
		problem.getRules().stream()
                .map(rule -> {
                    Optional<State> state = rule.apply(currentNode.getState());
                    Optional<GPSNode> node = Optional.empty();
                    if(state.isPresent()) {
                        node = Optional.of(new GPSNode(state.get(), currentNode.getCost() + rule.getCost(), rule));
                    }
                    return node;
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(node -> !bestCosts.containsKey(node.getState()))
				.sorted((node1, node2) -> {
					return getHeuristic().getValue(node2.getState()) - getHeuristic().getValue(node1.getState()); //descending order
				})
				.forEach(node -> {
					node.setParent(currentNode);
					open.addFirst(node);
					bestCosts.put(node.getState(), node.getCost());
				});
    }

    private void addAStarNodes(GPSNode currentNode) {
		problem.getRules().stream()
				.map(rule -> {
					Optional<State> state = rule.apply(currentNode.getState());
					Optional<GPSNode> node = Optional.empty();
					if(state.isPresent()) {
						GPSNode n = new GPSNode(state.get(), currentNode.getCost() + rule.getCost(), rule);
						n.setParent(currentNode);
						n.setEstimtedCost(getNodeEstimatedCost(n));
						node = Optional.of(n);
					}
					return node;
				})
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(node ->
					!bestCosts.containsKey(node.getState())
				)
				.forEach(node -> {
					open.add(node);
					bestCosts.put(node.getState(), getNodeEstimatedCost(node));
				});
    }

    private int getNodeEstimatedCost(GPSNode node) {
	    int cost = node.getCost();

	    int estimatedCostToEnd = getHeuristic().getValue(node.getState());

	    return cost + estimatedCostToEnd;
    }

    private Heuristic getHeuristic() {
        if(!heuristic.isPresent()) {
            throw new IllegalStateException("Heuristic not defined");
        }
        return this.heuristic.get();
    }


	// GETTERS FOR THE PEOPLE!

	public Queue<GPSNode> getOpen() {
		return open;
	}

	public Map<State, Integer> getBestCosts() {
		return bestCosts;
	}

	public Problem getProblem() {
		return problem;
	}

	public long getExplosionCounter() {
		return explosionCounter;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isFailed() {
		return failed;
	}

	public GPSNode getSolutionNode() {
		return solutionNode;
	}

	public SearchStrategy getStrategy() {
		return strategy;
	}

	public long getFrontierNodes() {
		return frontierCounter;
	}

	@Override
	public String toString() {
		String explosionCounterString = String.format("%,d", explosionCounter);
		String nodesVisitedString = String.format("%,d", bestCosts.size());
		String frontierCounterString = String.format("%,d", frontierCounter);
		return new StringBuilder().append("\nSearch strategy: ")
				.append(strategy)
				.append("\nFinished: ")
				.append(finished)
				.append("\nFailed: ")
				.append(failed)
				.append("\nSolution depth: ")
				.append(solutionNode.getDepth())
				.append("\nTotal cost: ")
				.append(solutionNode.getCost())
				.append("\nNodes Visited: ")
				.append(nodesVisitedString)
				.append("\nExplosions: ")
				.append(explosionCounterString)
				.append("\nForniter nodes: ")
				.append(frontierCounterString)
				.append("\nSolution: \n")
				.append(solutionNode)
				.toString();
	}
}

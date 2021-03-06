package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

public class GPSNode {

	private State state;

	private GPSNode parent;

	private Integer cost;
	private int estimtedCost;

	private Rule generationRule;

	private Integer depth;

	public GPSNode(State state, Integer cost, Rule generationRule) {
		this.state = state;
		this.cost = cost;
		this.generationRule = generationRule;
		this.depth = 0; // Unless overwritten by the setParent() method
		this.estimtedCost = 0;
	}

	public GPSNode getParent() {
		return parent;
	}

	public void setParent(GPSNode parent) {
		this.parent = parent;
		this.depth = parent.getDepth() + 1;
	}

	public State getState() {
		return state;
	}

	public Integer getCost() {
		return cost;
	}

	public Integer getDepth() {
		return depth;
	}

	public int getEstimtedCost() {
		return estimtedCost;
	}

	public void setEstimtedCost(int estimtedCost) {
		this.estimtedCost = estimtedCost;
	}

	@Override
	public String toString() {
		return state.toString();
	}

	public String getSolution() {
		if (this.parent == null) {
			return this.state.toString();
		}
		return this.parent.getSolution() + this.state.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GPSNode other = (GPSNode) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return state.hashCode();
	}

	public Rule getGenerationRule() {
		return generationRule;
	}

	public void setGenerationRule(Rule generationRule) {
		this.generationRule = generationRule;
	}

}

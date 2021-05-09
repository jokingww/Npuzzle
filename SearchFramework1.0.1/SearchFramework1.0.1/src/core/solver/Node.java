package core.solver;

import core.problem.Action;
import core.problem.State;
import core.solver.heuristic.EvaluationType;
import core.solver.heuristic.Predictor;

import java.util.Comparator;
import java.util.EnumMap;

import static core.solver.heuristic.EvaluationType.*;

public final class Node{

	//不考虑路径代价的Node

	/**
	 *
	 * @param state	当前结点
	 * @param parent	父结点
	 * @param action	从父结点到当前结点所采取的Action
	 */
	public Node(State state, Node parent, Action action){
		this(state, parent, action, 0, 0);
	}

	//考虑路径代价的Node

	/**
	 *
	 * @param state		当前结点
	 * @param parent		父结点
	 * @param action		从父结点到当前结点所采取的Action
	 * @param pathCost		从根结点到当前结点的耗散值
	 * @param heuristic	从当前结点到目标结点的距离估计值
	 */
	public Node(State state, Node parent, Action action, int pathCost, int heuristic) {
		super();
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.pathCost = pathCost;
		this.heuristic = heuristic;
	}

	/**
	 *
	 * @param action：当前结点状态所使用的Action
	 * @param predictor：用于计算h值的启发函数
	 * @param goal: 作为启发函数的参数，用于计算子结点状态的h值
	 * @return 当前结点的子结点
	 */
	public Node childNode(Action action, Predictor predictor, State goal) {
		return new Node(state.next(action), this, action,
				pathCost + action.stepCost(),
				predictor.heuristics(state.next(action), goal));
	}

	private final State state;	// the state in the state space to which the node corresponds
	private final Node parent;	// the node in the search tree which generated this node

	public Action getAction() {
		return action;
	}

	private final Action action;  // the action that was applied to the parent to generate the node
	private final int pathCost;	// the cost of the path from the initial state to this node
	private final int heuristic;  // estimated cost of the cheapest path from the state of this node to a goal state

	//返回当前Node的f值 f = g + h
	public int evaluation()
	{
		return pathCost + heuristic;
	}
	
	public State getState() {
		return state;
	}

	public Node getParent() {
		return parent;
	}

	public int getPathCost() {
		return pathCost;
	}

	public int getHeuristic() {
		return heuristic;
	}

	/**
	 *  Node的状态相同，即认为他们是相同的
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;

		if (obj instanceof Node) {
			Node another = (Node) obj;
			//两个Node对象的状态相同，则认为是相同的
			return this.getState().equals(another.getState());
		}
		return false;
        //Node another = (Node) obj;
        //return obj == this || this.getState().equals(another.getState());
	}
	
	public void draw() {
		System.out.println(this);
	}

	/**
	 *	不同估值函数的枚举映射表
	 */
	private static final EnumMap<EvaluationType, Comparator<Node>> evaluators = new EnumMap<>(EvaluationType.class);
	//枚举映射表的初始化
	static{
		//f = g + h FULL
		evaluators.put(FULL,
				Comparator.comparingInt(Node::evaluation)
		);

		//g PATH_COST
		evaluators.put(PATH_COST,
				Comparator.comparingInt(Node::getPathCost)
		);

		//h HEURISTIC
		evaluators.put(HEURISTIC,
				Comparator.comparingInt(Node::getHeuristic)
		);
	}

	/**
	 *
	 * @param type  结点评估器的类型
	 * @return		相关类型的结点评估器
	 */
	public static Comparator<Node> evaluator(EvaluationType type) {
		return evaluators.get(type);
	}

	@Override
	public String toString() {
		return "[" + state.toString() + "⬅" + "[" + parent.getState().toString() + ", " + action.toString() + "], " + pathCost + ", " + heuristic +  "]";
	}

}

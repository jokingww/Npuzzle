package core.solver.heuristic;

import java.util.*;

import core.problem.Problem;
import core.problem.State;
import core.solver.*;

/**
 * 不能被继承的类，final类
 */
public final class BestFirstSearch implements Searcher {

	//已经访问过的节点集合
	private final Set<State> explored;

	//还未扩展的节点队列
	private final AbstractFrontier frontier;

	private final Predictor predictor;
	/**
	 * 构造函数
	 * @param explored 具体的状态类的Set hashSet
	 * @param frontier Node对象的一个优先队列，可以确定一个状态所对应的结点是否在frontier中，
	 */
	public BestFirstSearch(Set<State> explored, AbstractFrontier frontier, Predictor predictor) {
		this.explored = explored;
		this.frontier = frontier;
		this.predictor = predictor;
	}

	@Override
	public Deque<Node> search(Problem problem)
	{
		//如果可直接判断问题是否可解，无解时直接返回解路径为null
		if (!problem.solvable()){
			return null;
		}
		frontier.clear();
		explored.clear();
		//搜索树的根节点
		Node root = problem.root(predictor);
		this.frontier.add(root);
		while (true) {
			if (frontier.isEmpty())
				return null;	//失败
			Node node = frontier.poll(); //choose the lowest-cost node in frontier
			//System.out.println(node.evaluation());
			//如果已经到达目标状态，
			if (problem.goal(node.getState())) {
				return generatePath(node);
			}
			//将当前结点放入explored表中
			explored.add(node.getState());
			for (Node child : problem.childNodes(node, predictor)) {
				// 如果新扩展的节点，没有在Explored和Fringe中出现过。
				// 因为两个node的状态相同，则视为二者相同（equals函数），
				// 所以contains函数判断frontier中是否存在跟child的状态相同的结点
				if (!explored.contains(child.getState()) && !frontier.contains(child)) {
					frontier.offer(child);
				}
				else {
					//child出现在Explored或Fringe中
					//在启发函数满足单调条件的前提下，如果child是出现在Explored表里的节点，肯定不在Fringe中；
					//而且到达这个节点的新路径肯定不会比旧路径更优
					frontier.discardOrReplace(child);
				}
				//System.out.println("expanded: " + expandedNode());
			}
		}
	}

	public int expandedNode(){
		return explored.size();
	}
}
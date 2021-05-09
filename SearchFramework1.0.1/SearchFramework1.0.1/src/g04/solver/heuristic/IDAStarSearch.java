package g04.solver.heuristic;

import core.problem.Problem;
import core.solver.Node;
import core.solver.Searcher;
import core.solver.heuristic.Predictor;
import g04.problem.npuzzle.Puzzle;
import g04.problem.npuzzle.PuzzleState;

import java.util.Deque;
import java.util.HashMap;
import java.util.Stack;

public class IDAStarSearch implements Searcher {

    private final Predictor predictor;

    //裁剪阈值
    private int cutoff;
    //下一轮迭代的裁剪阈值
    private int newCutoff;
    //最大迭代深度
    private int maxIteratorDepth = 256;
    //统计扩展结点数
    private int expanded = 0;

    private final Stack<Node> openStack;
    //private final HashMap<Integer, Integer> closeStack;

    public IDAStarSearch(Predictor predictor) {
        this.predictor = predictor;
        openStack = new Stack<Node>();
        //closeStack = new HashMap<Integer, Integer>();
    }

    @Override
    public Deque<Node> search(Problem problem) {
        if (!problem.solvable()){
            return null;
        }
        //获取根节点
        openStack.clear();
        //closeStack.clear();
        Node root = problem.root(predictor);
        cutoff = root.evaluation();

        while (cutoff < maxIteratorDepth) {
            openStack.push(root);
            newCutoff = cutoff;
            //当栈未空时继续，执行带裁剪值的深度优先搜索
            expanded = 0;
            while (!openStack.empty()) {
                expanded++;
                Node node = openStack.pop();
                //更新裁剪值为未被探索节点中最小的评估值
                if (problem.goal(node.getState())) {
                    return generatePath(node);
                }
                //当小于等于裁剪值时，继续向深处搜索
                for (Node child : problem.childNodes(node, predictor)) {
                    //剪枝，防止节点探索回到父节点
                    if (child.evaluation() <= cutoff) {
                        if (node.getParent() == null || !node.getParent().equals(child)) {
                            //if (closeStack.get(child.getState().hashCode()) == null || closeStack.get(child.getState().hashCode()) > child.evaluation()) {
                                openStack.push(child);
                            //    if (child.evaluation() < cutoff)
                            //        closeStack.put(child.getState().hashCode(), child.evaluation());
                            //}
                        }
                    } else {
                        //记录大于当前cutoff的最小值
                        newCutoff = (newCutoff > cutoff) ? (Math.min(child.evaluation(), newCutoff)) : child.evaluation();
                    }
                }
            }
            //更新裁剪值
            cutoff = newCutoff;
            //closeStack.clear();
            //System.out.println("cutoff: " + cutoff);
            //System.out.println("expanded node: " +  expanded);
        }
        return null;
    }
}

package core.solver;

import core.problem.Problem;
import java.util.ArrayDeque;
import java.util.Deque;

public interface Searcher {

    /**
     * @problem 要解决的问题
     * @return 当前问题的解路径。如果没有解，则返回null
     */
    Deque<Node> search(Problem problem);

    /**
     * 默认实现，从目标结点，反向回溯得到一条路径
     * @param node 目标结点
     * @return  倒推生成的从根结点到目标结点的路径，栈底是目标结点，栈顶是根结点
     */
    default Deque<Node> generatePath(Node node) {
        Deque<Node> stack = new ArrayDeque<>();

        while (node.getParent() != null) {
            stack.push(node);
            node = node.getParent();
        }
        return stack;
    }
}
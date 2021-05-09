package core.solver.heuristic;

import core.problem.State;
import core.solver.Node;

import java.util.*;

/**
 *  AbstractQueue的子类，
 *  其存放的元素的类型为 core.solver.Node
 */
public abstract class  AbstractFrontier extends AbstractQueue<Node> {

    public AbstractFrontier(Comparator<Node> evaluator) {
        this.evaluator = evaluator;
    }

    public Comparator<Node> getEvaluator() {
        return evaluator;
    }

    // 节点优先级比较器，在Node类中定义了三个不同的比较器（Dijkstra, Greedy Best-First, and Best-First）
    // 不同的选择对应不同的算法。
    protected final Comparator<Node> evaluator;

    /**
     * 获取 Frontier 中，状态为 s 的节点
     * @param s  状态
     * @return   存在：  相应的状态为 s 的节点；
     *           不存在：null
     */
    protected abstract Node getNode(State s);

    /**
     * 如果Frontier中已经存在与node状态相同的结点，
     * 则舍弃掉二者之间不好的那一个。
     * @param node 结点
     * @return 插入成功返回true
     */
    public final boolean discardOrReplace(Node node){
        if (node == null)
            throw new NullPointerException();

        //结点node是否出现在frontier中; null: not revisited
        Node oldNode = getNode(node.getState());
        //如果oldNode为null，则当前结点node的状态不在Frontier中，那么肯定在explored表中，
        // 又因为h函数是consistent的，所以discard
        //如果oldNode不为null，则在oldNode已经在Frontier中，并且旧的估值比新的大，即新生成的结点更好
        if (oldNode != null && evaluator.compare(oldNode, node) > 0){
            //则，用新节点替换旧节点
            replace(oldNode, node);
        }
        return true;
    }

    /**
     * 用节点 e 替换掉具有相同状态的旧节点 oldNode
     *
     * @param oldNode 被替换的结点
     * @param newNode 新结点
     */
    public void replace(Node oldNode, Node newNode) {
        this.remove(oldNode);
        this.add(newNode);
    }

    public boolean contains(Node node) {
        return false;
    }
}

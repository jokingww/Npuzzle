package g04.solver.heuristic;

import core.problem.State;
import core.solver.Node;
import core.solver.heuristic.AbstractFrontier;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * S18020031073开发的”超级棒“的Frontier数据结构
 */
public class MyFrontier extends AbstractFrontier {

    /**
     * 优先队列，节点根据评估值排序
     */
    PriorityQueue<Node> frontier = new PriorityQueue(new Comparator<Node>(){
        public int compare(Node a, Node b){
            //return evaluator.compare(a, b);
            if (a.evaluation() != b.evaluation()) {
                return a.evaluation() - b.evaluation();
            } else {
                return a.getHeuristic() - b.getHeuristic();
            }
        }
    });
    //增加Map集合，使得节点的查找时间复杂度降低至O(1)
    HashMap<Integer, Node> hashMap=new HashMap<Integer, Node>();

    /**
     *
     * @param comparator
     */
    public MyFrontier(Comparator<Node> comparator) {
        super(comparator);
    }

    /**
     * 获取 Frontier 中，状态为 s 的节点
     *
     * @param s 状态
     * @return 存在：  相应的状态为 s 的节点；
     * 不存在：null
     */
    @Override
    protected Node getNode(State s) {
        return hashMap.get(s.hashCode());
    }

    /**
     * 用节点 e 替换掉具有相同状态的旧节点 oldNode
     *
     * @param oldNode
     * @param e
     */
    @Override
    public void replace(Node oldNode, Node e) {
        hashMap.put(oldNode.getState().hashCode(), e);
        //frontier.remove(oldNode);
        frontier.offer(e);
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<Node> iterator() {
        return frontier.iterator();
    }

    @Override
    public int size() {
        return frontier.size();
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param node the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this queue
     * @throws NullPointerException     if the specified element is null and
     *                                  this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *                                  prevents it from being added to this queue
     */
    @Override
    public boolean offer(Node node) {
        hashMap.put(node.getState().hashCode(), node);
        frontier.offer(node);
        return true;
    }

    @Override
    public Node poll(){
        try {
            Node node = frontier.poll();
            hashMap.remove(node.getState().hashCode());
            return node;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Node peek() {
        return frontier.peek();
    }

    /**
     * 判断是否含有该节点
     * @param node
     * @return
     */
    @Override
    public boolean contains(Node node) {
        return hashMap.get(node.getState().hashCode()) != null;
    }
}

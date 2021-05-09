package g04.solver.heuristic;

import core.problem.State;
import core.solver.Node;
import core.solver.heuristic.AbstractFrontier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 * S18020031073开发的”超级棒“的Frontier数据结构
 */
public class VeryEffecientFrontier extends AbstractFrontier {

    /**
     * My secret wonderful data structures! :)
     *
     */
    ArrayList <Node> frontier = new ArrayList();
    HashMap<Integer, Node> hashMap=new HashMap();

    /**
     *
     * @param comparator
     */
    public VeryEffecientFrontier(Comparator<Node> comparator) {
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
        /*for (Node node : frontier) {
            if (node.getState().equals(s)) {
                return node;
            }
        }
        return null;*/
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
        //hashMap.put(oldNode.getState().hashCode(), e);
        //frontier.set(frontier.indexOf(oldNode), e);
        this.remove(oldNode);
        this.offer(e);
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
        int j = 0;
        for (Node value : frontier) {
            if (evaluator.compare(value, node) >= 0) {
                break;
            }
            j++;
        }
        frontier.add(j, node);
        hashMap.put(node.getState().hashCode(), node);
        return true;
    }

    @Override
    public Node poll(){
        try {
            Node node = frontier.get(0);
            frontier.remove(0);
            hashMap.remove(node.getState().hashCode());
            return node;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Node peek() {
        try {
            return frontier.get(0);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public boolean contains(Node node) {
        /*for (Node oldNode : frontier) {
            if (oldNode.equals(node)) {
                return true;
            }
        }
        return false;*/
        return hashMap.get(node.getState().hashCode()) != null;
    }

    public void remove(Node node) {
        //frontier.remove(node);
        hashMap.remove(node.getState().hashCode());
    }

}

package g04.problem.npuzzle;

import core.problem.Action;
import core.problem.Problem;
import core.problem.State;
import core.solver.Node;
import g04.solver.heuristic.DisjointPatternDatabase;

import java.util.Deque;

public class Puzzle extends Problem {

    public static DisjointPatternDatabase puzzle3;
    public static DisjointPatternDatabase puzzle4;
    //读取文件内容，加载DisjointPattern数据库
    public static void load(){
        puzzle3 = new DisjointPatternDatabase(3, 2,4);
        //puzzle3.read("resources/db3.txt");
        puzzle3.loadSubSets(44);
        puzzle3.read2("resources/db3(1).txt", 0, 4);
        puzzle3.read2("resources/db3(2).txt", 1, 4);

        //puzzle4 = new DisjointPatternDatabase(4, 2,8);
        //puzzle4.read("resources/db4_78.txt");
        puzzle4 = new DisjointPatternDatabase(4, 3,6);
        //puzzle4.read2("resources/db4_6(1).txt", 0, 6);
        //puzzle4.read2("resources/db4_6(2).txt", 1, 6);
        //puzzle4.read2("resources/db4_3.txt", 2, 3);
        puzzle4.loadSubSets(663);
        puzzle4.read2("resources/db4_3(2).txt", 0, 3);
        puzzle4.read2("resources/db4_6(3).txt", 1, 6);
        puzzle4.read2("resources/db4_6(4).txt", 2, 6);

    }

    /**
     * 构造函数
     * @param initialState 初始状态
     * @param goal  目标状态
     * @param size 棋盘大小
     */
    public Puzzle(State initialState, State goal, int size) {
        super(initialState, goal, size);
        //PuzzleState.init(size, (PuzzleState) goal);
        //loadDisjointPattern();
    }

    /**
     * 比较两个状态的奇偶性是否相等
     * @return
     */
    @Override
    public boolean solvable() {
        PuzzleState.init(size, (PuzzleState)goal);
        return ((PuzzleState)initialState).parity() == ((PuzzleState)goal).parity();
    }

    @Override
    public int stepCost(State state, Action action) {
        return 1;
    }

    /**
     *
     * @param state     当前状态
     * @param action    当前状态下所采用的动作
     * @return 当前action是否可行，true: 可行，false: 不可行
     */
    @Override
    protected boolean applicable(State state, Action action) {
        //获取action对应的位移量
        int[] next = ((PuzzleAction)action).getNext();
        //空白位移动后是否越界
        int row = ((PuzzleState)state).getBlank(0) + next[0];
        int col = ((PuzzleState)state).getBlank(1) + next[1];
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    @Override
    public void showSolution(Deque<Node> path) {

    }

    public String returnSolution(Deque<Node> path) {
        String answer="";
        for(Node node: path) {
            PuzzleAction action = (PuzzleAction) node.getAction();
            int dir = action.getDir();
            switch(dir) {
                case 0:
                    answer+="E ";
                    break;
                case 1:
                    answer+="S ";
                    break;
                case 2:
                    answer+="W ";
                    break;
                case 3:
                    answer+="N ";
                    break;
            }
        }
        return answer;
    }

    /**
     * 输出初始状态和目标状态
     */
    @Override
    public void draw() {
        initialState.draw();
        System.out.println();
        goal.draw();
    }

    /**
     * 打印解路径
     * @param path 解路径
     */
    @Override
    public void printPath(Deque<Node> path) {
        if (path == null){
            System.out.println("No Solution.");
            return;
        }
        initialState.draw();
        for (Node node : path) {
            PuzzleAction puzzleAction = (PuzzleAction) node.getAction();
            puzzleAction.draw();
            PuzzleState PuzzleState = (PuzzleState) node.getState();
            PuzzleState.draw();
        }
        System.out.println();
    }
/*
    public final Iterable<? extends Node> childs(Node parent) {
        ArrayList<Node> nodes = new ArrayList<>();
        //父结点的状态
        PuzzleState parentState = (PuzzleState) parent.getState();
        //对于parentState上所有可能的action，但有的不可行
        //parentState.draw();
        for (Action action : parentState.actions()){
            //如果父结点状态下的动作是可行的
            if (applicable(parentState, action)){
                //得到后继状态
                PuzzleState state = (PuzzleState)parentState.next(action);
                state.setHash(nextHash(parentState, state));
                //计算路径长度 = 父结点路径长度 + 进入后继状态所采取的动作的代价
                int pathCost = parent.getPathCost() + stepCost(state, action);
                //根据曼哈顿距离特性，只需要计算被移动的位将牌改变的曼哈顿距离
                int heuristics = parent.getHeuristic() - nextManhattan(parentState, state);
                //生成子结点
                Node child = new Node(state, parent, action, pathCost, heuristics);
                nodes.add(child);
            }
        }
        return nodes;
    }

    public void loadDisjointPattern() {
        switch (size) {
            case 3:
                disjoint_pattern = new DisjointPatternDatabase(3, 4);
                disjoint_pattern.readFile("resources/db3.txt");
                break;
            case 4:
                disjoint_pattern = new DisjointPatternDatabase(4, 5);
                disjoint_pattern.readFile("resources/db4.txt");
                break;
            default:
                break;
        }
    }
 */
}

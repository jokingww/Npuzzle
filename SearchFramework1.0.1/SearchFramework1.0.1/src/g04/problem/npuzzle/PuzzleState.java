/**
 * 
 */
package g04.problem.npuzzle;

import core.problem.Action;
import core.problem.State;
import core.runner.HeuristicType;
import core.solver.heuristic.Predictor;
import g04.solver.heuristic.DisjointPatternBuilder;
import g04.solver.heuristic.DisjointPatternDatabase;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;


/** 
 * 类描述：
 * 作者： wanwenlong 
 * 创建日期：2021年3月28日
 * 修改人：
 * 修改日期：
 * 修改内容：
 * 版本号： 1.0.0   
 */
public class PuzzleState extends State {
	
	private final int size;
	//该状态每个位置的位将牌数字
	private final int[] board;
	//空格对应位置
    private int[] blank;

    //曼哈顿距离
    private int manhattan = 0;
    //错位将牌
    private int misPlaced = 0;
    //哈希值
    private int hash = 0;
    //分组状态码，确定每个分组对应唯一值
    private int[] stateCode = null;

    //zobrist哈希随机值
    public static int[][][] zobrist;
    //目标状态到默认状态的曼哈顿距离(默认状态为按顺序放置位将牌，空格最后)
    public static int[][][] goalManhattan;
    //disjointPattern
    public static DisjointPatternDatabase db;

    /**
     * 构造函数
     */
    public PuzzleState(int[] board, int size) {
        this.size = size;
        this.board = new int[size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i * size + j] = board[i * size + j];
                if (board[i * size + j] == 0)
                    this.blank = new int[]{i, j};
            }
        }
    }

    /**
     * 拷贝构造
     */
    public PuzzleState(PuzzleState puzzleState) {
        this.size = puzzleState.size;
        this.board = puzzleState.board.clone();
        this.blank = puzzleState.blank.clone();
        /*
        this.board = new int[size * size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(puzzleState.board[i], 0, this.board[i], 0, size);
            this.board[i] = puzzleState.board[i].clone();
        }*/
    }

    public int getSize() {
        return size;
    }

    public int getBoardAt(int i, int j) {
        return board[i * size + j];
    }

    public int getBoardAt(int i) {
        return board[i];
    }

    public int getBlank(int i) {
        return blank[i];
    }

    public int getBoardAtAnother(PuzzleState s1, PuzzleState s2) {
        return s1.getBoardAt(s2.getBlank(0), s2.getBlank(1));
    }

    /**
     * @param i 行坐标, j 列坐标, val 位将牌值
     * @return 该状态对应位置处的Zobrist随机值
     */
    public static int getZobristAt(int i, int j, int val) {
        return zobrist[i][j][val];
    }

    /**
     * 初始化目标状态到默认状态的曼哈顿距离
     * 初始化zobrist哈希随机值
     * 根据Puzzle阶数确定disjointPattern数据库
     * @param size
     * @param goal
     */
    public static void init(int size, PuzzleState goal) {
        Random r = new Random();
        goalManhattan = new int[size][size][2];
        zobrist = new int[size][size][size * size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                goalManhattan[i][j][0] = (goal.board[i * size +  j] - 1) / size;
                goalManhattan[i][j][1] = (goal.board[i * size +  j] - 1) % size;
                for(int k = 0; k < size * size; k++) {
                    zobrist[i][j][k] = r.nextInt();
                }
            }
        }
        switch (size) {
            case 3:
                db = Puzzle.puzzle3;
                break;
            case 4:
                db = Puzzle.puzzle4;
                break;
            default:
                break;
        }
    }

    /**
     * 输出当前状态
     */
    @Override
    public void draw() {
        drawLine();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //System.out.print("| " + String.valueOf(board[i * size + j]) + " ");
                System.out.printf("| %2d ", board[i * size + j]);
            }
            System.out.println("|");
            drawLine();
        }
    }

    /**
     * 画线
     */
    public void drawLine() {
        for (int i = 0; i < size; i++)
            System.out.print("+----");
        System.out.println("+");
    }

    /**
     * 当前状态采用action而进入的下一个状态
     * @param action
     * @return
     */
    @Override
    public State next(Action action) {
        PuzzleState nextState = new PuzzleState(this);
        int[] next = ((PuzzleAction)action).getNext();
        // 交换对应位将牌
        nextState.blank[0] += next[0];
        nextState.blank[1] += next[1];
        nextState.board[this.blank[0] * size + this.blank[1]] = this.board[nextState.blank[0] * size + nextState.blank[1]];
        nextState.board[nextState.blank[0] * size + nextState.blank[1]] = 0;

        // 根据父状态和移动的位将牌计算子状态的哈希值、曼哈顿距离、不在位将牌和分组状态码
        // 根据使用的不用方法，只调用其对应的计算
        nextState.hash = nextHash(this, nextState);
        // 使用曼哈顿距离时执行此代码，使用错位将牌时则将此代码注释，使用再下一行代码(不用的可以注释减少不必要的计算，提升效率)
        nextState.manhattan = this.manhattan - nextManhattan(this, nextState);
        //nextState.misPlaced = this.misPlaced - nextMispalced(this, nextState);

        // 使用DisjointPattern时使用以下代码，不用时可以注释减少不必要的计算，提升效率
        //nextState.stateCode = this.stateCode.clone();
        //int val = (getBoardAtAnother(this, nextState));
        //nextState.stateCode[db.subsets[val]] -= nextStateCode(this, nextState, val);

        // 旧版本写法
        // int val = (getBoardAtAnother(this, nextState) - 1);
        // nextState.stateCode[val / db.n] -= nextStateCode(this, nextState, val);

        return nextState;
    }

    /**
     * 当前状态下可以采用的所有Action
     * @return 所有Action
     */
    @Override
    public Iterable<? extends Action> actions() {
        ArrayList<Action> actions = new ArrayList<Action>();
        //遍历所有方向
        for (int d = 0; d < 4; d++)
            actions.add(new PuzzleAction(d));
        return actions;
    }

    /**
     * NPuzzle状态的奇偶性
     * @return 1:奇 0:偶
     */
    public int parity() {
        // 根据Puzzle奇偶阶数分类讨论
        int n = (size % 2 == 0) ? blank[0] + 1 : 0;
        for (int i = 0; i < size * size; i++) {
            for (int j = 0; j < i; j++) {
                if (this.getBoardAt(i) != 0 && this.getBoardAt(j) != 0 && this.getBoardAt(i) < this.getBoardAt(j))
                    n++;
            }
        }
        return n % 2;
    }

    @Override
    public boolean equals(Object obj) {
        /*
        for (int i = 0; i < size * size - 1; i++) {
            if (this.getBoardAt(i) != ((PuzzleState) obj).getBoardAt(i))
                return false;
        }
        return true;*/

        if (obj == this)
            return true;
        if (obj instanceof PuzzleState) {
            PuzzleState another = (PuzzleState) obj;
            //两个PuzzleState对象的状态相同，则认为是相同的
            return this.hashCode() == obj.hashCode();
        }
        return false;
    }

    /**
     * Zobrist哈希
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        if (hash == 0)
            hash = getHash();
        return hash;
    }

    /**
     * @return zobrist哈希计算
     */
    private int getHash() {
        int hash = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i * size + j] != 0)  {
                    hash ^= getZobristAt(i, j, board[i * size + j]);
                }
            }
        }
        return hash;
    }

    /**
     * @return 单个位将牌移动改变的哈希值
     */
    private int nextHash(PuzzleState parent, PuzzleState child) {
        int hash = parent.hashCode();
        hash ^= getZobristAt(child.getBlank(0), child.getBlank(1), getBoardAtAnother(parent, child));
        hash ^= getZobristAt(parent.getBlank(0), parent.getBlank(1), getBoardAtAnother(child, parent));
        return hash;
    }

    public Object clone() {
        return null;
    }

    //枚举映射，存放不同类型的启发函数
    private static final EnumMap<HeuristicType, Predictor> predictors = new EnumMap<>(HeuristicType.class);
    static{
        predictors.put(HeuristicType.MANHATTAN,
                (state, goal) -> ((PuzzleState)state).getManhattan((PuzzleState)goal));
        predictors.put(HeuristicType.MISPLACED,
                (state, goal) -> ((PuzzleState)state).getMisPlaced((PuzzleState)goal));
        predictors.put(HeuristicType.DISJOINT_PATTERN,
                (state, goal) -> ((PuzzleState)state).getDisjointPattern((PuzzleState)goal));
    }
    public static Predictor predictor(HeuristicType type){
        return predictors.get(type);
    }

    /**
     * @return 获取曼哈顿距离，若已经通过优化则直接返回该值，若无则进行计算
     */
    private int getManhattan(PuzzleState goal) {
        if (manhattan == 0)
            manhattan = manhattan(goal);
        return manhattan;
    }

    private int getMisPlaced(PuzzleState goal) {
        if (misPlaced == 0)
            misPlaced = misPlaced(goal);
        return misPlaced;
    }

    /**
     * 不在位将牌
     */
    private int misPlaced(PuzzleState goal) {
        int heuristics = 0;
        int size = goal.getSize();
        for (int i = 0; i < size * size - 1; i++) {
            if (this.getBoardAt(i) != 0 && this.getBoardAt(i) != goal.getBoardAt(i))
                heuristics++;
        }
        return heuristics;
    }

    /**
     * 曼哈顿距离
     */
    private int manhattan(PuzzleState goal) {
        int heuristics = 0;
        int[][][] m = new int[size][size][2];
        int x, y;
        //第一步，计算当前状态到默认状态曼哈顿距离
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.board[i * size + j] == 0)
                    continue;
                x = (this.board[i * size + j] - 1) / size;
                y = (this.board[i * size + j] - 1) % size;
                m[x][y][0] = x - i;
                m[x][y][1] = y - j;
            }
        }
        //第二步，计算默认状态到目标状态曼哈顿距离
        //结合两步即为当前状态到目标状态曼哈顿距离
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (goal.board[i * size + j] == 0)
                    continue;
                x = (goal.board[i * size + j] - 1) / size;
                y = (goal.board[i * size + j] - 1) % size;
                heuristics += Math.abs(m[x][y][0] + i - x) + Math.abs(m[x][y][1] + j - y);
            }
        }
        return heuristics;
    }

    /**
     * 计算被移动的位将牌改变的曼哈顿距离
     * @param parent 父状态
     * @param child 子状态
     * @return 改变的曼哈顿距离
     */
    private int nextManhattan(PuzzleState parent, PuzzleState child) {
        //被移动的位将牌对应数字
        int n = getBoardAtAnother(parent, child);
        int x = (n - 1) / size;
        int y = (n - 1) % size;
        return Math.abs(goalManhattan[x][y][0] - child.getBlank(0)) + Math.abs(goalManhattan[x][y][1] - child.getBlank(1))
                - Math.abs(goalManhattan[x][y][0] - parent.getBlank(0)) - Math.abs(goalManhattan[x][y][1] - parent.getBlank(1));
    }

    /**
     * @return 被移动的位将牌改变的不在位将牌数
     */
    private int nextMispalced(PuzzleState parent, PuzzleState child) {
        int row = (getBoardAtAnother(parent, child) - 1) / size;
        int col = (getBoardAtAnother(parent, child) - 1) % size;
        if (row == child.getBlank(0) && col == child.getBlank(1)) {
            return 1;
        }
        row = (getBoardAtAnother(child, parent) - 1) / size;
        col = (getBoardAtAnother(child, parent) - 1) % size;
        if (row == parent.getBlank(0) && col == parent.getBlank(1)) {
            return -1;
        }
        return 0;
    }

    private int getDisjointPattern(PuzzleState goal) {
        if (stateCode == null)
            stateCode = getStateCode();
        return disjointPattern();
    }

    /**
     * Disjoint pattern heuristic
     * 计算每个分组的状态码以获取每个分组到目的状态的路径损耗
     */
    private int disjointPattern() {
        int disjointPattern = 0;
        for (int i = 0; i < db.classes; i++) {
            disjointPattern += db.cost[i][stateCode[i]];
            //System.out.println("class = " + i + ", cost = " + db.cost[i][stateCode[i]] + ", stateCode = " + stateCode[i]);
        }
        return disjointPattern;
    }

    /**
     * @return 计算每个组的状态码
     */
    private int[] getStateCode() {
        int[] stateCode = new int[db.classes];
        //System.out.println("classes: " + db.classes);
        for (int i = 0; i < size * size; i++) {
            if (board[i] == 0) continue;
            //stateCode[(board[i] - 1) / db.n] += i * (int) Math.pow(size * size, (board[i] - 1) % db.n);
            stateCode[db.subsets[board[i]]] += i * Math.pow(size * size, db.positions[board[i]]);
        }
        return stateCode;
        //System.out.println("index = " + i + ", classes = " + ((board[i] - 1) / db.n) +
        //        ", val = " + i * (int) Math.pow(size * size, (board[i] - 1) % db.n));
    }

    /**
     * @return 移动位将牌改变的对应分组的状态码
     */
    private int nextStateCode(PuzzleState parent, PuzzleState child, int val) {
        //return (child.getBlank(0) * size + child.getBlank(1)) * (int) Math.pow(size * size, val % db.n)
        //        - (parent.getBlank(0) * size + parent.getBlank(1)) * (int) Math.pow(size * size, val % db.n);
        return (child.getBlank(0) * size + child.getBlank(1)) * (int) Math.pow(size * size, db.positions[val])
                  - (parent.getBlank(0) * size + parent.getBlank(1)) * (int) Math.pow(size * size, db.positions[val]);
    }

    public static void main(String[] args) {
        int[] a = new int[]{8, 13, 0, 6, 1, 15, 9, 14, 3, 4, 5, 11, 7, 2, 10, 12};
        int[] g = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0};
        PuzzleState puzzleState = new PuzzleState(a, 4);
        int n = 0;
        for (int i = 0; i < 4 * 4; i++) {
            for (int j = 0; j < i; j++) {
                if (puzzleState.getBoardAt(i) != 0 && puzzleState.getBoardAt(j) != 0 && puzzleState.getBoardAt(i) < puzzleState.getBoardAt(j))
                    n++;
            }
            System.out.println(n);
        }
        System.out.println(n);
    }
}
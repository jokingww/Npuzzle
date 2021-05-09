package g04.problem.npuzzle;

import g04.solver.heuristic.DisjointPatternBuilder;


//NPuzzle的状态的子分组，disjointPattern
public class SubPuzzleState {

    //最大分组数
    private int maxN;
    //稀疏点数
    private int n;
    private int size;
    private PuzzlePoint[] points;

    private int heuristic = 0;
    // 到目标状态的路径损耗
    private int cost = 0;

    private SubPuzzleState parent = null;

    private static final int[][] next = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0}
    };

    /**
     * 构造函数
     */
    public SubPuzzleState(int size, int n, int maxN, PuzzlePoint[] points) {
        this.n = n;
        this.size = size;
        this.points = new PuzzlePoint[n];
        for (int i = 0; i < n; i++) {
            this.points[i] = new PuzzlePoint(points[i]);
        }
        this.maxN = maxN;
    }

    public SubPuzzleState(SubPuzzleState state) {
        this.n = state.n;
        this.size = state.size;
        this.points = new PuzzlePoint[n];
        for (int i = 0; i < n; i++) {
            this.points[i] = new PuzzlePoint(state.points[i]);
        }
        this.maxN = state.maxN;
    }

    public void draw() {
        for (int i = 0; i < n; i++) {
            System.out.print("(" + points[i].getRow() + "," + points[i].getCol() + "," + points[i].getVal() + ") ");
        }
        System.out.println();
    }

    public boolean applicable(int i, int dir) {
        //移动后是否越界
        int row = points[i].getRow() + next[dir][0];
        int col = points[i].getCol() + next[dir][1];
        if (row < 0 || row >= size || col < 0 || col >= size)
            return false;
        for (int j = 0; j < n; j++) {
            if (points[j].getRow() == row && points[j].getCol() == col) {
                return false;
            }
        }
        return true;
    }

    public int getN() {
        return n;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public int getCost() {
        return cost;
    }

    public SubPuzzleState getParent() {
        return parent;
    }

    public int getNum() {
        return (int) Math.pow(size * size, n);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < n; i++) {
            hash += (points[i].getRow() * size + points[i].getCol()) *
                    //(int)Math.pow(size * size, (points[i].getVal() - 1) % maxN);
                    Math.pow(size * size, DisjointPatternBuilder.positions[points[i].getVal()]);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof SubPuzzleState) {
            SubPuzzleState another = (SubPuzzleState) obj;
            for (int i = 0; i < n; i++) {
                if (!this.points[i].equals(another.points[i]))
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 执行位移操作，并将新状态的pathcost设置父状态pathcost+1
     * @return
     */
    public SubPuzzleState move(int i, int d) {
        SubPuzzleState state = new SubPuzzleState(this);
        int x = state.points[i].getRow() + next[d][0];
        int y = state.points[i].getCol() + next[d][1];
        state.points[i].setRow(x);
        state.points[i].setCol(y);
        state.cost = this.cost + 1;
        state.parent = this;
        return state;
    }

    /**
     * @return 曼哈顿距离 + 线性冲突
     */
    public int heuristic() {
        int heuristic = 0;
        for (int i = 0; i < n; i++) {
            int row = (points[i].getVal() - 1) / size;
            int col = (points[i].getVal() - 1) % size;
            heuristic += Math.abs(points[i].getRow() - row) + Math.abs(points[i].getCol() - col);
            if (col < size - 1 && points[i].getRow() == row) {
                for (int j = 0; j < n; j++) {
                    if (points[i].getRow() == points[j].getRow() &&
                            points[i].getCol() == points[j].getCol() - 1 &&
                            points[i].getVal() > points[j].getVal()) {
                        heuristic += 2;
                    }
                }
            }
        }
        this.heuristic = heuristic;
        return heuristic;
    }
}

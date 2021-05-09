package g04.problem.npuzzle;

import core.problem.Action;

public class PuzzleAction extends Action {

    /*
     *  移动一步
     *  0: Right, 1: Down, 2: Left, 3: Up
     */
    private final int dir;
    //  移动方向
    private static final char[] direction = {'R', 'D', 'L', 'U'};
    private static final int[][] next = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0}
    };

    public PuzzleAction(int dir) {
        this.dir = dir;
    }

    public int[] getNext() {
        return next[dir];
    }

    public int getDir() {
        return dir;
    }

    /**
     * 打印输出action
     */
    @Override
    public void draw() {
        System.out.println("   ↓");
        System.out.println("   ↓-(#, " + direction[dir] + ")");
        System.out.println("   ↓");
    }

    @Override
    public int stepCost() {
        return 1;
    }
}

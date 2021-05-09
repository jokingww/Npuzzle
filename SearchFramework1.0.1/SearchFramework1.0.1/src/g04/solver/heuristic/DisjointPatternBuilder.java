package g04.solver.heuristic;


import g04.problem.npuzzle.PuzzlePoint;
import g04.problem.npuzzle.SubPuzzleState;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

public class DisjointPatternBuilder {

    private int size;
    private int n;
    private int classes;

    public static int[] positions = {-1, 0, 0, 1, 2, 1, 2, 0, 1, 3, 4, 2, 3, 5, 4, 5};

    /**
     * 4阶: 1,048,576
     * 3阶: 65,536
     */
    private final Queue<SubPuzzleState> stateQueue = new ArrayDeque<SubPuzzleState>();

    public DisjointPatternBuilder(int size, int classes, int n) {
        this.size = size;
        this.classes = classes;
        this.n = n;
    }

    public int[] breadFirstSreach(SubPuzzleState root, int num) {
        stateQueue.clear();
        stateQueue.add(root);
        HashSet<Integer> stateSet = new HashSet<>();
        stateSet.add(root.hashCode());
        SubPuzzleState state, child;
        //记录pathCost
        int[] cost = new int[num];

        //广度优先搜索
        while (!stateQueue.isEmpty()) {
            state = stateQueue.poll();
            //遍历每个非零元素
            for (int i = 0; i < root.getN(); i++) {
                //遍历所有方向
                for (int d = 0; d < 4; d++) {
                    if (state.applicable(i, d)) {
                        child = state.move(i, d);
                        if (!stateSet.contains(child.hashCode())) {
                            stateQueue.add(child);
                            stateSet.add(child.hashCode());
                            cost[child.hashCode()] = child.getCost();
                        }
                    }
                }
            }
        }
        System.out.println("Map size = " + stateSet.size());
        stateSet.clear();
        return cost;
    }
/*
    public int[][] generateALLStatus(HashMap[] maps) {
        int nums = (int) Math.pow(size * size, n);
        //System.out.println(nums);
        int[][] cost = new int[classes][nums];
        for (int i = 0; i < classes; i++) {
            //System.out.println(maps[i].size());
            for (Object obj: maps[i].values()) {
                SubPuzzleState state = (SubPuzzleState)obj;
                cost[i][state.hashCode()] = state.getCost();
                //test(state, 854910);
            }
        }
        return cost;
    }

    public int[] generateALLStatus2(HashMap map, int num) {
        //System.out.println(nums);
        int[] cost = new int[num];
        for (Object obj: map.values()) {
            SubPuzzleState state = (SubPuzzleState)obj;
            cost[state.hashCode()] = state.getCost();
            //test(state, 854910);
        }
        return cost;
    }
*/
    public void test(SubPuzzleState state, int hash) {
        if (state.hashCode() == hash) {
            System.out.println("***********************");
            state.draw();
            System.out.println(state.getCost());
            printPath(state);
            System.out.println("***********************");
        }
    }

    /**
     * 打印到目标状态的路径
     * @param state
     */
    public void printPath(SubPuzzleState state) {
        System.out.println("--------------------");
        while (state.getParent() != null) {
            state.draw();
            System.out.println("⬆");
            state = state.getParent();
        }
        state.draw();
        System.out.println("--------------------");
    }
/*
    public void save(int[][] cost, String filename) {
        //创建字符输出流
        FileWriter writeFile = null;
        try {
            File file = new File("resources/" + filename);
            //如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            writeFile = new FileWriter(file);
            for (int[] ints: cost) {
                for (int val: ints) {
                    writeFile.write(val+ "\t");
                }
                writeFile.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(writeFile != null) {
                    writeFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/

    /**
     * 将状态的路径损失存入文件
     * @param cost 路径损失
     * @param filename 文件名
     */
    public void save(int[] cost, String filename) {
        //创建字符输出流
        FileWriter writeFile = null;
        try {
            File file = new File("resources/" + filename);
            //如果该文件不存在，就创建
            if(!file.exists()) {
                file.createNewFile();
            }
            writeFile = new FileWriter(file);
            for (int val: cost) {
                writeFile.write(val+ "\t");
            }
            writeFile.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(writeFile != null) {
                    writeFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void bulidPuzzle3() {
        HashMap[] maps = new HashMap[2];
        DisjointPatternBuilder dpd = new DisjointPatternBuilder(3, 2,4);
        PuzzlePoint[] points = {
                new PuzzlePoint(0, 0, 1),
                new PuzzlePoint(0, 1, 2),
                new PuzzlePoint(0, 2, 3),
                new PuzzlePoint(1, 0, 4),
        };
        SubPuzzleState root = new SubPuzzleState(3, 4, 4, points);
        int[] cost = dpd.breadFirstSreach(root, root.getNum());
        dpd.save(cost,"db3(1).txt");
        PuzzlePoint[] points2 = {
                new PuzzlePoint(1, 1, 5),
                new PuzzlePoint(1, 2, 6),
                new PuzzlePoint(2, 0, 7),
                new PuzzlePoint(2, 1, 8),
        };
        SubPuzzleState root2 = new SubPuzzleState(3, 4, 4, points2);
        cost = dpd.breadFirstSreach(root2, root2.getNum());
        dpd.save(cost,"db3(2).txt");
        System.out.println("===================");
    }

    public static void bulidPuzzle4() {
        HashMap[] maps = new HashMap[3];
        DisjointPatternBuilder dpd = new DisjointPatternBuilder(4, 3,5);
        PuzzlePoint[] points = {
                new PuzzlePoint(0, 0, 1),
                new PuzzlePoint(0, 1, 2),
                new PuzzlePoint(0, 2, 3),
                new PuzzlePoint(0, 3, 4),
                new PuzzlePoint(1, 0, 5),
        };
        PuzzlePoint[] points2 = {
                new PuzzlePoint(1, 1, 6),
                new PuzzlePoint(1, 2, 7),
                new PuzzlePoint(1, 3, 8),
                new PuzzlePoint(2, 0, 9),
                new PuzzlePoint(2, 1, 10),
        };
        PuzzlePoint[] points3 = {
                new PuzzlePoint(2, 2, 11),
                new PuzzlePoint(2, 3, 12),
                new PuzzlePoint(3, 0, 13),
                new PuzzlePoint(3, 1, 14),
                new PuzzlePoint(3, 2, 15),
        };
        SubPuzzleState root = new SubPuzzleState(4, 5, 5, points);
        int[] cost = dpd.breadFirstSreach(root, root.getNum());
        dpd.save(cost,"db4(1).txt");

        SubPuzzleState root2 = new SubPuzzleState(4, 5, 5, points2);
        cost = dpd.breadFirstSreach(root2, root2.getNum());
        dpd.save(cost,"db4(2).txt");

        SubPuzzleState root3 = new SubPuzzleState(4, 5, 5, points3);
        cost = dpd.breadFirstSreach(root3, root3.getNum());
        dpd.save(cost,"db4(3).txt");

        System.out.println("===================");
    }

    public static void bulidPuzzle4_663() {
        DisjointPatternBuilder dpd = new DisjointPatternBuilder(4, 3,6);
        PuzzlePoint[] points = {
                new PuzzlePoint(0, 0, 1),
                new PuzzlePoint(1, 0, 5),
                new PuzzlePoint(1, 1, 6),
                new PuzzlePoint(2, 0, 9),
                new PuzzlePoint(2, 1, 10),
                new PuzzlePoint(3, 0, 13),
        };
        PuzzlePoint[] points2 = {

                new PuzzlePoint(1, 2, 7),
                new PuzzlePoint(1, 3, 8),
                new PuzzlePoint(2, 2, 11),
                new PuzzlePoint(2, 3, 12),
                new PuzzlePoint(3, 1, 14),
                new PuzzlePoint(3, 2, 15),
        };
        PuzzlePoint[] points3 = {
                new PuzzlePoint(0, 1, 2),
                new PuzzlePoint(0, 2, 3),
                new PuzzlePoint(0, 3, 4),
        };
        SubPuzzleState root = new SubPuzzleState(4, 6, 6, points);
        int[] cost = dpd.breadFirstSreach(root, root.getNum());
        dpd.save(cost,"db4_6(3).txt");
        System.out.println("build db4_6(3).txt finish");
        System.out.println("===================");

        SubPuzzleState root2 = new SubPuzzleState(4, 6, 6, points2);
        cost = dpd.breadFirstSreach(root2, root2.getNum());
        dpd.save(cost,"db4_6(4).txt");
        System.out.println("build db4_6(4).txt finish");
        System.out.println("===================");

        SubPuzzleState root3 = new SubPuzzleState(4, 3, 6, points3);
        cost = dpd.breadFirstSreach(root3, root3.getNum());
        dpd.save(cost,"db4_3(2).txt");
        System.out.println("build db4_3(2).txt finish");
        System.out.println("===================");
        System.out.println("===================");
    }
/*
    public static void bulidPuzzle4_78() {
        DisjointPatternBuilder dpd = new DisjointPatternBuilder(4, 2,8);
        PuzzlePoint[] points = {
                new PuzzlePoint(0, 0, 1),
                new PuzzlePoint(0, 1, 2),
                new PuzzlePoint(0, 2, 3),
                new PuzzlePoint(0, 3, 4),
                new PuzzlePoint(1, 0, 5),
                new PuzzlePoint(1, 1, 6),
                new PuzzlePoint(1, 2, 7),
                new PuzzlePoint(1, 3, 8),
        };
        PuzzlePoint[] points2 = {
                new PuzzlePoint(2, 0, 9),
                new PuzzlePoint(2, 1, 10),
                new PuzzlePoint(2, 2, 11),
                new PuzzlePoint(2, 3, 12),
                new PuzzlePoint(3, 0, 13),
                new PuzzlePoint(3, 1, 14),
                new PuzzlePoint(3, 2, 15),
        };
        SubPuzzleState root = new SubPuzzleState(4, 8, 8, points);
        int[] cost = dpd.breadFirstSreach(root, root.getNum());
        dpd.save(cost,"db4_8.txt");
        System.out.println("build db4_8.txt finish");

        SubPuzzleState root2 = new SubPuzzleState(4, 7, 8, points2);
        cost = dpd.breadFirstSreach(root2, root2.getNum());
        dpd.save(cost,"db4_7.txt");
        System.out.println("build db4_7.txt finish");
        System.out.println("===================");
    }
*/
    public static void main(String[] args) {
        //bulidPuzzle3();
        //bulidPuzzle4();
        bulidPuzzle4_663();
    }
}

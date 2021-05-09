package g04.solver.heuristic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DisjointPatternDatabase {
    public int n;
    public int size;
    public int nums;
    public int classes;
    public int[][] cost;

    // 位将牌在分组内的优先级，用于计算状态码
    public int[] positions;
    // 位将牌所属分组
    public int[] subsets;

    //zobrist哈希随机值
    public static int[][][] zobrist;

    public void read(String filename) {
        cost = new int[classes][nums];
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                for (int i = 0; i < cost.length; i++) {
                    String[] line = scanner.nextLine().split("\t");
                    for (int j = 0; j < line.length; j++) {
                        cost[i][j] = Integer.parseInt(line[j]);
                    }
                }
            }
            //System.out.println(cost.length);
            //System.out.println(cost[0].length);
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void read2(String filename, int i, int n) {
        int num = (int) Math.pow(size * size, n);
        cost[i] = new int[num];
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("\t");
                for (int j = 0; j < line.length; j++) {
                    cost[i][j] = Integer.parseInt(line[j]);
                }
            }
            System.out.println("class-" + i + ", length: "+ cost[i].length);
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DisjointPatternDatabase(int size, int classes, int n) {
        this.size = size;
        this.classes = classes;
        this.n = n;
        this.nums = (int) Math.pow(size * size, n);
        cost = new int[classes][];
        System.out.println("build: " + "size: " + size + ", n: " + n + ", classes: " + classes);
    }

    public void loadSubSets(int pattern) {
        switch (pattern) {
            case 44:
                this.positions = new int[]{-1, 0, 1, 2, 3, 0, 1, 2, 3};
                this.subsets = new int[]{-1, 0, 0, 0, 0, 1, 1, 1, 1};
                break;
            case 663:
                this.positions = new int[]{-1, 0, 0, 1, 2, 1, 2, 0, 1, 3, 4, 2, 3, 5, 4, 5};
                this.subsets = new int[]{-1, 1, 0, 0, 0, 1, 1, 2, 2, 1, 1, 2, 2, 1, 2, 2};
                break;
            default:
        }
    }

    public static void main(String[] args) {
    }
}

package g04.problem.npuzzle;

import algs4.util.In;
import core.problem.Problem;
import core.runner.EngineFeeder;
import core.runner.HeuristicType;
import core.solver.Node;
import core.solver.Searcher;
import core.solver.heuristic.AbstractFrontier;
import core.solver.heuristic.EvaluationType;
import core.solver.heuristic.Predictor;
import g04.solver.heuristic.IDAStarSearch;
import g04.solver.heuristic.MyFrontier;

import java.util.ArrayList;
import java.util.Scanner;

public class MyFeeder extends EngineFeeder {
    /**
     * 从文件中读取所有的NPuzzle问题
     * @param io 输入流
     * @return 所有的NPuzzle问题
     */
    @Override
    public ArrayList<Problem> getProblems(In io) {
        ArrayList<Problem> problems = new ArrayList<Problem>();
        while (io.hasNextLine()){
            //棋盘大小
            int size = io.readInt();
            int[] board = new int[size * size];
            //读入初始状态
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    board[i * size + j] = io.readInt();
            PuzzleState initState = new PuzzleState(board, size);
            //读入目标状态
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    board[i * size + j] = io.readInt();
            PuzzleState goal = new PuzzleState(board, size);
            Puzzle puzzleProblem = new Puzzle(initState, goal, size);
            //添加到问题列表
            problems.add(puzzleProblem);
        }
        return problems;
    }

    @Override
    public ArrayList<Problem> getProblems(Scanner scanner) {
        return null;
    }

    @Override
    public AbstractFrontier getFrontier(EvaluationType type) {
        return new MyFrontier(Node.evaluator(type));
    }

    /**
     * 获取对应的启发函数
     * @param type 估值函数的类型
     * @return 对应的启发函数
     */
    @Override
    public Predictor getPredictor(HeuristicType type) {
        return PuzzleState.predictor(type);
    }

    /**
     * 获取IDA*算法的搜索器
     * @return IDA*算法的搜索器
     */
    @Override
    public Searcher getIdaStar() {
        return new IDAStarSearch(PuzzleState.predictor(HeuristicType.MANHATTAN));
    }

    /**
     * 获取IDA*算法的搜索器
     * @return IDA*+DisjointPattern算法的搜索器
     */
    public Searcher getScoreSearcher() {
        Puzzle.load();
        return new IDAStarSearch(PuzzleState.predictor(HeuristicType.DISJOINT_PATTERN));
        //return super.getAStar(HeuristicType.DISJOINT_PATTERN);
    }

    /**
     * @return A*算法的搜索器
     */
    public Searcher getAStar() {
        return super.getAStar(HeuristicType.MANHATTAN);
    }
}

package g04;

import algs4.util.In;
import algs4.util.Stopwatch;
import core.problem.Problem;
import core.runner.EngineFeeder;
import core.solver.Node;
import g04.problem.npuzzle.MyFeeder;
import g04.solver.heuristic.IDAStarSearch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Deque;

public class IDASTAR2 {
    @Test
    public void getIDAstar2Problems() {
        //生成一个具体的EngineFeeder
        EngineFeeder feeder = new MyFeeder();

        //从文件中读入问题的实例，NPuzzle问题
        In problemInput = new In("resources/npuzzle.txt");
        //feeder从文件获取所有问题实例
        ArrayList<Problem> problems = feeder.getProblems(problemInput);

        //从Feeder获取所使用的搜索引擎 IDASTAR2
        IDAStarSearch idastar2 = (IDAStarSearch) feeder.getScoreSearcher();

        for (Problem problem : problems) {
            Stopwatch stopwatch = new Stopwatch();
            //使用AStar引擎求解问题
            Deque<Node> path = idastar2.search(problem);
            //解的可视化
            //problem.showSolution(path);
            //打印路径
            problem.printPath(path);
            if (path != null)
                System.out.println(path.size());
            System.out.println(stopwatch.elapsedTime() + "s");
            System.out.println("==============================================================");
        }
    }
}
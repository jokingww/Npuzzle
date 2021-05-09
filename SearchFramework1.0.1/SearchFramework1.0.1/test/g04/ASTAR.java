package g04;

import algs4.util.In;
import algs4.util.Stopwatch;
import core.problem.Problem;
import core.runner.EngineFeeder;
import core.runner.HeuristicType;
import core.solver.Node;
import core.solver.heuristic.BestFirstSearch;
import g04.problem.npuzzle.MyFeeder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Deque;

public class ASTAR {

    @Test
    public void getAstarProblems() {
        //生成一个具体的EngineFeeder
        EngineFeeder feeder = new MyFeeder();
        //从文件中读入问题的实例，NPuzzle问题
        In problemInput = new In("resources/npuzzle.txt");
        //feeder从文件获取所有问题实例
        ArrayList<Problem> problems = feeder.getProblems(problemInput);

        //从Feeder获取所使用的搜索引擎 AStar
        BestFirstSearch astar = (BestFirstSearch) feeder.getAStar(HeuristicType.MANHATTAN);

        for (Problem problem : problems){
            Stopwatch stopwatch = new Stopwatch();
            //使用AStar引擎求解问题
            Deque<Node> path = astar.search(problem);
            //解的可视化
            //problem.showSolution(path);
            //打印路径
            problem.printPath(path);
            //用于AStar输出
            //System.out.println(astar.expandedNode());
            if (path != null)
                System.out.println(path.size());
            System.out.println(stopwatch.elapsedTime() + "s");
            System.out.println("==============================================================");
        }

    }
}
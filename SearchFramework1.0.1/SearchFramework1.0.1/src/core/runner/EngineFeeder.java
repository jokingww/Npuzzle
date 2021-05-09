package core.runner;

import core.problem.Problem;
import core.problem.State;
import core.solver.Searcher;
import core.solver.heuristic.AbstractFrontier;
import core.solver.heuristic.BestFirstSearch;
import core.solver.heuristic.EvaluationType;
import core.solver.heuristic.Predictor;
import algs4.util.In;
import g04.problem.npuzzle.PuzzleState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 为搜索算法提供各样素材。包括
 *  问题problem，
 *  使用的Frontier，
 *  使用的估值函数 h函数，启发式函数
 *  IDA Star搜索算法的一个实现
 *
 */
public abstract class EngineFeeder {

    /**
     * 从文件输入流中读入NPuzzle问题的实例
     * @param io 输入流
     * @return 文件中所有NPuzzle实例
     */
    public abstract ArrayList<Problem> getProblems(In io);
    public abstract ArrayList<Problem> getProblems(Scanner scanner);
    /**
     * 生成采取某种估值机制的Frontier
     * @param type 结点评估器的类型
     * @return  使用该评估机制的一个Frontier实例
     */
    public abstract AbstractFrontier getFrontier(EvaluationType type);

    /**
     * 获得对状态进行估值的Predictor
     * @param type 估值函数的类型
     * @return 启发函数
     */
    public abstract Predictor getPredictor(HeuristicType type);

    /**
     * 生成IdaStar搜索的一个实例，用来做对比实验
     */
    public abstract Searcher getIdaStar();

    /**
     * 用来提交头歌评分的Searcher
     * @return 小组目前最佳的搜索引擎
     */
    public abstract Searcher getScoreSearcher();

    /**
     * 用来做对比实验的AStar, 对所有问题都是一样的
     * 使用不同的启发函数的AStar
     */
    public Searcher getAStar(HeuristicType type){
        //获取Frontier，其Node以g(n)+h(n)的升序排列
        AbstractFrontier frontier = getFrontier(EvaluationType.FULL);
        //以HashSet作为Explored表
        Set<State> explored = new HashSet<>();
        //根据explored和frontier生成AStar引擎，并使用类型为type的启发函数
        return new BestFirstSearch(explored, frontier, PuzzleState.predictor(type));
    }

    /**
     * 用来做对比实验的Dijkstra，对所有的问题都是一样的
     * @return Dijkstra搜索算法
     */
    public Searcher getDijkstra(){
        //获取Frontier，其Node以g(n)+h(n)的升序排列
        AbstractFrontier frontier = getFrontier(EvaluationType.PATH_COST);
        //以HashSet作为Explored表
        Set<State> explored = new HashSet<>();
        //根据explored和frontier生成AStar引擎，并使用曼哈顿距离作为启发函数
        return new BestFirstSearch(explored, frontier, (state, goal) -> 0);
    }
}
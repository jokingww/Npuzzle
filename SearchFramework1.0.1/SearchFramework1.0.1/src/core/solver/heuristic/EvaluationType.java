package core.solver.heuristic;

/**
 * Best-First搜索的三类不同的估值策略
 * 对所有问题都是通用的
 */
public enum EvaluationType {
    FULL,
    PATH_COST,
    HEURISTIC   //对于不同的问题，启发策略又可能又多种。
}

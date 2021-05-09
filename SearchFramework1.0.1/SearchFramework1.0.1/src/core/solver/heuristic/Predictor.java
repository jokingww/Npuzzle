package core.solver.heuristic;

import core.problem.State;

/**
 *
 *
 */
public interface Predictor {
    /**
     *
     * @param
     *      state 被评估的状态
     *      goal  目标状态
     * @return 该状态到目标状态的启发值
     */
    int heuristics(State state, State goal);
}

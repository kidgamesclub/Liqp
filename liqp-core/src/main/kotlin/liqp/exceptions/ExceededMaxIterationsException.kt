package liqp.exceptions

class ExceededMaxIterationsException(maxIterations: Int) : RuntimeException("exceeded maxIterations: $maxIterations")

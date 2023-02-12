package com.kiki.searchcustomization.util.algorithm

fun List<Double>.normalize(factor: EvaluationFactor, item: Double): Double {
    return if (factor is PriceEvaluationFactor || factor is DistanceEvaluationFactor) {
        (max() - item) / (max() - min())
    } else {
        (item - min()) / (max() - min())
    }
}
package com.kiki.searchcustomization.util.algorithm

import com.kiki.searchcustomization.data.entity.Menu
import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.data.entity.Warteg
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MultiFactorEvaluatorTest {
    private lateinit var evaluator: MultiFactorEvaluator
    private lateinit var list: List<SearchModel>

    @Before
    fun setUp() {
        evaluator = MultiFactorEvaluator()
        val warteg1 = SearchModel(
            Warteg(
                1,
                "Warteg Berkah",
                "Jl. Tenggiri No.46, RT.9/RW.6, Jati, Kec. Pulo Gadung, Kota Jakarta Timur, Daerah Khusus Ibukota Jakarta 13220",
                -6.197724210408504,
                106.89195671534424,
                3.7,
                4.7,
                1406
            ),
            Menu(
                1,
                1,
                "Ayam Goreng",
                15000.0
            )
        )
        val warteg2 = SearchModel(
            Warteg(
                2,
                "Warteg 84",
                "Jl. Tenggiri No.84, RT.9/RW.6, Jati, Kec. Pulo Gadung, Kota Jakarta Timur, Daerah Khusus Ibukota Jakarta 13220",
                -6.197366991342407,
                106.89166721313599,
                1.2,
                4.6,
                635
            ),
            Menu(
                2,
                2,
                "Ayam Goreng",
                12000.0
            )
        )
        val warteg3 = SearchModel(
            Warteg(
                3,
                "Warteg Sederhana",
                "Jl. Tenggiri No.2a, RT.14/RW.3, Jati, Kec. Pulo Gadung, Kota Jakarta Timur, Daerah Khusus Ibukota Jakarta 13220",
                -6.197470471272448,
                106.89175988420091,
                3.4,
                4.4,
                1390
            ),
            Menu(
                2,
                2,
                "Ayam Goreng",
                10000.0
            )
        )
        list = listOf(warteg1, warteg2, warteg3)
    }

    @Test
    fun `Evaluate distance factor`() {
        val distances = list.map {
            it.warteg.distance!!
        }
        val factor = listOf<EvaluationFactor>(DistanceEvaluationFactor(distances))
        val weight = 1.0
        val expectedResult = listOf(
            "Warteg 84",
            "Warteg Sederhana",
            "Warteg Berkah"
        )
        val actualResult = list.map {
            evaluator.evaluate(it, factor, weight)
        }.sortedByDescending { it.score }.map { it.data.warteg.name }
        assertEquals(actualResult, expectedResult)
    }

    @Test
    fun `Evaluate price factor`() {
        val prices = list.map {
            it.menu.price
        }
        val factor = listOf<EvaluationFactor>(PriceEvaluationFactor(prices))
        val weight = 1.0
        val expectedResult = listOf(
            "Warteg Sederhana",
            "Warteg 84",
            "Warteg Berkah"
        )
        val actualResult = list.map {
            evaluator.evaluate(it, factor, weight)
        }.sortedByDescending { it.score }.map { it.data.warteg.name }
        assertEquals(actualResult, expectedResult)
    }

    @Test
    fun `Evaluate rating factor`() {
        val ratings = list.map {
            it.warteg.rating
        }
        val factor = listOf<EvaluationFactor>(RatingEvaluationFactor(ratings))
        val weight = 1.0
        val expectedResult = listOf(
            "Warteg Berkah",
            "Warteg 84",
            "Warteg Sederhana"
        )
        val actualResult = list.map {
            evaluator.evaluate(it, factor, weight)
        }.sortedByDescending { it.score }.map { it.data.warteg.name }
        assertEquals(actualResult, expectedResult)
    }

    @Test
    fun `Evaluate review factor`() {
        val reviews = list.map {
            it.warteg.review.toDouble()
        }
        val factor = listOf<EvaluationFactor>(ReviewEvaluationFactor(reviews))
        val weight = 1.0
        val expectedResult = listOf(
            "Warteg Berkah",
            "Warteg Sederhana",
            "Warteg 84"
        )
        val actualResult = list.map {
            evaluator.evaluate(it, factor, weight)
        }.sortedByDescending { it.score }.map { it.data.warteg.name }
        assertEquals(actualResult, expectedResult)
    }

    @Test
    fun `Evaluate all factors`() {
        val distances = list.map {
            it.warteg.distance!!
        }
        val prices = list.map {
            it.menu.price
        }
        val ratings = list.map {
            it.warteg.rating
        }
        val reviews = list.map {
            it.warteg.review.toDouble()
        }
        val factor = listOf(
            DistanceEvaluationFactor(distances),
            PriceEvaluationFactor(prices),
            RatingEvaluationFactor(ratings),
            ReviewEvaluationFactor(reviews)
        )
        val weight = 1.0.div(factor.size)
        val expectedResult = listOf(
            "Warteg 84",
            "Warteg Sederhana",
            "Warteg Berkah"
        )
        val actualResult = list.map {
            evaluator.evaluate(it, factor, weight)
        }.sortedByDescending { it.score }.map { it.data.warteg.name }
        assertEquals(actualResult, expectedResult)
    }
}


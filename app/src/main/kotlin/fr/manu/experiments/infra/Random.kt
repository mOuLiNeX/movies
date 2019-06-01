package fr.manu.experiments.infra

import java.util.*

private val rand = Random()

fun gaussianRandomTimer(constantDelay: Long, deviation: Long) =
    (rand.nextGaussian() * deviation).toLong() + constantDelay
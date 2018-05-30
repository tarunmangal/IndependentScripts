package com.udaan.independent.scripts

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    val csvFile = File("/Users/tarunmangal/Documents/seed_data_order_variability.csv")
    val orgOrderDetails: List<List<String>> = csvFile.readLines().map { it.split(",") }
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    val startDate = LocalDate.parse("28/02/18", dateFormatter)
    var currentOrgID = ""
    var currentOrgName = ""
    var countLst = initForNewOrder()
    var counter = 0

    orgOrderDetails.forEach { orgDetail ->
        val orgId = orgDetail[0]
        val orgName = orgDetail[1]
//        val dateInstant = LocalDate.parse("${orgDetail[1]}", dateFormatter)
        val count = orgDetail[3].toInt()

        if (orgId == currentOrgID) {
            countLst[counter++] = count
        } else {
            println("$currentOrgID,$currentOrgName,${calculateStandardDev(countLst).let { "${it.first},${it.second}" }}")
            currentOrgID = orgId
            currentOrgName = orgName
            countLst = initForNewOrder()
            counter = 0
            countLst[counter++] = count
        }
    }
}

private fun initForNewOrder(): MutableList<Int> {
    return MutableList<Int>(91, { index -> 0})
}

private fun calculateStandardDev(list: List<Int>): Pair<Double, Double> {
    val mean = list.sum().toDouble() / list.size.toDouble()

    return Pair (mean, Math.sqrt(list.map { Math.pow(it.toDouble() - mean, 2.toDouble()) }.sum() / list.size ))
}
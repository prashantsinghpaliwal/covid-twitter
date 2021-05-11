package com.bigsteptech.covidtracker.network

data class ErrorRo(val field: String, val error: Map<String, String>)
data class ErrorsRo(val message: String, val errors: List<ErrorRo>)

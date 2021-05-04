package com.example.covidtracker.common

object TwitterSearchQueryMaker {

    fun getQuery(
        isBedRequest: Boolean,
        isICURequest: Boolean,
        isOxygenRequest: Boolean,
        isVentilatorRequest: Boolean,
        isFavifluRequest: Boolean,
        isRemdevisirRequest: Boolean,
        isPlasmaRequest: Boolean,
        isTocilizuRequest: Boolean,
        otherQuery: String,
        location: String
    ): String {
        val query = StringBuilder()

        query.append("verified")

        if (location.isNotEmpty())
            query.append(" $location ")

        query.append("(")

        if (otherQuery.isNotEmpty())
            query.append(" $otherQuery")

        if (isBedRequest)
            query.append("bed OR beds")

        if (isICURequest)
            query.append(appendOr(listOf(isBedRequest), "icu"))

        if (isOxygenRequest)
            query.append(appendOr(listOf(isBedRequest, isICURequest), "oxygen"))

        if (isVentilatorRequest)
            query.append(
                appendOr(
                    listOf(
                        isBedRequest,
                        isICURequest,
                        isOxygenRequest
                    ), "ventilator OR ventilators"
                )
            )

        if (isFavifluRequest)
            query.append(
                appendOr(
                    listOf(
                        isBedRequest,
                        isICURequest,
                        isOxygenRequest,
                        isVentilatorRequest
                    ), "faviflu OR favipiravir"
                )
            )

        if (isPlasmaRequest)
            query.append(
                appendOr(
                    listOf(
                        isBedRequest,
                        isICURequest,
                        isOxygenRequest,
                        isVentilatorRequest,
                        isFavifluRequest
                    ), "plasma"
                )
            )

        if (isRemdevisirRequest)
            query.append(
                appendOr(
                    listOf(
                        isBedRequest,
                        isICURequest,
                        isOxygenRequest,
                        isVentilatorRequest,
                        isFavifluRequest,
                        isPlasmaRequest
                    ), "remdevisir"
                )
            )

        if (isTocilizuRequest)
            query.append(
                appendOr(
                    listOf(
                        isBedRequest,
                        isICURequest,
                        isOxygenRequest,
                        isVentilatorRequest,
                        isFavifluRequest,
                        isPlasmaRequest,
                        isRemdevisirRequest
                    ), "tocilizu"
                )
            )

        query.append(")")


        query.append(" -not verified")
        query.append(" -unverified")

        query.append(" -need")
        query.append(" -needed")
        query.append(" -needs")

        query.append(" -required")
        query.append(" -require")
        query.append(" -requires")
        query.append(" -requirement")
        query.append(" -requirements")

        return query.toString()
    }

    private fun appendOr(isPreviousRequestsOn: List<Boolean>, requestQuery: String): String {
        var isPreviousRequestOn = false
        val newQueryString: String
        for (previousRequest in isPreviousRequestsOn) {
            if (previousRequest)
                isPreviousRequestOn = true
        }

        if (isPreviousRequestOn)
            newQueryString = " OR $requestQuery"
        else newQueryString = " $requestQuery"

        return newQueryString
    }
}
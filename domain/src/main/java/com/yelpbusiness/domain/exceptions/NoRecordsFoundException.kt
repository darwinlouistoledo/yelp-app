package com.yelpbusiness.domain.exceptions

/**
 * throws this error for database query without result i.e. get business
 */
class NoRecordsFoundException(message: String = "default error message") : RuntimeException(message)
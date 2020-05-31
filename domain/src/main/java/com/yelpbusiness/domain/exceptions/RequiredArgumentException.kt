package com.yelpbusiness.domain.exceptions

class RequiredArgumentException(argName: String) : RuntimeException(
    "Argument $argName is required."
)
package liqp.tag

class IncludeException(name: String, cause:Throwable): Exception("Error while processing include $name: $cause", cause)
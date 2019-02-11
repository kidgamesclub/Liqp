package liqp.exceptions

import java.io.File

class InvalidIncludeException(val includePath: File, e: Throwable) : Exception("Missing include $includePath (${includePath.absolutePath}", e)

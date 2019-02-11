package liqp.exceptions

class InvalidTemplateException : RuntimeException {

  constructor(message: String) : super(message) {}

  constructor(message: String, cause: Throwable) : super(message, cause) {}
}

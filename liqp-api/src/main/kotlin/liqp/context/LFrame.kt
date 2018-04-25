package liqp.context

interface LFrame {
  fun endLoop()
  operator fun set(varName: String, value: Any?)
  operator fun get(varName: String): Any?

  fun addScopedVar(name: String)
  fun hasVar(varName: String): Boolean
  fun remove(varName:String): Any?

  operator fun minus(varName: String): Any? {
    return remove(varName)
  }

  fun hasScopedVar(varName: String): Boolean
}

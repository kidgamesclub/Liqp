package liqp

class RenderFrame(private val scopedVars: MutableSet<String>) {

  private var _loop: LoopState? = null
  var loop: LoopState
    get() = _loop!!
    set(value) {
      if (_loop != null) {
        throw Exception("Loop already initialized")
      }
      _loop = value
    }

  fun endLoop() {
    this._loop = null
  }

  fun set(varName: String, value: Any?) {
    when (value) {
      null -> variables.remove(varName)
      else -> variables[varName] = value
    }
  }

  fun addScopedVar(name: String) {
    scopedVars.add(name)
  }

  fun get(varName: String): Any? {
    return variables[varName]
  }

  fun hasVar(varName: String): Boolean {
    return variables.containsKey(varName)
  }

  fun remove(varName: String): Any? {
    return variables.remove(varName)
  }

  fun hasScopedVar(varName: String): Boolean {
    return scopedVars.contains(varName)
  }

  private var variables = mutableMapOf<String, Any?>()

  val varNames: Set<String>
    get() = variables.keys

  val allVars: MutableSet<String>
    get() = scopedVars.union(varNames).toMutableSet()
}

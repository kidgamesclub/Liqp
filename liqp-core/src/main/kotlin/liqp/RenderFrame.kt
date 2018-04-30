package liqp

import liqp.context.LFrame
import liqp.context.LoopState

class RenderFrame(private val scopedVars: MutableSet<String>) : LFrame {

  private var _loop: LoopState? = null
  var loop: LoopState
    get() = _loop!!
    set(value) {
      if (_loop != null) {
        throw Exception("Loop already initialized")
      }
      _loop = value
    }

  override fun endLoop() {
    this._loop = null
  }

  override fun set(varName: String, value: Any?) {
    when (value) {
      null -> variables.remove(varName)
      else -> variables[varName] = value
    }
  }

  override fun addScopedVar(name: String) {
    scopedVars.add(name)
  }

  override operator fun get(varName: String): Any? {
    return variables[varName]
  }

  operator fun get(varName: String, supplier:()->Any): Any? {
    return variables.getOrPut(varName, supplier)
  }

  override fun hasVar(varName: String): Boolean {
    return variables.containsKey(varName)
  }

  override fun remove(varName: String): Any? {
    return variables.remove(varName)
  }

  override fun hasScopedVar(varName: String): Boolean {
    return scopedVars.contains(varName)
  }

  private var variables = mutableMapOf<String, Any?>()

  val varNames: Set<String>
    get() = variables.keys

  val allVars: MutableSet<String>
    get() = scopedVars.union(varNames).toMutableSet()
}

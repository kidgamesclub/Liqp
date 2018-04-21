package liqp

import java.io.File
import java.util.concurrent.ExecutorService

interface RenderSettingsSpec {
  val isStrictVariables: Boolean
  val maxIterations: Int
  val maxStackSize: Int
  val isUseTruthyChecks: Boolean
  val includesDir: File
  val maxSizeRenderedString: Int
  val maxRenderTimeMillis: Long
  val executor: ExecutorService?
}

open class RenderSettingsBase(val isStrictVariables: Boolean = false,
                              val maxIterations: Int = Integer.MAX_VALUE,
                              val maxStackSize: Int = 100,
                              val isUseTruthyChecks: Boolean = false,
                              val includesDir: File = File("includes"),
                              val maxSizeRenderedString: Int = Integer.MAX_VALUE,
                              val maxRenderTimeMillis: Long = Long.MAX_VALUE,
                              val executor: ExecutorService? = null)

data class RenderSettings(override var isStrictVariables: Boolean = false,
                          override var maxIterations: Int = Integer.MAX_VALUE,
                          override var maxStackSize: Int = 100,
                          override var isUseTruthyChecks: Boolean = false,
                          override var includesDir: File = File("includes"),
                          override var maxSizeRenderedString: Int = Integer.MAX_VALUE,
                          override var maxRenderTimeMillis: Long = Long.MAX_VALUE,
                          override var executor: ExecutorService? = null) : RenderSettingsBase() {

  fun strictVariables(strictVariables: Boolean): RenderSettings {
    this.isStrictVariables = strictVariables
    return this
  }

  fun maxIterations(maxIterations: Int): RenderSettings {
    this.maxIterations = maxIterations
    return this
  }

  fun maxStackSize(maxStackSize: Int): RenderSettings {
    this.maxStackSize = maxStackSize
    return this
  }

  fun isTruthy(isTruthy: Boolean): RenderSettings {
    this.isUseTruthyChecks = isTruthy
    return this
  }

  fun includesDir(includesDir: File): RenderSettings {
    this.includesDir = includesDir
    return this
  }

  fun maxSizeRenderedString(maxSizeRenderedString: Int): RenderSettings {
    this.maxSizeRenderedString = maxSizeRenderedString
    return this
  }

  fun maxRenderTimeMillis(maxRenderTimeMillis: Long): RenderSettings {
    this.maxRenderTimeMillis = maxRenderTimeMillis
    return this
  }

  fun executor(executor: ExecutorService): RenderSettings {
    this.executor = executor
    return this
  }
}

//data class RenderSettings constructor(override var isStrictVariables: Boolean = false,
//                          override var maxIterations: Int = Integer.MAX_VALUE,
//                          override var maxStackSize: Int = 100,
//                          override var isUseTruthyChecks:Boolean = false,
//                          override var includesDir: File = File("includes"),
//                          override var maxSizeRenderedString: Int = Integer.MAX_VALUE,
//                          override var maxRenderTimeMillis: Long = Long.MAX_VALUE,
//                          override var executor: ExecutorService? = null) : RenderSettingsSpec {
//
//  fun strictVariables(strictVariables: Boolean): RenderSettings {
//    this.isStrictVariables = strictVariables
//    return this
//  }
//
//  fun maxIterations(maxIterations: Int): RenderSettings {
//    this.maxIterations = maxIterations
//    return this
//  }
//
//  fun maxStackSize(maxStackSize: Int): RenderSettings {
//    this.maxStackSize = maxStackSize
//    return this
//  }
//
//  fun isTruthy(isTruthy: Boolean): RenderSettings {
//    this.isUseTruthyChecks = isTruthy
//    return this
//  }
//
//  fun includesDir(includesDir: File): RenderSettings {
//    this.includesDir = includesDir
//    return this
//  }
//
//  fun maxSizeRenderedString(maxSizeRenderedString: Int): RenderSettings {
//    this.maxSizeRenderedString = maxSizeRenderedString
//    return this
//  }
//
//  fun maxRenderTimeMillis(maxRenderTimeMillis: Long): RenderSettings {
//    this.maxRenderTimeMillis = maxRenderTimeMillis
//    return this
//  }
//
//  fun executor(executor: ExecutorService): RenderSettings {
//    this.executor = executor
//    return this
//  }
//}

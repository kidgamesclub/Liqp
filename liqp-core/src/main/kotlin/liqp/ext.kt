package liqp

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File



fun File.child(path: String): File = File(this, path)

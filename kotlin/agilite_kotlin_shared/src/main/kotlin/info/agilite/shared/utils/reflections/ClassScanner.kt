package info.agilite.shared.utils.reflections

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.function.Predicate
import java.util.stream.Collectors


class ClassScanner {
  companion object {
    fun findAllClassOnPackage(packageName: String, classFilter: Predicate<Class<*>> ): Set<Class<*>?> {
      val stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace(".", "/"))
      val reader = BufferedReader(InputStreamReader(stream))
      reader.use {
        return it.lines()
          .filter { line: String -> line.endsWith(".class") }
          .map { line: String ->
            loadClass(
              line,
              packageName
            )
          }
          .filter(classFilter)
          .collect(Collectors.toSet())
      }
    }

    private fun loadClass(className: String, packageName: String): Class<*> {
      return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')))
    }
  }
}
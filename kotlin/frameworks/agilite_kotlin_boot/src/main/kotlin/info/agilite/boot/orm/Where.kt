package info.agilite.boot.orm

interface Where{
  fun where(whereAndOr: String = " WHERE "): String
  val params: Map<String, Any?>?
}

class Teste {

  fun teste() {
    where {
      and {
        or {

        }
        eq("id", 2)
      }
      eq("id", 1)
    }
  }
}


package info.agilite.core.extensions

fun Throwable.headerStackTrace(): String {
  val stack = StringBuilder()
  this.stackTraceToString(stack, 0)
  return stack.toString()
}

private fun Throwable.stackTraceToString(stack: StringBuilder, count: Int) {
  var localCount = count
  localCount++
  stack.append(this.toString()).append("\n")
  if(this.cause != null){
    if(count == 10){
      stack.append("\n").append("Verifique o log para maiores detalhes... \n")
    }else{
      stack.append("\tCause: ")
      this.cause!!.stackTraceToString(stack, localCount)
    }
  }
}
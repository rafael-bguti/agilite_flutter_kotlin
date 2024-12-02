package info.agilite.shared.extensions

import java.util.concurrent.locks.Lock

fun <T:Any?> Lock.doInLock(block: () -> T): T {
  this.lock()
  return try {
    block()
  } finally {
    this.unlock()
  }
}
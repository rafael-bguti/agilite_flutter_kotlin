package info.agilite.boot.security

import org.springframework.stereotype.Component

@Component
class UserDetailService(
  val userCache: UserDetailCache,
  val repository: LoadUserDetailService,
) {
  fun loadUserByUserId(userId: Long): UserDetail? {
    var user = userCache.getUser(userId)
    if (user != null) {
      return user
    }

    user = repository.loadUserByUserId(userId)
    if(user == null) {
      return null
    }

    userCache.putUser(user.userId, user)
    return user
  }
}
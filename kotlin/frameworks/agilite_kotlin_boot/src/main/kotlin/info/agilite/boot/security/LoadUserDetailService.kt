package info.agilite.boot.security


interface LoadUserDetailService {
    fun loadUserByUserId(userId: Long): UserDetail?
}
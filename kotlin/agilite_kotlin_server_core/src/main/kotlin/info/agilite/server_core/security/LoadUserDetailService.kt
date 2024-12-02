package info.agilite.server_core.security


interface LoadUserDetailService {
    fun loadUserByUserId(userId: Long): UserDetail?
}
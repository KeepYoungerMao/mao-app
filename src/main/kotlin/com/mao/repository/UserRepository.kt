package com.mao.repository

import com.mao.entity.UserDo
import com.mao.entity.UserQo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: BaseRepository<UserDo, Int, UserQo> {

    suspend fun findByUsername(username: String): UserDo?

    @Query("""
        SELECT u.*
        FROM sys_user u
        WHERE (:username IS NULL OR :username = '' OR u.username LIKE CONCAT('%', :username, '%'))
          AND (:phone IS NULL OR :phone = '' OR u.phone LIKE CONCAT('%', :phone, '%'))
          AND (:email IS NULL OR :email = '' OR u.email LIKE CONCAT('%', :email, '%'))
          AND (:expired IS NULL OR u.expired = :expired)
          AND (:locked IS NULL OR u.locked = :locked)
          AND (:enabled IS NULL OR u.enabled = :enabled)
          AND (
              :roleId IS NULL
              OR EXISTS (
                  SELECT 1
                  FROM sys_user_role_ref ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = :roleId
              )
          )
          AND (
              :departmentId IS NULL
              OR EXISTS (
                  SELECT 1
                  FROM sys_user_department_ref ud
                  WHERE ud.user_id = u.id
                    AND ud.department_id = :departmentId
              )
          )
        ORDER BY u.id DESC
        LIMIT :pageSize OFFSET :offset
    """)
    fun searchUsers(
        @Param("username") username: String?,
        @Param("phone") phone: String?,
        @Param("email") email: String?,
        @Param("expired") expired: Boolean?,
        @Param("locked") locked: Boolean?,
        @Param("enabled") enabled: Boolean?,
        @Param("roleId") roleId: Int?,
        @Param("departmentId") departmentId: Int?,
        @Param("pageSize") pageSize: Int,
        @Param("offset") offset: Int,
    ): Flow<UserDo>

    @Query("""
        SELECT COUNT(*)
        FROM sys_user u
        WHERE (:username IS NULL OR :username = '' OR u.username LIKE CONCAT('%', :username, '%'))
          AND (:phone IS NULL OR :phone = '' OR u.phone LIKE CONCAT('%', :phone, '%'))
          AND (:email IS NULL OR :email = '' OR u.email LIKE CONCAT('%', :email, '%'))
          AND (:expired IS NULL OR u.expired = :expired)
          AND (:locked IS NULL OR u.locked = :locked)
          AND (:enabled IS NULL OR u.enabled = :enabled)
          AND (
              :roleId IS NULL
              OR EXISTS (
                  SELECT 1
                  FROM sys_user_role_ref ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = :roleId
              )
          )
          AND (
              :departmentId IS NULL
              OR EXISTS (
                  SELECT 1
                  FROM sys_user_department_ref ud
                  WHERE ud.user_id = u.id
                    AND ud.department_id = :departmentId
              )
          )
    """)
    suspend fun countUsers(
        @Param("username") username: String?,
        @Param("phone") phone: String?,
        @Param("email") email: String?,
        @Param("expired") expired: Boolean?,
        @Param("locked") locked: Boolean?,
        @Param("enabled") enabled: Boolean?,
        @Param("roleId") roleId: Int?,
        @Param("departmentId") departmentId: Int?,
    ): Long

}
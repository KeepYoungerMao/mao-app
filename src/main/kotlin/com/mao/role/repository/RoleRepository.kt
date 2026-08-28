package com.mao.role.repository

import com.mao.role.entity.RoleDo
import com.mao.role.entity.RoleQo
import com.mao.common.repository.BaseRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: BaseRepository<RoleDo, Int, RoleQo>
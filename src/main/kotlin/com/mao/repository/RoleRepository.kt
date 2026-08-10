package com.mao.repository

import com.mao.entity.RoleDo
import com.mao.entity.RoleQo
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: BaseRepository<RoleDo, Int, RoleQo>
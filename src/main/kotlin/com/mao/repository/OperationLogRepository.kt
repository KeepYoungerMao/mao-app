package com.mao.repository

import com.mao.entity.OperationLogDo
import com.mao.entity.OperationLogQo

interface OperationLogRepository: BaseRepository<OperationLogDo, Long, OperationLogQo>
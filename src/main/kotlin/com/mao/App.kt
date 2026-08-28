package com.mao

import com.mao.common.repository.BaseRepositoryImpl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories(repositoryBaseClass = BaseRepositoryImpl::class)
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}

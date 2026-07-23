package com.mao

import com.mao.extension.BaseRepositoryImpl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = ["com.mao.repository"], repositoryBaseClass = BaseRepositoryImpl::class)
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}

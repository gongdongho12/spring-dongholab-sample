package com.dongholab

import com.dongholab.configure.CustomBeanNameGenerator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = ["com.dongholab.repository.reactive"])
@EnableJpaRepositories(basePackages = ["com.dongholab.repository.jpa"])
@ComponentScan(nameGenerator = CustomBeanNameGenerator::class)
class DongholabApplication: SpringBootServletInitializer() {

    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(DongholabApplication::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<DongholabApplication>(*args)
}

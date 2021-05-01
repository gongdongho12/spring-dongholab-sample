package com.dongholab.configure

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor

import org.springframework.orm.jpa.JpaTransactionManager

import org.springframework.transaction.PlatformTransactionManager
import java.util.*


@Configuration
@EnableTransactionManagement
class JpaConfig {
    @Value("\${spring.datasource.url}")
    private val dataSourceUrl: String? = null

    @Value("\${spring.datasource.username}")
    private val dataSourceUserName: String? = null

    @Value("\${spring.datasource.password}")
    private val dataSourcePassword: String? = null

    @Value("\${spring.datasource.driver-class-name}")
    private val dataSourceDriver: String = ""

    @Value("\${spring.jpa.generate-ddl}")
    private val generateDdl: Boolean = false

    @Value("\${spring.jpa.hibernate.ddl-auto}")
    private val ddlAuto: String? = "none"

    @Value("\${spring.jpa.properties.hibernate.dialect}")
    private val dialect: String? = "org.hibernate.dialect.MySQL57Dialect"

    fun dataSource(): DataSource? {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(dataSourceDriver)
        dataSource.username = dataSourceUserName
        dataSource.password = dataSourcePassword
        dataSource.url = dataSourceUrl
        return dataSource
    }

    @Bean
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean? {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource()!!
        em.setPackagesToScan(*arrayOf("com.dongholab.entity"))
        em.jpaVendorAdapter = HibernateJpaVendorAdapter()
        additionalProperties()?.let { em.setJpaProperties(it) }
        return em
    }

    @Bean
    fun transactionManager(): PlatformTransactionManager? {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory()!!.getObject()
        return transactionManager
    }

    @Bean
    fun exceptionTranslation(): PersistenceExceptionTranslationPostProcessor? {
        return PersistenceExceptionTranslationPostProcessor()
    }

    fun additionalProperties(): Properties? {
        val properties = Properties()
        if (generateDdl) {
            properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto)
        }
        properties.setProperty("hibernate.dialect", dialect)
        return properties
    }
}
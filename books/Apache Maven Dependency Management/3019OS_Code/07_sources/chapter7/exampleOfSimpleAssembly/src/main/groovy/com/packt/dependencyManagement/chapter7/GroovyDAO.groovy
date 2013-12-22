package com.packt.dependencyManagement.chapter7

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component(value = "anyDao")
/**
 * Fake-DAO
 */
class GroovyDAO implements AnyDao {

    @Value("*** 'any data' retrieved by Groovy / Spring annotation ***")
    /**
     * Implements AnyDao.getAnyString()
     */
    String anyString
}
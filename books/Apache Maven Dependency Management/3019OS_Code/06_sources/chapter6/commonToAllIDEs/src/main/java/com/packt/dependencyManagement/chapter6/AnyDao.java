package com.packt.dependencyManagement.chapter6;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/*
* this class is declared as a Spring repository
*/
@Repository
/**
 * Basic "DAO", that only checks the DB is up
 */
public class AnyDao extends HibernateDaoSupport {
    private static final Logger LOGGER = Logger.getLogger(AnyDao.class);

    @Autowired
    public void anyMethodName(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    public void callDao() {
        final Integer i = (Integer) getSession().createSQLQuery("select 1 from INFORMATION_SCHEMA.SYSTEM_USERS").uniqueResult();
        LOGGER.info("DAO call result: " + i);
    }
}

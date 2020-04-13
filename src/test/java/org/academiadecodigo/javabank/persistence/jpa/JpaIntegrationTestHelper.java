package org.academiadecodigo.javabank.persistence.jpa;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.GenericXmlApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JpaIntegrationTestHelper {

    protected EntityManagerFactory emf;
    protected EntityManager em;

    @Before
    public void init() {

        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.getEnvironment().setActiveProfiles("test");
        ctx.load("file:src/main/webapp/WEB-INF/spring/spring-config.xml");
        ctx.refresh();

        emf = ctx.getBean(EntityManagerFactory.class);
        em = emf.createEntityManager();

    }

    @After
    public void tearDown() {

        if (em != null) {
            em.clear();
            em.close();
        }

        if (emf != null) {
            emf.close();
        }
    }
}

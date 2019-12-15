package com.adenon.sp.persistence.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EmHolder {

    private final EntityManagerFactory emf;
    private final EntityManager        em;

    public EmHolder(EntityManagerFactory emf,
                    EntityManager em) {
        this.emf = emf;
        this.em = em;
    }

    public EntityManager manager() {
        return this.em;
    }

    public EntityManagerFactory factory() {
        return this.emf;
    }

    public void close() {
        this.em.close();
        this.emf.close();
    }

}

package eu.jangos.auth.controller;

/*
 * Copyright 2016 Warkdev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import eu.jangos.auth.hibernate.HibernateUtil;
import eu.jangos.auth.model.Realm;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RealmService is an abstraction layer to interact with Realm entities.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class RealmService {

    private static final Logger logger = LoggerFactory.getLogger(RealmService.class);

    /**
     * Provides all realms stored in the database.
     *
     * @return A List of Realms corresponding to all realms stored in the
     * database.
     */
    public List<Realm> getAllRealms() {
        logger.debug("Returning all realms");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Realm> listRealms = session.createCriteria(Realm.class).list();

            return listRealms;
        } catch (HibernateException he) {
            return null;
        }

    }

    /**
     * Execute the calculation of the realm population, calculation is then
     * saved into the database. Calculation is: (playerCount / maxPlayerCount *
     * 2)
     *
     * @param realm The realm for which the population should be recalculated.
     */
    public void calculatePopulation(Realm realm) {
        if (realm == null) {
            return;
        }

        float population = (realm.getCountPlayers() + realm.getMaxPlayers()) * 2.0f;

        realm.setPopulation(population);

        save(realm);
    }

    /**
     * This method save a realm information into the database.
     *
     * @param realm The realm instance to be saved in the database.
     * @return A realm object representing the created realm. Or null if there
     * was an error while updating the database.
     */
    public Realm save(Realm realm) {
        if (realm == null) {
            logger.error("Realm to save is null.");
            return null;
        }

        Realm r = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            r = (Realm) session.merge(realm);
            session.flush();
            session.getTransaction().commit();
            logger.info("Realm " + realm.getName() + " saved.");
        } catch (HibernateException he) {
            logger.error("There was an issue while performing the save action on " + realm.getName());
        }

        return r;
    }
}

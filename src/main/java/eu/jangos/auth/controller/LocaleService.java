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
import eu.jangos.auth.model.Locale;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LocaleService is an abstract implementation for interaction with Locale entity.
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class LocaleService {     
    private static final Logger logger = LoggerFactory.getLogger(LocaleService.class);           
    
    private ParameterService ps = new ParameterService();
    
    /**
     * Returns the corresponding Locale entity for a given String (e.g. "enGB" will return the locale "English").
     * If the locale doesn't exist in database, the default locale will be returned.
     * @param locale The short name of the locale to retrieve from the database (e.g. "enGB" or "frFR").
     * @return The locale corresponding to the locale parameter or the default locale if not found.
     */
    public Locale getLocaleForString(String locale){
        if(locale == null || locale.isEmpty()){
            logger.debug("Locale string is empty.");
            return getDefaultLocale();
        }        
        
        Locale l;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            l = (Locale) session.createCriteria(Locale.class).add(Restrictions.like("localeString", locale)).uniqueResult();
            
            if(l == null)
                return getDefaultLocale();
            
            logger.debug("Locale found, returning "+l.getLocale());
            return l;
        } catch(HibernateException he) {
            logger.debug("Locale not supported, providing default.");
            return getDefaultLocale();
        }        
    }
    
    /**
     * Provides the default locale according to the parameter defaultLocale in database.
     * @return the default locale.
     */
    private Locale getDefaultLocale() {     
        String locale = this.ps.getParameter("defaultLocale");
        
        Locale l;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            l = (Locale) session.createCriteria(Locale.class).add(Restrictions.like("localeString", locale)).uniqueResult();
            logger.debug("Locale found, returning "+l.getLocale());
            return l;
        } catch (HibernateException he) {
            logger.debug("No default locale found, returning null.");
            return null;
        }                        
    }
}

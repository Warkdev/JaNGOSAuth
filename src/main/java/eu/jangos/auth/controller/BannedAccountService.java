package eu.jangos.auth.controller;

/*
 * Copyright 2016 Talendrys.
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
import eu.jangos.auth.model.Account;
import eu.jangos.auth.model.Bannedaccount;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BannedAccountService is an abstraction layer to perform activities on a BannedAccount entity.
 * @author Warkdev
 * @version v0.1 BETA
 */
public class BannedAccountService {
    private static final Logger logger = LoggerFactory.getLogger(BannedAccountService.class);                

    /**
     * This method checks whether an account is banned or not into the database.
     * @param account The account to be checked.
     * @return true if the account is banned, false otherwise.
     */
    public boolean isAccountBanned(Account account) {
        if(account == null){
            logger.debug("Account parameter is null. ban = true.");
            return true;
        }         
                        
        try(Session session = HibernateUtil.getSessionFactory().openSession()) { 
            Bannedaccount banInfo = (Bannedaccount) session.createCriteria(Bannedaccount.class)
                    .add(Restrictions.and(
                        Restrictions.eq("accountByFkBannedaccount", account),
                        Restrictions.eq("active", true)))
                .uniqueResult();                        
            
            if(banInfo == null)
            {
                return false;
            }                        
            
            return true;
        } catch (HibernateException he) {
            return true;
        }        
    }
    
}

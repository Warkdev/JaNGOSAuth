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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * AccountService is the controller for account management. It allows to manager back-end database transparently as well as ensuring business rules
 * such as failed login attempt. It handles the application logic.
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);                
    
    private final BannedAccountService bas = new BannedAccountService();
    private final BannedIPService bis = new BannedIPService();
    private final LocaleService ls = new LocaleService();
    private final ParameterService ps = new ParameterService();
       
    /**
     * Returns the account corresponding to the given name.
     * The name must contain only alphanumeric values.
     * @param name The name of the account to be found.
     * @return The account corresponding to the given name. Null if the account if not found.
     */
    public Account getAccount(String name)
    {
        if(name == null || name.isEmpty())
        {
            logger.error("The account name is null or empty.");
            return null;
        }
        
        if(!name.matches("[a-zA-Z0-9]+"))
        {
            logger.error("The account name must contain only alphanumeric values.");
            return null;
        }                
        
        Account account = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        account = (Account) session.createCriteria(Account.class)
                .add(Restrictions.like("name", name))
                .setFetchMode("realmAccounts", FetchMode.JOIN)
                .uniqueResult();
        
        session.close();
        
        return account;
    }
    
    /**
     * It will check that a single account with a given named exists in the dabase.
     * @param name The name of the account used to login.
     * @return true if an account with the given name exists in the database, false otherwise.
     */
    public boolean checkExistence(String name) {                
        // Empty names are not allowed.
        if(name == null || name.isEmpty())
        {
            logger.error("The account name is null or empty.");
            return false;
        }
        
        if(!name.matches("[a-zA-Z0-9]+"))
        {
            logger.error("The account name must contain only alphanumeric values.");
            return false;
        }                  
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean found = false;                        
        
        found = (session.createCriteria(Account.class).add(Restrictions.ilike("name", name)).list().size() == 1);               
        
        session.close();
        
        return found;
    }

    /**
     * Performs login update with the client information.
     * @param account Account to login.
     * @param ip IP of the client who just logged in.
     * @param locale Locale of the client who just logged in.
     * @param session Hashed session key of the client who just logged in.
     */
    public void login(Account account, String ip, String locale, String session) {                      
        account.setFailedattempt(0);
        account.setLastIp(ip);
        account.setLastlogin(new Date());        
        account.setLocale(this.ls.getLocaleForString(locale));
        account.setSessionkey(session);
        
        update(account);
        
        logger.info("User "+account.getName() + " just logged in successfully from ip "+ip);
    }    
    
    /**
     * Check whether the account or the client ip is banned or not.
     * @param ip IP of the client who is trying to log in.
     * @return true if the account or the ip is banned, false otherwise. It returns false if there's no account loaded.
     */
    public boolean isBanned(Account account, String ip) {
        if(account == null || ip == null || ip.isEmpty()){
            logger.debug("Account doesn't exist or ip is empty, ban = true.");
            return true;
        }
        
        return (this.bas.isAccountBanned(account) || this.bis.isIPBanned(ip));
    }
    
    /**
     * This method updates the failed attempt counter of the account. 
     * If the number of attempts is higher than the maxFailedAttempt parameter, 
     * it locks the account
     */
    public void updateFailedAttempt(Account account) {
        if (account == null){
            logger.debug("There is no account loaded, can't perform update operation.");
            return;
        }
        
        int failed = account.getFailedattempt() + 1;
        
        if(Integer.parseInt(this.ps.getParameter("maxFailedAttempt")) <= failed){
            logger.error("MaxFailedAttempt reached for account "+account.getName()+". Account is now locked.");
            account.setLocked(true);
        }
        
        account.setFailedattempt(failed);
        
        this.update(account);
        logger.debug("Account "+account.getName()+" attempt counter increased to "+failed);
    }
    
    /**
     * This method updates the account information into the database.
     * @param account The account to update in the dabatase.
     */
    public void update(Account account) {
        if(account != null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();                        
            session.merge(account);
            session.flush();
            session.getTransaction().commit();
            logger.debug("Account "+account.getName()+" updated.");
            session.close();
        }
    }   
}

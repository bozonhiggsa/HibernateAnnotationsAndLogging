package com.hibernateApp.hibernate.annotations;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Entry point
 * @author Ihor Savchenko
 * @version 1.0
 */
public class DeveloperRunner {

    private static final Logger logger = Logger.getLogger(DeveloperRunner.class);

    public static void main(String[] args) {

        DeveloperRunner developerRunner = new DeveloperRunner();

        System.out.println("Adding Developer's records to the database");
        Integer developerId1 = developerRunner.addDeveloper("Proselyte", "Developer", "Java Developer", 2);
        Integer developerId2 = developerRunner.addDeveloper("Some", "Developer", "C++ Developer", 2);
        Integer developerId3 = developerRunner.addDeveloper("Peter", "Team Lead", "Java Team Lead", 6);

        System.out.println("List of Developers:");
        developerRunner.listDevelopers();

        System.out.println("Removing \'Some Developer\' and updating \'Experienced Developer\''s experience:");
        developerRunner.removeDeveloper(developerId2);
        developerRunner.updateDeveloper(developerId1, 3);

        System.out.println("Final list of Developers:");
        developerRunner.listDevelopers();

        HibernateUtil.getSessionFactory().close();
    }

    public Integer addDeveloper(String firstName, String lastName, String specialty, int experience) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer developerId = null;

        transaction = session.beginTransaction();
        Developer developer = new Developer(firstName, lastName, specialty, experience);
        developerId = (Integer) session.save(developer);
        transaction.commit();
        session.close();

        return developerId;
    }

    public void listDevelopers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        List<Developer> developers = session.createQuery("FROM Developer").list();
        for (Developer developer : developers) {
            System.out.println(developer);
            System.out.println("\n================\n");
        }
        session.close();
    }

    public void updateDeveloper(int developerId, int experience) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = (Developer) session.get(Developer.class, developerId);
        developer.setExperience(experience);
        session.update(developer);
        transaction.commit();
        session.close();

        PropertyConfigurator.configure("Log4Jconfig1.properties");

        logger.setLevel(Level.WARN);

        logger.trace("1. Trace-Info");
        logger.debug("2. Debug-Info");
        logger.info("3. Info");
        logger.warn("4. Warn-Info");
        logger.error("5. Error-Info");
        logger.fatal("6. Fatal-Info");

    }

    public void removeDeveloper(int developerId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = (Developer) session.get(Developer.class, developerId);
        session.delete(developer);
        transaction.commit();
        session.close();
    }
}

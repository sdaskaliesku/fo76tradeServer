package com.fo76.jdump;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Hello world!
 */
public class App {

  private static SessionFactory sessionFactory = null;

  private static void configureSessionFactory() throws HibernateException {
    Configuration configuration = new Configuration();
    configuration.configure();
    sessionFactory = configuration.buildSessionFactory();
  }

  public static void main(String[] args) {
    // Configure the session factory
    configureSessionFactory();

    Session session = null;
    Transaction tx = null;

    try {
      session = sessionFactory.openSession();
      tx = session.beginTransaction();

      // Creating Contact entity that will be save to the sqlite database
      List<SqlLiteEntity> list = session.createSQLQuery("SELECT *\n"
          + "from fo76jdump\n"
          + "where signature = 'WEAP'\n"
          + "  and flags not like '%Non-Playable%'\n"
          + "  and name != 'null'\n"
          + "  and editorid not like '%Dummy%'\n"
          + "  and editorid not like '%Test%'\n"
          + "  and editorid not like '%Temp%'\n"
          + "  and editorid not like 'POST%'\n"
          + "  and editorid not like 'CUT%'\n"
          + "  and editorid not like 'DEPRECATED%'\n"
          + "  and editorid not like 'Workshop%'\n"
          + "  and editorid not like 'zzz%'\n"
          + "  and editorid not like 'Survival%'\n"
          + "  and editorid not like 'V96%'\n"
          + "  and editorid not like '%DUPLICATE%'\n"
          + "  and editorid not like '%OBSOLETE%'\n"
          + "  and editorid not like 'DLC05WorkshopFireworkWeapon%'\n"
          + "  and editorid not like 'DELETED%'\n"
          + "  and editorid not like 'ATX_KDInkwell_Quill%'\n"
          + "  and editorid not like 'W05%'\n"
          + "  and editorid not like 'Debug%'\n"
          + "  and editorid not like '%Epic'\n"
          + "  and editorid not like 'ATX_CroquetMallet_Red%'\n"
          + "  and editorid not like '%Babylon%'\n"
          + "  and editorid not like 'MTR06%'\n"
          + "  and editorid not like 'MTR05_10mm_ScorchedTrainer%'\n"
          + "  and editorid not like 'P01B_Mini_Robot01_Binoculars%'\n"
          + ";")
          .addEntity(SqlLiteEntity.class)
          .list();
      list.forEach(x-> {
        System.out.println(x);
      });
      // Committing the change in the database.
      session.flush();
      tx.commit();

    } catch (Exception ex) {
      ex.printStackTrace();

      // Rolling back the changes to make the data consistent in case of any failure
      // in between multiple database write operations.
      tx.rollback();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
}
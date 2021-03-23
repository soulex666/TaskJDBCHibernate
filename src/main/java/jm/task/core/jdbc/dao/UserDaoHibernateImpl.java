package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS users;";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE users";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users " +
                                                "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                                                "name VARCHAR(100) NOT NULL, " +
                                                "last_name VARCHAR(100) NOT NULL, " +
                                                "age TINYINT NOT NULL)";
    private final Util util;

    public UserDaoHibernateImpl() {
        util = new Util("config");
    }

    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        SessionFactory factory = util.getSessionFactory();
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery(CREATE_TABLE)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        SessionFactory factory = util.getSessionFactory();
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery(DROP_TABLE)
                    .addEntity(User.class)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        SessionFactory factory = util.getSessionFactory();
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            session.save(new User(name, lastName, age));

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        SessionFactory factory = util.getSessionFactory();
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            User user;
            if ((user = session.load(User.class, id)) != null) {
                session.delete(user);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        SessionFactory factory = util.getSessionFactory();
        List<User> users = new ArrayList<>();
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            criteria.from(User.class);
            users.addAll(session.createQuery(criteria).getResultList());

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        SessionFactory factory = util.getSessionFactory();
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery(TRUNCATE_TABLE)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}

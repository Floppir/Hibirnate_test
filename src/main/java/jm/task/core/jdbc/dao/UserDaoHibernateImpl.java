package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession();) {
            Transaction transaction = session.beginTransaction();

            try {
                String sql = "CREATE TABLE IF NOT EXISTS user " +
                        "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(50) NOT NULL, last_name VARCHAR(50) NOT NULL, " +
                        "age TINYINT NOT NULL)";

                session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                String sql = "DROP TABLE IF EXISTS users";

                session.createSQLQuery(sql).addEntity(User.class).executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            User user = new User(name, lastName, age);
            session.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<User> cd = cb.createCriteriaDelete(User.class);
            cd.where(cb.equal(cd.from(User.class).get("id"), id));

            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery(cd).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cd = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cd.createQuery(User.class);
            Query query = session.createQuery(cq.select(cq.from(User.class)));
            List<User> userList = query.getResultList();
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<User> cd = cb.createCriteriaDelete(User.class);
            cd.from(User.class);

            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery(cd).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }

        }
    }
}

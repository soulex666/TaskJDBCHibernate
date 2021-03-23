package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class Util {
    private final ResourceBundle resource;
    private final String url;
    private final String user;
    private final String password;
    private final String driver;

    public Util(String fileConfigName) {
        resource = ResourceBundle.getBundle(fileConfigName);
        driver = resource.getString("db.driver");
        url = resource.getString("db.url");
        user = resource.getString("db.user");
        password = resource.getString("db.password");
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);

        return DriverManager.getConnection(url, user, password);
    }

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();

            Properties settings = new Properties();
            settings.put(Environment.DRIVER, driver);
            settings.put(Environment.URL, url);
            settings.put(Environment.USER, user);
            settings.put(Environment.PASS, password);
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
            settings.put(Environment.SHOW_SQL, "true");
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            settings.put(Environment.HBM2DDL_AUTO, "none");

            configuration.setProperties(settings);

            configuration.addAnnotatedClass(User.class);
            configuration.setProperties(settings);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }
}

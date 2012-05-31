package br.com.tg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {
			File file = new File("auxPlot.properties");
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String dialect = prop.getProperty("dialect");
			String driverClass = prop.getProperty("driverClass");
			String url = prop.getProperty("url");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");

			// Cria a SessionFactory do hibernate.cfg.xml
			sessionFactory = new Configuration().configure()
			.setProperty("hibernate.dialect", dialect)
			.setProperty("hibernate.connection.driver_class", driverClass)
			.setProperty("hibernate.connection.url", url)
			.setProperty("hibernate.connection.username", username)
			.setProperty("hibernate.connection.password", password)
			.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Criação da SessionFactory inicial falhou." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
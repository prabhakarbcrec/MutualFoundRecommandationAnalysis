package com.finacular.connectionInstanceofPostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.finacular.constants.ConstantsOfProjects;

public class GetPostgresConnection {
	private static Connection con = null;

	public GetPostgresConnection()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName(ConstantsOfProjects.DRIVER);

		con = DriverManager.getConnection(ConstantsOfProjects.POSTGRES_PATH_TO_CONNECT_LOCAL_DB,
				ConstantsOfProjects.POSTGRES_USER_NAME, ConstantsOfProjects.POSTGRES_PASSWORD);

		System.out.println("New Connection created with Postgres:");
	}

	public static Connection getConnectionInstance()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if (con == null) {
			synchronized (GetPostgresConnection.class) {
				new GetPostgresConnection();

			}
		}

		return con;

	}

}

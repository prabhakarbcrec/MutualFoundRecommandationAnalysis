package com.finacular.connectionInstanceofPostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.finacular.constants.ConstantsOfProjects;

public class ConnectToAwsPostgres {
	private static Connection con = null;

	public ConnectToAwsPostgres()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName(ConstantsOfProjects.DRIVER);

		con = DriverManager.getConnection(ConstantsOfProjects.POSTGRES_PATH_TO_CONNECT_AWS_DB,
				ConstantsOfProjects.POSTGRES_USER_NAME_OF_AWS, ConstantsOfProjects.POSTGRES_PASSWORD_OF_AWS);

		System.out.println("New Connection created with Postgres:");
	}

	public static Connection getConnectionInstance()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if (con == null) {
			synchronized (GetPostgresConnection.class) {
				new ConnectToAwsPostgres();

			}
		}

		return con;

	}
}

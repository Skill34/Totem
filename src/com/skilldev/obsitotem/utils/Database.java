package com.skilldev.obsitotem.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;

import com.skilldev.obsitotem.ObsiTotemPlugin;

public abstract class Database {
	protected Connection connection;

	public abstract Connection openConnection() throws SQLException, ClassNotFoundException;

	public boolean checkConnection() throws SQLException {
		return (this.connection != null) && (!this.connection.isClosed());
	}

	public Connection getConnection() {
		return this.connection;
	}

	public boolean closeConnection() throws SQLException {
		if (this.connection == null) {
			return false;
		}
		this.connection.close();
		return true;
	}

	public ResultSet query(String query) {
		ResultSet result = null;

		try {
			if (!checkConnection()) {
				openConnection();
			}
			Statement statement = connection.createStatement();
			result = statement.executeQuery(query);
		} catch (Throwable t) {
		}
		return result;
	}

	public void queryAsynchronously(final String query, final Callback<ResultSet> callback) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Throwable error = null;
				ResultSet result = null;

				try {
					if (!checkConnection()) {
						openConnection();
					}
					Statement statement = connection.createStatement();
					result = statement.executeQuery(query);
				} catch (Throwable t) {
					error = t;
				}

				callback.call(error, result);
			}
		}.runTaskAsynchronously(ObsiTotemPlugin.getInstance());
	}

	public int updateSQL(String query) throws SQLException, ClassNotFoundException {
		if (!checkConnection()) {
			openConnection();
		}
		Statement statement = this.connection.createStatement();

		int result = statement.executeUpdate(query);

		return result;
	}

	public void updateAsynchrounously(final String line) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					updateSQL(line);
				} catch (SQLException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(ObsiTotemPlugin.getInstance());
	}
}

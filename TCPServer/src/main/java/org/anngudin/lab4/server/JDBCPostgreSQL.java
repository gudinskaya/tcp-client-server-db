package org.anngudin.lab4.server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anngudin.lab4.server.MilitaryClient;
import org.anngudin.lab4.server.MilitaryOrder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class JDBCPostgreSQL {
	
	private Connection connection;

	public JDBCPostgreSQL(String url, String user, String pass) {
		System.out.println("Testing connection to PostgreSQL JDBC");

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
			e.printStackTrace();
			return;
		}
		System.out.println("PostgreSQL JDBC Driver successfully connected");

		try {
			connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			System.out.println("Connection Failed");
			e.printStackTrace();
			return;
		}
	}

	public List<HashMap<String, String>> addClients() throws SQLException{
		List<HashMap<String, String>> militaryClients = new ArrayList<HashMap<String, String>>();	
			Statement stmt;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM clients ORDER BY id");
			
			
			while (rs.next()) {
			HashMap<String, String> militaryClient = new HashMap<String, String>();	
                String id = rs.getString("id");
				String TIN = rs.getString("TIN");
				String country = rs.getString("country");
				militaryClient.put("id", id); 
				militaryClient.put("TIN", TIN); 
				militaryClient.put("country", country);
				militaryClients.add(militaryClient); 
					
			System.out.println(id + "   " + TIN + "    " + country);
	
		}
		return militaryClients;
	}
	public List<HashMap<String, String>> addOrders() throws SQLException{
		List<HashMap<String, String>> militaryOrders = new ArrayList<HashMap<String, String>>();	
			Statement stmt;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM orders ORDER BY id");
			
			
			while (rs.next()) {
			HashMap<String, String> militaryOrder = new HashMap<String, String>();	
			String id = rs.getString("id");
			String type = rs.getString("type");
			String subtype = rs.getString("subtype");
			String marking = rs.getString("marking");
			String client_id = rs.getString("client_id");
				militaryOrder.put("id", id); 
				militaryOrder.put("type", type); 
				militaryOrder.put("subtype", subtype); 
				militaryOrder.put("marking", marking); 
				militaryOrder.put("client_id", client_id);
				militaryOrders.add(militaryOrder); 
					
			System.out.println(id + "  " + type + "  " + subtype + "  " + marking + "  " + client_id);
			
		}
		return militaryOrders;
	}

	public void printClients() {

		try {
			System.out.println("You successfully connected to database now");
			Statement stmt;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM clients");
			System.out.println("id  TIN  country");

			while (rs.next()) {
				int id = rs.getInt("id");
				String TIN = rs.getString("TIN");
				String country = rs.getString("country");
				System.out.println(id + "   " + TIN + "    " + country);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void printOrders() {

		try {
			System.out.println("You successfully connected to database now");
			Statement stmt;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM orders");
			System.out.println("id  type	  subtype  marking  client_id");

			while (rs.next()) {
				int id = rs.getInt("id");
				String type = rs.getString("type");
				String subtype = rs.getString("subtype");
				String marking = rs.getString("marking");
				String client_id = rs.getString("client_id");

				System.out.println(id + "  " + type + "  " + subtype + "  " + marking + "  " + client_id);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public String addClient(String TIN, String country) {
		
		PreparedStatement statement = null;
		String response = "";
		try {
			statement = connection.prepareStatement(String.format("INSERT INTO clients(tin, country) VALUES (%s, '%s')", TIN, country));
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return response;
	}

	public String addOrder(String type, String subtype, String marking, String clientId) {
		PreparedStatement statement = null;
		String response = String.format("Order (%s %s %s %s) is added", type, subtype, marking, clientId);
		try {
			statement = connection.prepareStatement("INSERT INTO orders(type, subtype, marking, client_id ) VALUES (?, ?, ?, ?)");
			statement.setString(1, type);
			statement.setString(2, subtype);
			statement.setString(3, marking);
			statement.setString(4, clientId);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return response;
	}

	public String deleteOrdersById(String id) {
		PreparedStatement statement = null;
		String response = String.format("Order %s is deleted", id);
		try {
			statement = connection.prepareStatement(String.format("DELETE FROM orders WHERE id = %s", id));
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return response;
	}
	public String deleteOrdersByClientId(String id) {
		PreparedStatement statement = null;
		String response = String.format("Order %s is deleted", id);
		try {
			statement = connection.prepareStatement(String.format("DELETE FROM orders WHERE client_id = '%s'", id));
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return response;
	}

	public String deleteClientsById(String id) {
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;
		String response = String.format("Client %s is added", id);
		try {
			statement1 = connection.prepareStatement(String.format("DELETE FROM clients WHERE id = %s", id));
			statement1.executeUpdate();
			statement2 = connection.prepareStatement(String.format("DELETE FROM orders WHERE client_id = %s", id));
			statement2.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return response;
	}
	public String changeDataInTable(String table, String column, String value, String id) {
		PreparedStatement statement = null;
		
		try{
			statement = connection.prepareStatement(String.format("UPDATE %s SET %s = '%s' WHERE id = %s", table, column, value, id));
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return String.format("%s is changed", table);
		}
	}


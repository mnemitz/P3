package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Venue {
	int id;
	String name;
	String city;
	String type;
	String description;
	String weblink;
	String email;
	String address;

	public Venue(int id, String name, String city, String type, String description, String weblink, String email, String address)
	{
		this.id = id;
		this.name = name;
		this.city = city;
		this.type = type;
		this.description = description;
		this.weblink = weblink;
		this.email = email;
		this.address = address;
	}
}


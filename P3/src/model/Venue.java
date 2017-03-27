package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Venue {
	public int id;
	public String name;
	public String city;
	public String type;
	public String description;
	public String weblink;
	public String email;
	public String address;

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

	public Venue(String name, String city, String type, String description, String weblink, String email, String address)
	{
		this.id = -1; // for insertions where no id is provided
		this.name = name;
		this.city = city;
		this.type = type;
		this.description = description;
		this.weblink = weblink;
		this.email = email;
		this.address = address;
	}
}


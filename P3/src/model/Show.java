package model;



import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Show
{
	public int id;
	public String name;
	public String date;
	public String time;
	public String venueName;
	public String[] bands;
	public ArrayList<Integer> bandIDs;
	public boolean isPublic;


	public Show(int id, String name, String date, String time, String venueName, boolean isPublic)
	{
		// constructor for retrievals
		this.id = id;
		this.name = name;
		this.date = date;
		this.time = time;
		this.venueName = venueName;
		this.isPublic = isPublic;

	}

	public Show(String name, String date, String time, String venueName, boolean isPublic, String[] bands)
	{
		this.id = -1; // for insertions
		this.name = name;
		this.date = date;
		this.time = time;
		this.venueName = venueName;
		this.isPublic = isPublic;
		this.bands = bands;
		this.bandIDs = new ArrayList<>();
	}

	public String insertionQuery(Integer venueId)
	{
		String ret = "INSERT INTO show(public, showname, showdate, showtime) VALUES('" +
				((Boolean)isPublic).toString() + "', '" + name + "', '" + date + "', '" + time + "');" +
				"INSERT INTO hosts(venueid, showid) VALUES(" + venueId.toString() + ", currval('show_showid_seq'::regclass));";
				for(Integer bandid : bandIDs)
				{
					ret += "INSERT INTO plays(showid, bandid) VALUES(currval('show_showid_seq'::regclass), " + bandid.toString() + ");";
				}
		return ret;
	}

	public void addBandID(Integer pID)
	{
		bandIDs.add(pID);
	}
}
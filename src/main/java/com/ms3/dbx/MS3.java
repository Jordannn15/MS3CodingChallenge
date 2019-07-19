package com.ms3.dbx;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MS3
{
	private Connection connection = null;
	private final String URL = "jdbc:sqlite::memory:";

	private final String CSV_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "ms3Interview.csv";
	private final String BAD_DATA_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "bad-data-";
	private final String BAD_DATA_EXT = ".csv";

	private DateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
	private String badDataFilename = BAD_DATA_PATH + df.format(new Date()) + BAD_DATA_EXT;

	private final String LOG_FILE_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Statistics.log";

	private int recordsReceived = 0;
	private int recordsSuccessful = 0;
	private int recordsFailed = 0;

	static
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
		}

		catch (ClassNotFoundException e)
		{
			throw new ExceptionInInitializerError(e);
		}
	}

	// Opens connection to in-memory database
	private void openConnection() throws SQLException
	{
		if (connection == null || connection.isClosed())
		{
			connection = DriverManager.getConnection(URL);
			System.out.println("\nConnection to in-memory database established!");
		}
	}

	// Closes connection to database
	private void closeConnection() throws SQLException
	{
		connection.close();
		System.out.print("Database connection closed!");
	}

	// Creates a table named X in database
	private void createTable()
	{
		try
		{
			final Statement statement = connection.createStatement();

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS X"
    				+ "(A		TEXT,"
    				+ " B		TEXT,"
    				+ " C		TEXT,"
    				+ " D		TEXT,"
    				+ " E		TEXT,"
    				+ " F		TEXT,"
    				+ " G		TEXT,"
    				+ " H		TEXT,"
    				+ " I		TEXT,"
    				+ " J		TEXT);");

		}

		catch (SQLException e)
		{
			e.getMessage();
		}
		
		System.out.println("Table X has been created in database.");
	}

	// Reads data from ms3Interview.csv file using OpenCSV
	// If there is a blank column in a row, write it to "bad-data-<timestamp>.csv" file
	// Else insert the row into the database
	// Increment recordsReceived for each row in sample.csv file
	// Increment recordsSuccessful for each row that has every column filled with data
	// Increment recordsFailed for each row that has at least one blank column
	private void insertFromCSV()
	{
		try
		{
			Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
			CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

			Writer writer = Files.newBufferedWriter(Paths.get(badDataFilename));

			CSVWriter csvWriter = new CSVWriter(writer,
					CSVWriter.DEFAULT_SEPARATOR,
					CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END);

			final String[] headerRecord = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
			csvWriter.writeNext(headerRecord);

			PreparedStatement pstatement = connection.prepareStatement("INSERT INTO X(A,B,C,D,E,F,G,H,I,J) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?);");

    			String[] nextRecord;
    			while ((nextRecord = csvReader.readNext()) != null)
    			{
    				recordsReceived++;

    				if (!Arrays.asList(nextRecord).contains(""))
    				{
    					recordsSuccessful++;
	    				pstatement.setString(1, nextRecord[0]);
	    				pstatement.setString(2, nextRecord[1]);
	    				pstatement.setString(3, nextRecord[2]);
	    				pstatement.setString(4, nextRecord[3]);
	    				pstatement.setString(5, nextRecord[4]);
	    				pstatement.setString(6, nextRecord[5]);
	    				pstatement.setString(7, nextRecord[6]);
	    				pstatement.setString(8, nextRecord[7]);
	    				pstatement.setString(9, nextRecord[8]);
	    				pstatement.setString(10, nextRecord[9]);
	    				pstatement.executeUpdate();
    				}

    				else
    				{
    					recordsFailed++;
    					csvWriter.writeNext(nextRecord);
    				}
    			}

    			csvWriter.close();
		}

		catch (IOException e)
		{
			e.getMessage();
		}
		
		catch (SQLException e)
		{
			e.getMessage();
		}
		
		System.out.println("Successful records inserted into database completed.");
		System.out.println("Failed records written to csv file completed.");
	}

	// Log the received, successful, and failed records in a log file
	private void logStats()
	{
		try
		{
			FileWriter fw = new FileWriter(LOG_FILE_PATH);
			fw.write("Records Received: " + recordsReceived + "\n");
			fw.write("Records Successful: " + recordsSuccessful + "\n");
			fw.write("Records Failed: " + recordsFailed);
			fw.close();
		}

		catch (IOException e)
		{
			e.getMessage();
		}
		
		System.out.println("Record statistics written to log file completed.");
	}

	/*
	// Used to test to make sure data is actually in the database
	// Query the database and print everything to make sure the data is actually being inserted
	private void testDB()
	{
		try
		{
			final Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM X;");

			ResultSetMetaData rsmd = rs.getMetaData();
			final int numColumns = rsmd.getColumnCount();

			while(rs.next())
			{
				for (int i = 1; i <= numColumns; i++)
				{
					System.out.print(rs.getString(i) + ",");
				}

				System.out.println("\n");
			}
		}

		catch (SQLException e)
		{
			e.getMessage();
		}
	}*/
	
	public static void main(String[] args) throws SQLException
	{
		MS3 obj = new MS3();
		obj.openConnection();
		obj.createTable();
		obj.insertFromCSV();
		obj.logStats();
		//obj.testDB();
		obj.closeConnection();
	}
}

# MS3CodingChallenge
This program consumes a CSV file, parses the data using OpenCSV, and inserts the records into an in-memory SQLite database. The columns in the database correspond to the columns in the CSV file. Each record is checked to make sure the column count matches the number of columns in the CSV file. If the column count matches, the record is inserted into the database. If the column count doesn't match, the record is written to the 'bad-data-<timestamp>.csv' file. At the end, write the statistics of total number of records received, successful records, and failed records to a log file.
  
# My Approach
1. Open a connection to the SQLite in-memory database
2. Create table 'X' with columns corresponding to the columns in the CSV file
3. Loop through the records in the CSV file
   - Increment the total number of records received
   - If the column count matches, increment the number of successful records and insert the record into the database
   - If the column count doesn't match, increment the number of failed records and write it to the 'bad-data-<timestamp>.csv'
4. Write the total number of records, successful records, and failed records to a log file

# Running the Program
1. Download the src folder, target folder, and pom.xml file
2. Open the terminal/command prompt and go to the directory where you saved the files from step 1
3. Type command: mvn package
4. Type command: mvn clean compile assembly:single
5. Type command: java -cp dbx-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.ms3.dbx.MS3

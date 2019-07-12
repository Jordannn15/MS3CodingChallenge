# MS3CodingChallenge
This program consumes a CSV file, parses the data using OpenCSV, and inserts the records into an in-memory SQLite database. The columns in the database correspond to the columns in the CSV file. Each record is checked to make sure the column count matches the number of columns in the CSV file. If the column count matches, the record is inserted into the database. If the column count doesn't match, the record is written to the 'bad-data-timestamp.csv' file. At the end, write the statistics of total number of records received, successful records, and failed records to a log file.
  
# My Approach
1. Open a connection to the SQLite in-memory database
2. Create table 'X' with columns corresponding to the columns in the CSV file
3. Loop through the records in the CSV file
   - Increment the total number of records received
   - If the column count matches, increment the number of successful records and insert the record into the database
   - If the column count doesn't match, increment the number of failed records and write it to the 'bad-data-<timestamp>.csv'
4. Write the total number of records, successful records, and failed records to a log file

# Running the Program
1. Download the zip file with the src folder, target folder, README.md file, and pom.xml file to your desktop
2. Unzip the file
3. Open the terminal/command prompt and change directory to Desktop/MS3CodingChallenge-master/src/main/java/com/ms3/dbx/
4. Copy the ms3Interview.csv file to your desktop
5. In your terminal/command prompt change directory to Desktop/MS3CodingChallenge-master/
6. Type command: mvn package
7. Type command: mvn clean compile assembly:single
8. In your terminal/command prompt change directory to Desktop/MS3CodingChallenge-master/target/
9. Type command: java -cp dbx-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.ms3.dbx.MS3
10. Output from the program will be printed to your terminal/command prompt and the files created from the program will be on your desktop

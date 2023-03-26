import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/******************************************************************************************************************
* File:MiddleFilter.java
* Project: Lab 1
* Copyright:
*   Copyright (c) 2020 University of California, Irvine
*   Copyright (c) 2003 Carnegie Mellon University
* Versions:
*   1.1 January 2020 - Revision for SWE 264P: Distributed Software Architecture, Winter 2020, UC Irvine.
*   1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
* This class serves as an example for how to use the FilterTemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
* Parameters: None
* Internal Methods: None
 *
 * Modify System A by revising the Middle Filter to filter “wild jumps” out of the data stream for altitude. A
 * wild jump is a variation of more than 100 feet between two adjacent frames. For wild jumps
 * encountered in the stream, replace it with the average of the previous two altitudes. Note that if the
 * wild jump occurs in the second frame (i.e., there is only one previous altitude available), simply replace
 * the current altitude with the previous altitude. The Middle Filter should (1) write the records of wild
 * jumps (with the original value, before replacement) to a text file called WildPoints.csv using the same
 * format as System A, and (2) send the updated data stream to the Sink Filter. Modify the Sink Filter of
 * System A to write the output received from the Middle Filter to a text file called OutputB.csv by
 * formatting the output as shown below, annotating any replaced values with an asterisk:
******************************************************************************************************************/

public class MiddleFilter extends FilterFramework
{
	public void run()
    {
		int bytesread = 0;					// Number of bytes read from the input file.
		int byteswritten = 0;				// Number of bytes written to the stream.
		byte databyte = 0;					// The byte of data read from the file
		int MeasurementLength = 8;        // This is the length of all measurements (including time) in bytes
		int IdLength = 4;                // This is the length of IDs in the byte stream
		long measurement;                // This is the word used to store all measurements - conversions are illustrated.
		int id;                            // This is the measurement id
		int i;                            // This is a loop counter
		GetSetData data = new GetSetData();
		File csvFile = new File("WildPoints.csv");

		// Initiate fileWriter
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(csvFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String toWriteTitle = "    Time                " + "        Velocity     " + "      Altitude    " + "    Pressure     " + "  Temperature    " + "\n";
		try {
			fileWriter.write(toWriteTitle);
			fileWriter.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Sequence of the three altitude, altitude1, altitude2, altitude
		Double altitude1 = Double.valueOf(0);    // previous first altitude
		Double altitude2 = Double.valueOf(0);    // previous second altitude
		Double altitude = Double.valueOf(0);     // current altitude

		// Next we write a message to the terminal to let the world know we are alive...
		System.out.print( "\n" + this.getName() + "::Middle Reading ");

		while (true)
		{
			// Here we read a byte and write a byte
			try
			{
				/***************************************************************************
				 // We know that the first data coming to this filter is going to be an ID and
				 // that it is IdLength long. So we first get the ID bytes.
				 // Then We get the measurement following each ID.
				 ****************************************************************************/
				id = 0;
				for (i = 0; i < IdLength; i++) {
					databyte = ReadFilterInputPort();    // This is where we read the byte from the stream...
					id = id | (databyte & 0xFF);        // We append the byte on to ID...
					if (i != IdLength - 1)                // If this is not the last byte, then slide the
					{                                    // previously appended byte to the left by one byte
						id = id << 8;                    // to make room for the next byte we append to the ID
					}
					bytesread++;                        // Increment the byte count
				}

				measurement = 0;
				for (i = 0; i < MeasurementLength; i++) {
					databyte = ReadFilterInputPort();
					measurement = measurement | (databyte & 0xFF);    // We append the byte on to measurement...
					if (i != MeasurementLength - 1)                    // If this is not the last byte, then slide the
					{                                                // previously appended byte to the left by one byte
						measurement = measurement << 8;                // to make room for the next byte we append to the
						// measurement
					}
					bytesread++;                                    // Increment the byte count
				}

				if (id == 0) {
					data.setTime(measurement);
				}
				if (id == 1) {
					data.setVelocity(measurement);
				}
				if (id == 2) {
					altitude = Double.longBitsToDouble(measurement);
					if (altitude2 == 0) {
						data.setAltitude(measurement);
						data.setIsWildJump(false);
					} else if (altitude2 != 0) {
						double currJump = Math.abs(altitude - altitude2);
						if (currJump <= 100) {
							data.setAltitude(measurement);
							data.setIsWildJump(false);
						} else {
							if (altitude1 == 0) {
								data.setAltitude(Double.doubleToLongBits(altitude2));
								data.setIsWildJump(true);
							} else {
								data.setAltitude(Double.doubleToLongBits((altitude2 + altitude1) / 2));
								data.setIsWildJump(true);
							}
						}
					}
					altitude1 = altitude2;
					altitude2 = altitude;
				}

				if (id == 3) {
					data.setPressure(measurement);
				}
				// "some string".getBytes();
				if (id == 4) {
					data.setTemperature(measurement);
					if (data.IsWildJump) {
						String altitudeString = String.format("%.4f", altitude);
						String toPrintWildJumpFrame = data.getTimeString() + "," + data.getVelocityString() + "," + altitudeString + "," + data.getPressureString() + "," + data.getTemperatureString() + "\n";
						fileWriter.write(toPrintWildJumpFrame);
						fileWriter.flush();
					}
					String toPrint = data.getPrintString();
					byte[] output = toPrint.getBytes("UTF-8");
					for(byte b : output) {
						WriteFilterOutputPort(b);
						byteswritten ++;
					}
				}


			}
			catch (EndOfStreamException e)
			{
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
   }
}
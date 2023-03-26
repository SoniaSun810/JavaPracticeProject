/******************************************************************************************************************
 * File:SinkFilter.java
 * Project: Lab 1
 * Copyright:
 *   Copyright (c) 2020 University of California, Irvine
 *   Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 *   1.1 January 2020 - Revision for SWE 264P: Distributed Software Architecture, Winter 2020, UC Irvine.
 *   1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *
 * Description:
 * This class serves as an example for using the SinkFilterTemplate for creating a sink filter. This particular
 * filter reads some input from the filter's input port and does the following:
 *	1) It parses the input stream and "decommutates" the measurement ID
 *	2) It parses the input steam for measurments and "decommutates" measurements, storing the bits in a long word.
 * This filter illustrates how to convert the byte stream data from the upstream filterinto useable data found in
 * the stream: namely time (long type) and measurements (double type).
 * Parameters: None
 * Internal Methods: None
 ******************************************************************************************************************/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;                        // This class is used to interpret time words
import java.text.SimpleDateFormat;        // This class is used to format and write time in a string format.

public class SinkFilter extends FilterFramework {
    public void run() {
        /************************************************************************************
         *	TimeStamp is used to compute time using java.util's Calendar class.
         * 	TimeStampFormat is used to format the time value so that it can be easily printed
         *	to the terminal.
         *************************************************************************************/

        File csvFile = new File("OutputB.csv");
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

        byte databyte = 0;                // This is the data byte read from the stream
        int bytesread = 0;                // This is the number of bytes read from the stream

        // First we announce to the world that we are alive...
        System.out.print("\n" + this.getName() + "::Sink Reading " );

        while (true)
        {
            // Here we read a byte and write a byte
            try
            {
                databyte = ReadFilterInputPort();
                bytesread++;
                fileWriter.write(databyte);
                fileWriter.flush();
            }
            catch (EndOfStreamException e)
            {
                ClosePorts();
                System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread);
                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    } // run
}
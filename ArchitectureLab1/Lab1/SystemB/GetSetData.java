// Importing input output classes
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class GetSetData {

    // Member variable of this class
    private Long Time;
    private Long Velocity;
    private Long Altitude;
    private Long Pressure;
    private Long Temperature;
    public Boolean IsWildJump;
    Calendar TimeStamp = Calendar.getInstance();
    SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

    // Method - Getter (String)
    public String getPrintString() {
        String toPrint;
        if (this.IsWildJump) {
            toPrint = getTimeString() + "," + getVelocityString() + "," + getAltitudeString() + "*" + "," + getPressureString() + "," + getTemperatureString() + "\n";
        } else {
            toPrint = getTimeString() + "," + getVelocityString() + "," + getAltitudeString() + "," + getPressureString() + "," + getTemperatureString() + "\n";
        }
        return toPrint;
    }
    public String getTimeString() {
        TimeStamp.setTimeInMillis(this.Time);
        String timeString = TimeStampFormat.format(TimeStamp.getTime());
        return timeString;
    }

    public String getVelocityString() {
        return getString(this.Velocity);
    }

    public String getAltitudeString() {
        return getString(this.Altitude);
    }

    public String getPressureString() {
        return getString(this.Pressure);
    }

    public String getTemperatureString() {
        return getString(this.Temperature);
    }


    // Method - Getter (byte array)


    public String getString(long measurement){
        Double number = Double.longBitsToDouble(measurement);
        String numString = Double.toString(number);
        return numString;
    }

    // Method - Setter
    public void setTime(long measurement)
    {
        this.Time = measurement;
    }

    public void setVelocity(long measurement)
    {
        this.Velocity = measurement;
    }

    public void setAltitude(long measurement)
    {
        this.Altitude = measurement;
    }

    public void setPressure(long measurement)
    {
        this.Pressure = measurement;
    }

    public void setTemperature(long measurement)
    {
        this.Temperature = measurement;
    }

    public void setIsWildJump(Boolean flag){
        this.IsWildJump = flag;
    }


}

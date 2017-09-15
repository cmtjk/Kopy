package de.r3r57.kopy.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class KopyLogger {

    public static Logger logger = Logger.getRootLogger();

    public KopyLogger() throws IOException {
        initLogger();
    }

    private void initLogger() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("./config/log4j.properties"));
        PropertyConfigurator.configure(props);
    }
}

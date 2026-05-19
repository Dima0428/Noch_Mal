package de.fes.nochmal;

import javax.swing.SwingUtilities;

import de.fes.nochmal.gui.MainWindow;
import de.fes.nochmal.util.log.BasicLog;
import de.fes.nochmal.util.log.Log;
import de.fes.nochmal.util.log.SystemOutLogAppender;
import de.fes.nochmal.Configuration; 

public class Main {
    public static final String ProgramNameAndVersion = "Noch mal! Arena 0.5";
    
    public static void main(String[] args) {
        Log log = new BasicLog(SystemOutLogAppender.Instance);
        
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow(new Configuration(log));
            mainWindow.setVisible(true);
        });
    }
}
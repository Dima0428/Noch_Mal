package de.fes.nochmal;

import javax.swing.SwingUtilities;

// Импортируем ВСЕ необходимые классы из вашей Referenced Library
import de.fes.nochmal.gui.MainWindow;
import de.fes.nochmal.util.log.BasicLog;
import de.fes.nochmal.util.log.Log;
import de.fes.nochmal.util.log.SystemOutLogAppender;
// Добавляем импорт для Конфигурации (Eclipse сам подскажет, если путь другой)
import de.fes.nochmal.Configuration; 

public class Main {
    public static final String ProgramNameAndVersion = "Noch mal! Arena 0.5";
    
    public static void main(String[] args) {
        // 1. Создаем логгер из библиотеки
        Log log = new BasicLog(SystemOutLogAppender.Instance);
        
        // 2. Запускаем графическое окно игры
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow(new Configuration(log));
            mainWindow.setVisible(true);
        });
    }
}
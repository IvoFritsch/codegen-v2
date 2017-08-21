/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Ivo Fritsch
 */
public class FileChooser {
    private JFrame frame;
    public FileChooser() {
        frame = new JFrame();
        frame.setTitle("Escolhedor de arquivos do Codegen");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        BringToFront();
    }
    public String getFile(String descricao, String... extensoes) {
        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            return null;
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Escolhedor de arquivos do Codegen");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(descricao, extensoes);
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);
        if(JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)){
            frame.setVisible(false);
            frame.dispose();
            return fc.getSelectedFile().toString();
        }else {
            frame.dispose();
            return "";
        }
    }

    private void BringToFront() {                  
        frame.setExtendedState(JFrame.ICONIFIED);
        frame.setExtendedState(JFrame.NORMAL);
    }

}

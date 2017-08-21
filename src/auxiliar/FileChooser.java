/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
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
        if(JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)){
            frame.setVisible(false);
            return fc.getSelectedFile().toString();
        }else {
            return null;
        }
    }

    private void BringToFront() {                  
        frame.setExtendedState(JFrame.ICONIFIED);
        frame.setExtendedState(JFrame.NORMAL);

    }

}

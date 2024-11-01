package org.main.frontend.ui;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.sql.SQLException;

import org.main.backend.Operation;
import org.main.cryptomanager.Crypto;
import org.main.main.App;
// import org.main.backend.records.Memory;
import org.main.backend.records.Ressponse;
import org.worm.CRUD;
import org.main.backend.models.Memory;


public class MemoryController {

    private App ui;
    private Crypto cr;
    private Operation op;
    private CRUD<Memory> memo;
    @FXML
    private HTMLEditor memoryEditor;
    @FXML
    private Button memoButton;
    @FXML
    private TextField activeMemo;
    private StringBuilder lastSaved = new StringBuilder();
    @FXML
    private TextField memoryTitle;
   

    @FXML
    private void saveMemory()
    {
            Integer activeId = Integer.parseInt(activeMemo.getText());
            String content = Helper.extractBodyContent(memoryEditor.getHtmlText());
            System.out.println(content);
            String title = memoryTitle.getText();
            // content = content.substring(+4,content.length());
            String titleInHtml = "<h1 style=\"text-align: center;\"><span style=\"font-family: &quot;&quot;;\">"+title+"</span></h1>";
            if(content.indexOf(titleInHtml)==-1)
                content = titleInHtml+content;
            String memory = cr.encryptText(content);
            title = cr.encryptText(title);
            Memory mem = new Memory(title,memory,"da","dd");
            System.out.println("Title: "+title);
            // System.out.println("memory: "+memory+"  VS  "+memoryTitle.getText())
            if(activeId>0)
                mem.setId(activeId);
            else
                Helper.setBodyContent(content, memoryEditor);
            // long id = op.insertRecord(title,memory,123);
            Integer id = memo.save(mem);
            lastSaved.setLength(0);
            lastSaved.append(content+memoryTitle.getText());
            if(id>0)
            {
                activeMemo.setText(id+"");
            }
            else
            {
                ui.showMessage("ERROR", "Unable to save Memory");
            }


    }

    @FXML
    private void nextMemory()
    {
        if(!checkChangesAndProceed())
            return;
        Integer id = Integer.parseInt(activeMemo.getText());
        Memory mem = this.memo.findById(id+1);
        if(mem!=null)
        {
            
            Helper.setBodyContent(cr.decryptText(mem.getContent()), memoryEditor);
            activeMemo.setText((id+1)+"");
            lastSaved.setLength(0);
            lastSaved.append(Helper.extractBodyContent(memoryEditor.getHtmlText())+memoryTitle.getText());
        }
    }
    @FXML
    private void preMemory()
    {
        if(!checkChangesAndProceed())
            return;
        Integer id = Integer.parseInt(activeMemo.getText());
        if(id<=0)
            return;
        Memory mem = this.memo.findById(id-1);
        if(mem!=null)
        {
            
            Helper.setBodyContent(cr.decryptText(mem.getContent()), memoryEditor);
            activeMemo.setText((id-1)+"");
            lastSaved.setLength(0);
            lastSaved.append(Helper.extractBodyContent(memoryEditor.getHtmlText())+memoryTitle.getText());
        }   
    }

    public void initResources(App ui,Crypto cr) throws SQLException
    {
        this.ui = ui;
        this.cr = cr;
        this.activeMemo.setVisible(false);
        this.memo = new CRUD<>(Memory.class);
    }
    private static String removeBodyContent(String html) {
        // Regex to find <body ...>...</body> and replace its content
        return html.replaceAll("(?i)(<body[^>]*>)([\\s\\S]*?)(</body>)", "$1$3");
    }
    
    private boolean checkChangesAndProceed()
    {
        Integer id = Integer.parseInt(activeMemo.getText());
        System.out.println("NExt clicked");
        if(!lastSaved.toString().equals(Helper.extractBodyContent(memoryEditor.getHtmlText())+memoryTitle.getText()))
        {
           int res = ui.showYesNoDialog("Unasaved Changes", "Save Changes...?","You have unsaved Changes, Save Changes..?");
           if(res==1)
           {
                this.saveMemory();
                return true;
           }
           else if(res==2)
                return false;
            
        }
        return true;
    }

    @FXML
    private void returnToMainMenu()
    {
        ui.loadMainMenuScreen(cr);
    }
}

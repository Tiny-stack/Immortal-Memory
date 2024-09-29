package org.main.frontend.ui;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.SQLException;
import org.main.App;
import org.main.backend.Operation;
import org.main.cryptomanager.Crypto;
// import org.main.backend.records.Memory;
import org.main.backend.records.Ressponse;
import org.main.backend.victororm.CRUD;
import org.main.backend.models.Memory;


public class MemoryController {

    private App ui;
    private Crypto cr;
    private Operation op;
    private CRUD<Memory> memo;
    @FXML
    private TextArea memoryEditor;
    @FXML
    private TextField memoryTitle;
    @FXML
    private Button memoButton;
    @FXML
    private TextField activeMemo;

   

    @FXML
    private void saveMemory()
    {
            String title = cr.encryptText(memoryTitle.getText());
            String memory = cr.encryptText(memoryEditor.getText());
            Memory mem = new Memory(title,memory,"da","dd");
            System.out.println("Title: "+title);
            System.out.println("memory: "+memory+"  VS  "+memoryTitle.getText());
            Integer activeId = Integer.parseInt(activeMemo.getText());
            if(activeId>0)
                mem.setId(activeId);
            // long id = op.insertRecord(title,memory,123);
            Integer id = memo.save(mem);
            if(id>0)
            {
                memoButton.setText("Update Memory");
                activeMemo.setText(id+"");
            }
            else
            {
                ui.showMessage("ERROR", "Unable to save Memory");
            }


    }
    @FXML
    private void loadMemory()
    {
        activeMemo.setText("6");
        System.out.println("load clicked");

        Memory mem = this.memo.findById(6);
        if(mem!=null)
        {
            memoryTitle.setText(cr.decryptText(mem.getTitle()));
            memoryEditor.setText(cr.decryptText(mem.getContent()));
            memoButton.setText("Update Memory");
        }

    }

    @FXML
    private void nextMemory()
    {
        Integer id = Integer.parseInt(activeMemo.getText());
        System.out.println("NExt clicked");
        Memory mem = this.memo.findById(id+1);
        if(mem!=null)
        {
            memoryTitle.setText(cr.decryptText(mem.getTitle()));
            memoryEditor.setText(cr.decryptText(mem.getContent()));
            activeMemo.setText((id+1)+"");
        }
    }
    @FXML
    private void preMemory()
    {
        Integer id = Integer.parseInt(activeMemo.getText());
        if(id<=0)
            return;
        Memory mem = this.memo.findById(id-1);
        if(mem!=null)
        {
            memoryTitle.setText(cr.decryptText(mem.getTitle()));
            memoryEditor.setText(cr.decryptText(mem.getContent()));
            activeMemo.setText((id-1)+"");
        }   
    }

    public void initResources(App ui,Crypto cr) throws SQLException
    {
        this.ui = ui;
        this.cr = cr;
        this.activeMemo.setVisible(false);
        this.memo = new CRUD<>(Memory.class);
    }
    
}

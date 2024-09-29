package org.main.frontend.ui;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import org.main.App;
import org.main.backend.Operation;
import org.main.cryptomanager.Crypto;
import org.main.backend.records.Memory;
import org.main.backend.records.Ressponse;

public class MemoryController {

    private App ui;
    private Crypto cr;
    private Operation op;

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
        if(Long.parseLong(activeMemo.getText())==0l)
        {
            String title = cr.encryptText(memoryTitle.getText());
            String memory = cr.encryptText(memoryEditor.getText());
            System.out.println("Title: "+title);
            System.out.println("memory: "+memory+"  VS  "+memoryTitle.getText());
            long id = op.insertRecord(title,memory,123);
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
        else
        {
            long id = Long.parseLong(activeMemo.getText());
            String title = cr.encryptText(this.memoryTitle.getText());
            String content = cr.encryptText(this.memoryEditor.getText());
            op.updateMemo(new Memory(title, content,123, id));
            System.out.println("UPDATED");
        }

    }
    @FXML
    private void loadMemory()
    {
        activeMemo.setText("6");
        System.out.println("load clicked");

        Memory memo = op.readMemory(6l);
        if(memo!=null)
        {
            memoryTitle.setText(cr.decryptText(memo.getTitle()));
            memoryEditor.setText(cr.decryptText(memo.getContent()));
            memoButton.setText("Update Memory");
        }

    }

    @FXML
    private void nextMemory()
    {
        long id = Long.parseLong(activeMemo.getText());
        System.out.println("NExt clicked");
        Memory memo = op.readMemory(id+1);
        if(memo!=null)
        {
            memoryTitle.setText(cr.decryptText(memo.getTitle()));
            memoryEditor.setText(cr.decryptText(memo.getContent()));
            activeMemo.setText((id+1)+"");
        }
    }
    @FXML
    private void preMemory()
    {
        long id = Long.parseLong(activeMemo.getText());
        if(id<=0)
            return;
        Memory memo = op.readMemory(id-1);
        if(memo!=null)
        {
            memoryTitle.setText(cr.decryptText(memo.getTitle()));
            memoryEditor.setText(cr.decryptText(memo.getContent()));
            activeMemo.setText((id-1)+"");
        }   
    }

    public void initResources(App ui,Operation op,Crypto cr)
    {
        this.ui = ui;
        this.op = op;
        this.cr = cr;
        this.activeMemo.setVisible(false);
        System.out.println("OP: "+op);
    }
    
}

package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController implements Initializable {
    public Server serverSenders;
    public Server serverListeners;
    public InformArea area;
    public boolean isReady;
    
    @FXML private TextArea textArea;
    @FXML private TextField psField;
    @FXML private TextField plField;
    @FXML private TextField numberField;
    @FXML private Button startButton;
    @FXML public Button gameButton;
    
    @FXML
    private void startServer(ActionEvent event) {
        int portSen = Integer.parseInt(this.psField.getText());
        int portLis = Integer.parseInt(this.plField.getText());
        int number = Integer.parseInt(this.numberField.getText());
        
        GetSenders senders = new GetSenders(portSen, number, this);        
        new Thread(senders).start();
        
        GetListeners listeners = new GetListeners(portLis, number, this);        
        new Thread(listeners).start();
        
        area.addToArea("Server has started");
        area.addToArea("Server is waiting for players");
        
        this.startButton.setDisable(true);
    }
    
    @FXML
    private void startGame(ActionEvent event) throws FileNotFoundException, IOException {
        int number = this.serverSenders.Players.size();
        this.gameButton.setDisable(true);
        new Game(number, this.area, this.serverSenders.Players, this.serverListeners.Players);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.area = new InformArea(textArea);
        this.gameButton.setDisable(true);
        this.isReady = false;
    }    

    public static class InformArea {
        private TextArea textArea;
        private String area;

        private InformArea(TextArea textArea) {
            this.area = "";
            this.textArea = textArea;
        }
        
        public TextArea getTextArea()
        {
            return this.textArea;
        }
        
        public void addToArea(String text)
        {
            if(this.area == "")
            {
                this.area = text;
            }
            else
            {
                this.area += "\n" + text;
            }
            
            this.textArea.setText(area);
        }
    }
}

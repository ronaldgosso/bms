package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PlayerController implements Initializable {
	private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(AdminController.class.getResource("/Music/song.wav").toString());
    @FXML private MediaView mView;
    @FXML Slider volume;
    private  MediaPlayer mp;
    private Media me;
    private FileChooser fc;
    private File open;
    
    public void Choose(ActionEvent event){
    	fc = new FileChooser();
    	//fc.setInitialDirectory(new File("C:\\Users\\user\\Documents"));
    	try{
    		open = fc.showOpenDialog(null);//open to choose file
        	String video = open.getAbsolutePath();
        	String path = new File(video).getAbsolutePath();
    	    me = new Media(new File(path).toURI().toString());
    	    mp = new MediaPlayer(me);
    	    mView.setMediaPlayer(mp);
    	    
    	    //volume slider set up
    	    volume.setValue(mp.getVolume() * 25);
    	    volume.valueProperty().addListener(new InvalidationListener(){

				@Override
				public void invalidated(Observable arg0) {
				  mp.setVolume(volume.getValue()/100);
					
				}
    	    	
    	    });
    	}catch(Exception e){
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ALERT");
			alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
			alert.setContentText("System Error");
			alert.setHeaderText(null);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
			alert.showAndWait();
    	}
    	
    }
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    
    	
    	//set the video to Media view in Border Pane
	   // mp.setAutoPlay(true);//auto play video the video on launching
	    //we center the video in maximizing window
	    DoubleProperty width = mView.fitWidthProperty();
	    DoubleProperty height = mView.fitHeightProperty();
	    width.bind(Bindings.selectDouble(mView.sceneProperty(), "width"));
	    height.bind(Bindings.selectDouble(mView.sceneProperty(), "height"));
	}
     public void play(ActionEvent event){
    	 mp.play();//play the video
    	 mp.setRate(1);//default frame rate
     }
     public void pause(ActionEvent event){
    	 mp.pause();//pause the video
     }
     public void fast(ActionEvent event){
    	 mp.setRate(2.0);//incremented from normal 1.0 value
     }
     public void slow(ActionEvent event){
    	 mp.setRate(0.5);//decreased from 1 <slower rate>
     }
     @SuppressWarnings("static-access")
	public void repeat(ActionEvent event){
    	// mp.seek(mp.getStartTime());//reload the video set it to the start time then play
    	// mp.play();
    	 mp.setCycleCount(mp.INDEFINITE);
     }
     
     public void start(ActionEvent event){
    	 mp.seek(mp.getStartTime());//get the time then stop running and start
    	 mp.stop();
    	 mp.play();
     }
     public void last(ActionEvent event){
    	 mp.seek(mp.getTotalDuration());
    	 mp.stop();
     }
     public void stop(ActionEvent event){
    	 mp.stop();
     }
     public void exit(ActionEvent event)throws Exception{
    	 if(mView.isVisible()) {
    		 mp.stop();
        	 ((Node)event.getSource()).getScene().getWindow().hide();
    	 }else {
    		 
        	 ((Node)event.getSource()).getScene().getWindow().hide();
    	 }
    	
    	
     }
     public void dragAndPlay(ActionEvent event){
    	 String video = open.getAbsolutePath();
     	String path = new File(video).getAbsolutePath();
 	    me = new Media(new File(path).toURI().toString());
 	    mp = new MediaPlayer(me);
 	    mView.setMediaPlayer(mp);
 	    
 	    //volume slider set up
 	    volume.setValue(mp.getVolume() * 25);
 	    volume.valueProperty().addListener(new InvalidationListener(){

				@Override
				public void invalidated(Observable arg0) {
				  mp.setVolume(volume.getValue()/100);
					
				}
 	    	
 	    });
 	    mp.play();
     }
	public void errorDetection(ActionEvent event){
	PlayerController.ALERT_AUDIOCLIP.play();
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ALERT");
		alert.setGraphic(new ImageView(this.getClass().getResource("/img/Delete-icon.png").toString()));
		alert.setContentText("Please use .mp4 files for Videos and ,.mp3 files for Audio");
		alert.setHeaderText(null);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/img/baga.png").toString()));
		alert.showAndWait();
	}

}

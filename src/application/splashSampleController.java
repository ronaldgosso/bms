package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class splashSampleController implements Initializable {
	@FXML
	private ImageView splashView;
	@FXML
	private ImageView udsmLogo;
	@FXML
	private ImageView bagaLogo;
	@FXML
	private StackPane rootPane;
	@FXML
	private ImageView rotate;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		splashView.setImage(new Image(getClass().getResource("/img/splash.jpg").toString()));
		rotate.setImage(new Image(getClass().getResource("/img/earth.gif").toString()));
		udsmLogo.setImage(new Image(getClass().getResource("/img/node.png").toString()));
		bagaLogo.setImage(new Image(getClass().getResource("/img/baga.png").toString()));

	}

}

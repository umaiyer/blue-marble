package hellofx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.time.LocalDate;

import javax.imageio.ImageIO;

import org.curiousworks.BlueMarble;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BlueMarbleController {
	boolean isEnhanced=false;
	BufferedImage imgBuf=null;	

	@FXML
	private ImageView image;

	@FXML
	private DatePicker datePicker;
	
	@FXML
	private ToggleButton toggleEnhance;
	
	@FXML
	private Button displayBlackAndWhite;
	
	@FXML
	private Alert notAvailableAlert;

	@FXML
	void updateDate(ActionEvent event) {
		isEnhanced = false;
		updateImage(false);
	}

	@FXML
	void displayEnhanced(ActionEvent event) {
		if (!isEnhanced) {
			updateImage(true);
			isEnhanced=true;
		}			
		else {
			updateImage(false);
			isEnhanced=false;
		}
		
	}
	
	@FXML
	void convertBW(ActionEvent event) {
		if (imgBuf == null) return;
		BufferedImage convBuf = new BufferedImage(imgBuf.getWidth(), imgBuf.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D graphic = convBuf.createGraphics();
		graphic.drawImage(imgBuf, 0, 0, Color.WHITE,null);
		graphic.dispose();
		image.setImage(SwingFXUtils.toFXImage(convBuf, null));
	}
	
	private void updateImage(boolean enhance) {
		BlueMarble blueMarble = new BlueMarble();
		blueMarble.setDate(datePicker.getValue().getYear() + "-0" + datePicker.getValue().getMonthValue() + "-" + datePicker.getValue().getDayOfMonth());
		if (datePicker.getValue().isAfter(LocalDate.now())) {
			notAvailableAlert = new Alert(AlertType.ERROR);
			notAvailableAlert.setContentText("Please select dates in the past. Cannot retrieve images for dates in the future");
			notAvailableAlert.showAndWait();
			return;			
		}		
		else if (datePicker.getValue().getYear() > 2018 || (datePicker.getValue().getMonthValue() > 6 && datePicker.getValue().getYear() == 2018 ))
			toggleEnhance.setDisable(true);
		else
			toggleEnhance.setDisable(false);
		
		if (enhance) blueMarble.setEnhanced(enhance);
		
//		Image value = new Image(BlueMarble.getMostRecentImage());
		try { 
			InputStream imgInputStream = blueMarble.getImage();
			// Update imgBuf to most recent file
			imgBuf = ImageIO.read(imgInputStream);
			image.setImage(new Image(blueMarble.getImage()));
		} catch (Exception e) {
			e.printStackTrace();
			notAvailableAlert = new Alert(AlertType.ERROR);
			notAvailableAlert.setContentText("Error retrieving image");
			notAvailableAlert.showAndWait();
			return;
		}
		
	}

}

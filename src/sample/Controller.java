package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import misc.SkinGetter;
import misc.UUIDGetter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Controller {

    public Controller() {}

    public Button cancel;
    public Button search;
    public Button save;
    public ImageView skinImage;
    public ImageView skinRender;
    public TextField textfield;
    public Label skinOf;
    public Label notify;
    private String uuid;
    public BufferedImage image;

    public void onCancel(ActionEvent event) {
        if (event.getSource() == cancel) {
            Main.closeAction();
        }
    }

    public void onEnter(ActionEvent event) {
        if(event.getSource() == textfield || event.getSource() == search) {
            notify.setTextFill(Color.web("#FF0000"));
            String textfieldText = textfield.getText();
            if (textfieldText.length() < 4 || textfieldText.length() > 16) {
                notify.setText("Name must be 4-16 characters long!");
                skinOf.setText("");
                skinImage.setImage(null);
                return;
            }
            if (!textfieldText.matches("^[a-zA-Z0-9_]+$")) {
                notify.setText("Invalid characters!");
                skinOf.setText("");
                skinImage.setImage(null);
                return;
            }
            textfield.clear();
            try {
                uuid = UUIDGetter.getUUID(textfieldText);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (uuid == null) {
                notify.setText("Player doesn't exist!");
                skinImage.setImage(null);
                return;
            }
            notify.setText("");
            skinOf.setText("");
            image = SkinGetter.getSkinAsBufferedImage(notify, skinImage, uuid);
            BufferedImage skinRenderIMG = SkinGetter.getSkinRender(uuid);
            if (image != null) {
                skinOf.setText("Skin of " + UUIDGetter.getNameFromUUID(uuid));
                skinImage.setImage(SwingFXUtils.toFXImage(image, null));
                skinRender.setImage(SwingFXUtils.toFXImage(skinRenderIMG, null));
            }
        }
    }

    public void onSave(ActionEvent event) {
        if (event.getSource() == save) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG", ".png");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(Main.window);
            if (file == null) {
                notify.setTextFill(Color.web("#FF0000"));
                notify.setText("Please choose a valid file path!");
                return;
            } else if (skinImage.getImage() == null) {
                notify.setTextFill(Color.web("#FF0000"));
                notify.setText("You have not chosen a skin yet!");
                return;
            }
            try {
                ImageIO.write(image, "png", file);
                Main.closeAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import me.is103t4.corendonluggagesystem.database.tasks.RecoverPasswordTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.Scenes;

/**
 * FXML Controller class
 *
 * @author Finn Bon
 */
public class RecoverPasswordController extends Controller {

    @FXML
    private TextField emailField;

    @FXML
    private Button recoverButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void goToLogin() {
	Scenes.LOGIN.setToScene();
    }

    @FXML
    private void recoverPassword() {
	recoverButton.setDisable(true);
	errorLabel.setVisible(false);
	String address = emailField.getText();
	if (!validate(address)) {
	    recoverButton.setDisable(false);
	    errorLabel.setVisible(true);
	    errorLabel.
		    setText("That is not a valid email. A valid email contains at least one '@' and one '.'!");
	    return;
	}
	RecoverPasswordTask task = new RecoverPasswordTask(address);
	task.setOnSucceeded(value -> {
	    int result = (Integer) task.getValue();
	    if (result == -1) {
		recoverButton.setDisable(false);
		errorLabel.setVisible(true);
		errorLabel.
			setText("That email could not be found in our database. Are you sure you've typed it correctly?");
	    } else {
		Scenes.RECOVERY_CONFIRMATION.setToScene();
		((PasswordResetController) Scenes.RECOVERY_CONFIRMATION.
			getController()).setRecoverInfo(result, address);
	    }
	});
	task.setOnCancelled(value -> {
	    recoverButton.setDisable(false);
	    errorLabel.setVisible(true);
	    errorLabel.
		    setText("Something went wrong. Please try again.");
	});
	task.setOnFailed(value -> {
	    recoverButton.setDisable(false);
	    errorLabel.setVisible(true);
	    errorLabel.
		    setText("Something went wrong. Please try again.");
	});
    }

    private boolean validate(String address) {
	return address.contains("@") && address.contains(".");
    }

}

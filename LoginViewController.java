package controller.view;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;


import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import controller.Functions;
import controller.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginViewController {
	@FXML
	public String Identification;
	
	@FXML
	private void Accueil () throws IOException{
		Main.showAccueil(Identification);
		Main.IDCME=Identification.substring(0,1)+index;
	}
	@FXML
	private TextField NameField;
	@FXML
	private TextField PasswordField;
	@FXML
	private Label Errormessage;
	@FXML
	private Label Connectionmessage;
	@FXML
	private Button Logger;
	@FXML
	private InputStream fileInputStream;
	@FXML
	private XSSFWorkbook workbook;
	@FXML
	private XSSFSheet worksheet;
	int index=1;
	
	@FXML
	//Récupère les ID
	private void initialize() throws IOException, EncryptedDocumentException, InvalidFormatException{
		fileInputStream = new FileInputStream("Donnée/excel/ID.xlsm");
		workbook = null;
		String path= getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		if(!path.startsWith("/G:/MDV/VE/BOITE%20A%20OUTILS%20VE_CME/02-OUTILS%20COTATIONS/")){
			
		}
	}

	@FXML
	//Active l'action de Login en appuyant sur Entrer
	public void handleEnterPressed(KeyEvent event) throws IOException, GeneralSecurityException{
	    if (event.getCode() == KeyCode.ENTER) {
	    	Login();
	    }
	}
	@FXML
	public void Login() throws IOException, GeneralSecurityException {
		//déverrouille la feuille ID
		if(workbook == null){
		workbook = new XSSFWorkbook(fileInputStream);
		workbook.getSheet("Identifiants").validateSheetPassword("latecoere");
		worksheet = workbook.getSheet("Identifiants");
		}
		
		index=0;
		//Récupère les infos sur la feuille
		
		int bot = worksheet.getLastRowNum();
		Row row = worksheet.getRow(index);
		Cell FirstName = row.getCell(0); 
		Cell Name = row.getCell(1); 
		Cell ID = row.getCell(2); 
		Cell Password = row.getCell(3);
		String Securite="";
		String namecheck = ID.getStringCellValue();
		String Passcheck = Password.getStringCellValue();
		//Récupère les infos entré par l'utilisateur
		String utilisateur = NameField.getText();
		String pass = PasswordField.getText();
		//crypte le mot de passe entré
		String password = Functions.sha1(pass);
		//vérification des ID
		while (index < bot+1 && !utilisateur.equals(namecheck)) {
			if (!utilisateur.equals(namecheck)){
				row = worksheet.getRow(index);
				FirstName = row.getCell(0); 
				Name = row.getCell(1); 
				ID = row.getCell(2); 
				Password = row.getCell(3);
				Securite = row.getCell(4).getStringCellValue();
				namecheck = ID.getStringCellValue();
				Passcheck = Password.getStringCellValue();
				
				index= index + 1;
				}
			
			Identification=ID.getStringCellValue();
			
			}
		//dans le cas ou la cellule identifiant est vide
		if (utilisateur.equals("")){
			Errormessage.setText("Please enter your login");
		}
		else {
			Password = row.getCell(3);
			Passcheck = Password.getStringCellValue();
			//Si l'identifiant est dan sla liste d'ID
			if (utilisateur.equals(namecheck) && !namecheck.equals(null)){
				//Si le mdp correspond
				if (Passcheck.equals(password)){
					Main.primaryStage.setTitle("Bienvenue sur Sphere, "+FirstName+" "+Name);
					Main.Securite=Securite;
					System.out.println(Main.Securite);
					Accueil();
					}
				else {Errormessage.setText("Your password is incorrect");}
				}
			else {Errormessage.setText("Your username is incorrect");}	
		}
    }
}


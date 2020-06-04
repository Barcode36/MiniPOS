package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.ImageHelper;
import com.alphasoft.pos.commons.Validations;
import com.alphasoft.pos.contexts.PosException;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.services.ProductCategoryService;
import com.alphasoft.pos.views.customs.AlertBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductCategoryFormController implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private TextField categoryNameInput;

    @FXML
    private Label title;


    @FXML
    private HBox mainButtonBox;

    private ProductCategory category;

    private Button deleteButton,addButton,updateButton;

    private File imageFile;


    public void setData(ProductCategory category){
        this.category = category;
        imageView.setImage(new Image(Objects.requireNonNull(ImageHelper.blobToInputStream(category.getImageBlob()))));
        categoryNameInput.setText(category.getName());
    }

    @FXML
    void cancel() {
        close();
    }

    @FXML
    void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*.jpg","*.png","*.jpeg"));
        File file = fileChooser.showOpenDialog(MainWindowController.mainStage);
        if(null!=file){
            Image image = new Image(Objects.requireNonNull(ImageHelper.fileToInputStream(file)));
            if(image.getWidth()==image.getHeight()){
                imageFile = file;
                imageView.setImage(new Image(Objects.requireNonNull(ImageHelper.fileToInputStream(imageFile))));
            }else {
                showAlert("Invalid Image","Image must be square");
            }

        }
        if(null!=category){
            toggleUpdateButton();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupButton();
        Platform.runLater(this::runLater);
    }

    private void setupButton(){
        deleteButton = new Button("Delete");
        updateButton = new Button("Update");
        addButton = new Button("Add");

        addButton.setOnAction(e-> onAdd());
        updateButton.setOnAction(e-> onUpdate());
    }

    private void onAdd(){
        try{
            Validations.notEmptyString(categoryNameInput.getText().trim(),"Please enter category name");
            Validations.notNull(imageFile,"No image selected");
            addNewCategory();
            close();
        }catch (PosException exception){
            showAlert("Action cannot be completed",exception.getMessage());
        }
    }

    private void onUpdate(){
        try{
            Validations.notEmptyString(categoryNameInput.getText().trim(),"Please enter category name");
            if(null==imageView.getImage()){
                Validations.notNull(imageFile,"Please select an image");
            }
            updateCategory();
            close();
        }catch (PosException exception){
            showAlert("Action cannot be completed",exception.getMessage());
        }
    }

    private void addNewCategory(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(categoryNameInput.getText().trim());
        productCategory.setImageFile(imageFile);
        ProductCategoryService.getService().checkAndAddCategory(productCategory);
    }

    private void updateCategory(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(category.getId());
        productCategory.setName(categoryNameInput.getText().trim());
        productCategory.setImageFile(imageFile);
        ProductCategoryService.getService().checkAndUpdateCategory(productCategory);
    }

    private void toggleUpdateButton(){
        updateButton.setDisable((imageFile==null && category.getName().contentEquals(categoryNameInput.getText().trim())));
    }

    private void runLater(){
        if(null==category){
            title.setText("Add New Category");
            mainButtonBox.getChildren().addAll(addButton);
        }else{
            title.setText("Edit Category");
            mainButtonBox.getChildren().addAll(deleteButton,updateButton);
            categoryNameInput.textProperty().addListener((l,o,n)->toggleUpdateButton());
            toggleUpdateButton();
        }
    }

    private void close(){
        categoryNameInput.getScene().getWindow().hide();
    }

    private void showAlert(String title,String message){
        AlertBox alertBox = new AlertBox((Stage)imageView.getScene().getWindow());
        alertBox.setTitle(title);
        alertBox.setContentText(message);
        alertBox.show();
    }


}

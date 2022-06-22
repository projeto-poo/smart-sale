package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.datalayer.Connect;

import java.io.IOException;

public class Main extends Application {

    private static double xOffset = 0;
    private static double yOffset = 0;

    private static Stage stage;

    private static String currentPage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!Connect.checkDatabase()) {
            return;
        }

        Main.stage = primaryStage;

        Parent parentSignIn = FXMLLoader.load(
            getClass().getResource("/view/sign-in.fxml")
        );

        Scene sceneSignIn = new Scene(parentSignIn);
        sceneSignIn.setFill(Color.TRANSPARENT);

        stage.getIcons().add(new Image("/view/assets/media/logo/icon.png"));
        stage.setTitle("Smart Sale");
        stage.setScene(sceneSignIn);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

        FadeTransition fadeTransitionRoot = new FadeTransition(
            Duration.millis(400),
            parentSignIn
        );
        fadeTransitionRoot.setFromValue(0.5);
        fadeTransitionRoot.setToValue(1);

        ScaleTransition scaleTransitionRoot = new ScaleTransition(
            Duration.millis(250),
            parentSignIn
        );
        scaleTransitionRoot.setFromX(0.5);
        scaleTransitionRoot.setFromY(0.5);
        scaleTransitionRoot.setToX(1);
        scaleTransitionRoot.setToY(1);

        fadeTransitionRoot.play();
        scaleTransitionRoot.play();

        dragWindow(parentSignIn);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Minimiza a janela do sistema.
     */
    public static void minimizeWindow() {
        stage.setIconified(true);
    }

    /**
     * Fecha a janela e encerra o sistema.
     */
    public static void closeWindow() {
        FadeTransition fadeTransitionRoot = new FadeTransition(
            Duration.millis(250),
            stage.getScene().getRoot()
        );
        fadeTransitionRoot.setFromValue(1);
        fadeTransitionRoot.setToValue(0);

        ScaleTransition scaleTransitionRoot = new ScaleTransition(
            Duration.millis(250),
            stage.getScene().getRoot()
        );
        scaleTransitionRoot.setFromX(1);
        scaleTransitionRoot.setFromY(1);
        scaleTransitionRoot.setToX(0.5);
        scaleTransitionRoot.setToY(0.5);
        scaleTransitionRoot.setOnFinished(event -> {
            stage.hide();
        });

        fadeTransitionRoot.play();
        scaleTransitionRoot.play();
    }

    /**
     * Mostra a tela interna do sistema após efetuar o login.
     */
    public static void showInternalScreen() {
        FadeTransition fadeTransitionSignIn = new FadeTransition(
            Duration.millis(250),
            stage.getScene().getRoot()
        );
        fadeTransitionSignIn.setFromValue(1);
        fadeTransitionSignIn.setToValue(0);

        ScaleTransition scaleTransitionSignIn = new ScaleTransition(
            Duration.millis(250),
            stage.getScene().getRoot()
        );
        scaleTransitionSignIn.setFromX(1);
        scaleTransitionSignIn.setFromY(1);
        scaleTransitionSignIn.setToX(0.5);
        scaleTransitionSignIn.setToY(0.5);
        scaleTransitionSignIn.setOnFinished(event -> {
            try {
                Parent parentInternalScreen = FXMLLoader.load(
                    Main.class.getResource("/view/internal-screen.fxml")
                );

                Scene sceneInternalScreen = new Scene(parentInternalScreen);
                sceneInternalScreen.setFill(Color.TRANSPARENT);

                FadeTransition fadeTransitionInternalScreen = new FadeTransition(
                    Duration.millis(250),
                    parentInternalScreen
                );
                fadeTransitionInternalScreen.setFromValue(0);
                fadeTransitionInternalScreen.setToValue(1);

                ScaleTransition scaleTransitionInternalScreen = new ScaleTransition(
                    Duration.millis(250),
                    parentInternalScreen
                );
                scaleTransitionInternalScreen.setFromX(0.5);
                scaleTransitionInternalScreen.setFromY(0.5);
                scaleTransitionInternalScreen.setToX(1);
                scaleTransitionInternalScreen.setToY(1);

                fadeTransitionInternalScreen.play();
                scaleTransitionInternalScreen.play();

                stage.setScene(sceneInternalScreen);
                stage.centerOnScreen();

                dragWindow(parentInternalScreen);

                changeScreen("home");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fadeTransitionSignIn.play();
        scaleTransitionSignIn.play();
    }

    /**
     * Método responsável por trocar as telas na área interna do sistema.
     *
     * @param view Nome do arquivo da tela que deseja exibir sem a extensão.
     * Ex.: home.fxml > home
     */
    public static void changeScreen(String view) {
        if (view.equals(currentPage)) {
            return;
        }

        try {
            Parent parent = FXMLLoader.load(
                Main.class.getResource("/view/" + view + ".fxml")
            );

            BorderPane borderPaneOutside = (BorderPane) stage.getScene().getRoot();
            BorderPane borderPaneInside = (BorderPane) borderPaneOutside.getCenter();
            borderPaneInside.setCenter(parent);

            FadeTransition fadeTransitionParent = new FadeTransition(
                Duration.millis(400),
                parent
            );
            fadeTransitionParent.setFromValue(0);
            fadeTransitionParent.setToValue(1);
            fadeTransitionParent.play();

            Main.currentPage = view;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por aplicar o evento de arrastar as telas.
     *
     * @param parent Elemento principal da tela que está sendo exibida.
     */
    private static void dragWindow(Parent parent) {
        parent.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        parent.setOnMouseDragged(event -> {
            stage.setOpacity(0.8);
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        parent.setOnMouseReleased(event -> {
            stage.setOpacity(1);
        });
    }
}

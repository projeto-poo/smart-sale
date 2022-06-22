package helper;

import controller.Main;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import model.datalayer.DataLayer;

public class Table {

    /**
     * Aciona o redirecionamento para uma tela de formulário especificada em
     * view para editar um item selecionado na tabela.
     *
     * @param tableView Tabela do javafx.
     * @param view Nome do arquivo da tela de formulário que deseja exibir sem a
     * palavra -form e extensão.
     * Ex.: supplier-form.fxml > supplier
     */
    public static void editModelItem(TableView tableView, String view) {
        DataLayer model = (DataLayer) tableView.getSelectionModel().getSelectedItem();

        if (model == null) {
            return;
        }

        Main.getStage().setUserData(model);
        Main.changeScreen(view + "-form");
    }

    /**
     * Remove um item selecionado na tabela e sua respectiva tupla no banco de
     * dados.
     *
     * @param tableView Tabela do javafx.
     * @param observableList Uma lista rastreável com os objetos da tabela.
     * @param sortedList Objeto de classificação de observableList.
     */
    public static void removeModelItem(
        TableView tableView,
        ObservableList observableList,
        SortedList sortedList
    ) {
        DataLayer model = (DataLayer) tableView.getSelectionModel().getSelectedItem();

        if (model == null) {
            return;
        }

        if (!model.destroy()) {
            System.out.println(model.fail().getMessage());
            return;
        }

        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        int sourceIndex = sortedList.getSourceIndexFor(
            observableList,
            selectedIndex
        );

        observableList.remove(sourceIndex);
    }
}

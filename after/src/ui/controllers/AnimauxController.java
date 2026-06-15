package ui.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class AnimauxController {

    private final List<String[]> backingList;
    private final ObservableList<AnimalRow> rows = FXCollections.observableArrayList();
    private final TableView<AnimalRow> table = new TableView<>();

    public AnimauxController(List<String[]> backingList) {
        this.backingList = backingList;
        for (String[] a : backingList) rows.add(new AnimalRow(a));
        buildTable();
    }

    private void buildTable() {
        TableColumn<AnimalRow, String> cCode = new TableColumn<>("Code");
        cCode.setCellValueFactory(p -> p.getValue().code);
        TableColumn<AnimalRow, String> cEsp = new TableColumn<>("Espèce");
        cEsp.setCellValueFactory(p -> p.getValue().espece);
        TableColumn<AnimalRow, String> cAge = new TableColumn<>("Âge");
        cAge.setCellValueFactory(p -> p.getValue().age);
        TableColumn<AnimalRow, String> cPoids = new TableColumn<>("Poids (kg)");
        cPoids.setCellValueFactory(p -> p.getValue().poids);
        TableColumn<AnimalRow, String> cEtat = new TableColumn<>("État santé");
        cEtat.setCellValueFactory(p -> p.getValue().etat);

        table.getColumns().addAll(cCode, cEsp, cAge, cPoids, cEtat);
        table.setItems(rows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public Node getView() {
        VBox wrap = new VBox(8);
        wrap.setPadding(new Insets(6));

        HBox actions = new HBox(8);
        Button add = new Button("➕ Ajouter");
        Button edit = new Button("✏️ Modifier");
        Button del = new Button("🗑 Supprimer");
        actions.getChildren().addAll(add, edit, del);

        add.setOnAction(e -> doAdd());
        edit.setOnAction(e -> doEdit());
        del.setOnAction(e -> doDelete());

        VBox.setVgrow(table, Priority.ALWAYS);
        wrap.getChildren().addAll(actions, table);
        return wrap;
    }

    private void doAdd() {
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle("Ajouter un animal");
        ButtonType ok = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        TextField code = new TextField(); TextField espece = new TextField(); TextField age = new TextField(); TextField poids = new TextField(); TextField etat = new TextField();
        grid.add(new Label("Code:"), 0, 0); grid.add(code, 1, 0);
        grid.add(new Label("Espèce:"), 0, 1); grid.add(espece, 1, 1);
        grid.add(new Label("Âge:"), 0, 2); grid.add(age, 1, 2);
        grid.add(new Label("Poids:"), 0, 3); grid.add(poids, 1, 3);
        grid.add(new Label("État:"), 0, 4); grid.add(etat, 1, 4);
        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> bt == ok ? new String[]{code.getText(), espece.getText(), age.getText(), poids.getText(), etat.getText()} : null);
        dlg.showAndWait().ifPresent(arr -> {
            if (arr[0] == null || arr[0].isBlank()) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Le code de l'animal est requis.", ButtonType.OK);
                a.showAndWait();
                return;
            }
            // validate poids is numeric
            try {
                if (arr.length > 3 && arr[3] != null && !arr[3].isBlank()) Double.parseDouble(arr[3]);
            } catch (NumberFormatException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Le poids doit être un nombre.", ButtonType.OK);
                a.showAndWait();
                return;
            }
            boolean exists = backingList.stream().anyMatch(b -> b.length>0 && b[0].equals(arr[0]));
            if (exists) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Un animal avec ce code existe déjà.", ButtonType.OK);
                a.showAndWait();
                return;
            }
            backingList.add(arr);
            rows.add(new AnimalRow(arr));
        });
    }

    private void doEdit() {
        AnimalRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle("Modifier l'animal");
        ButtonType ok = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(8); grid.setVgap(8);
        // keep original code to update backing list correctly
        String originalCode = sel.code.get();
        TextField code = new TextField(originalCode); TextField espece = new TextField(sel.espece.get()); TextField age = new TextField(sel.age.get()); TextField poids = new TextField(sel.poids.get()); TextField etat = new TextField(sel.etat.get());
        grid.add(new Label("Code:"), 0, 0); grid.add(code, 1, 0);
        grid.add(new Label("Espèce:"), 0, 1); grid.add(espece, 1, 1);
        grid.add(new Label("Âge:"), 0, 2); grid.add(age, 1, 2);
        grid.add(new Label("Poids:"), 0, 3); grid.add(poids, 1, 3);
        grid.add(new Label("État:"), 0, 4); grid.add(etat, 1, 4);
        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> bt == ok ? new String[]{code.getText(), espece.getText(), age.getText(), poids.getText(), etat.getText()} : null);
        dlg.showAndWait().ifPresent(arr -> {
            sel.code.set(arr[0]); sel.espece.set(arr[1]); sel.age.set(arr[2]); sel.poids.set(arr[3]); sel.etat.set(arr[4]);
            for (int i = 0; i < backingList.size(); i++) {
                String[] b = backingList.get(i);
                if (b.length>0 && b[0].equals(originalCode)) {
                    backingList.set(i, arr);
                    break;
                }
            }
            table.refresh();
        });
    }

    private void doDelete() {
        AnimalRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer l'animal " + sel.code.get() + " ?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmer la suppression");
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                backingList.removeIf(b -> b.length>0 && b[0].equals(sel.code.get()));
                rows.remove(sel);
            }
        });
    }

    public void refresh() {
        rows.clear();
        for (String[] a : backingList) rows.add(new AnimalRow(a));
    }

    private static class AnimalRow {
        final SimpleStringProperty code = new SimpleStringProperty("");
        final SimpleStringProperty espece = new SimpleStringProperty("");
        final SimpleStringProperty age = new SimpleStringProperty("");
        final SimpleStringProperty poids = new SimpleStringProperty("");
        final SimpleStringProperty etat = new SimpleStringProperty("");

        AnimalRow(String[] arr) {
            if (arr.length > 0) code.set(arr[0]);
            if (arr.length > 1) espece.set(arr[1]);
            if (arr.length > 2) age.set(arr[2]);
            if (arr.length > 3) poids.set(arr[3]);
            if (arr.length > 4) etat.set(arr[4]);
        }
    }
}

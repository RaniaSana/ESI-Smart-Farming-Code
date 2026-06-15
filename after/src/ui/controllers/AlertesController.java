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

public class AlertesController {

    private final List<String[]> backingList;
    private final ObservableList<AlerteRow> rows = FXCollections.observableArrayList();
    private final TableView<AlerteRow> table = new TableView<>();

    public AlertesController(List<String[]> backingList) {
        this.backingList = backingList;
        for (String[] alerte : backingList) rows.add(new AlerteRow(alerte));
        buildTable();
    }

    private void buildTable() {
        TableColumn<AlerteRow, String> cCode = new TableColumn<>("Code");
        cCode.setCellValueFactory(p -> p.getValue().code);
        TableColumn<AlerteRow, String> cCapteur = new TableColumn<>("Capteur");
        cCapteur.setCellValueFactory(p -> p.getValue().capteur);
        TableColumn<AlerteRow, String> cGravite = new TableColumn<>("Gravité");
        cGravite.setCellValueFactory(p -> p.getValue().gravite);
        TableColumn<AlerteRow, String> cStatut = new TableColumn<>("Statut");
        cStatut.setCellValueFactory(p -> p.getValue().statut);
        TableColumn<AlerteRow, String> cMessage = new TableColumn<>("Message");
        cMessage.setCellValueFactory(p -> p.getValue().message);

        table.getColumns().addAll(cCode, cCapteur, cGravite, cStatut, cMessage);
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
        Dialog<String[]> dlg = buildDialog("Ajouter une alerte", "Ajouter", null);
        dlg.showAndWait().ifPresent(this::addRow);
    }

    private void doEdit() {
        AlerteRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        String originalCode = sel.code.get();
        Dialog<String[]> dlg = buildDialog("Modifier l'alerte", "Modifier", sel);
        dlg.showAndWait().ifPresent(arr -> {
            sel.code.set(arr[0]);
            sel.capteur.set(arr[1]);
            sel.gravite.set(arr[2]);
            sel.statut.set(arr[3]);
            sel.message.set(arr[4]);
            for (int i = 0; i < backingList.size(); i++) {
                String[] b = backingList.get(i);
                if (b.length > 0 && b[0].equals(originalCode)) {
                    backingList.set(i, arr);
                    break;
                }
            }
            table.refresh();
        });
    }

    private Dialog<String[]> buildDialog(String title, String action, AlerteRow row) {
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle(title);
        ButtonType ok = new ButtonType(action, ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        TextField code = new TextField(row == null ? "" : row.code.get());
        TextField capteur = new TextField(row == null ? "" : row.capteur.get());
        TextField gravite = new TextField(row == null ? "WARNING" : row.gravite.get());
        TextField statut = new TextField(row == null ? "ACTIVE" : row.statut.get());
        TextField message = new TextField(row == null ? "" : row.message.get());

        grid.add(new Label("Code:"), 0, 0); grid.add(code, 1, 0);
        grid.add(new Label("Capteur:"), 0, 1); grid.add(capteur, 1, 1);
        grid.add(new Label("Gravité:"), 0, 2); grid.add(gravite, 1, 2);
        grid.add(new Label("Statut:"), 0, 3); grid.add(statut, 1, 3);
        grid.add(new Label("Message:"), 0, 4); grid.add(message, 1, 4);
        dlg.getDialogPane().setContent(grid);

        dlg.setResultConverter(bt -> bt == ok
            ? new String[]{code.getText(), capteur.getText(), gravite.getText(), statut.getText(), message.getText()}
            : null);
        return dlg;
    }

    private void addRow(String[] arr) {
        if (arr[0] == null || arr[0].isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Le code de l'alerte est requis.", ButtonType.OK).showAndWait();
            return;
        }
        boolean exists = backingList.stream().anyMatch(b -> b.length > 0 && b[0].equals(arr[0]));
        if (exists) {
            new Alert(Alert.AlertType.ERROR, "Une alerte avec ce code existe déjà.", ButtonType.OK).showAndWait();
            return;
        }
        backingList.add(arr);
        rows.add(new AlerteRow(arr));
    }

    private void doDelete() {
        AlerteRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer l'alerte " + sel.code.get() + " ?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmer la suppression");
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                backingList.removeIf(b -> b.length > 0 && b[0].equals(sel.code.get()));
                rows.remove(sel);
            }
        });
    }

    public void refresh() {
        rows.clear();
        for (String[] alerte : backingList) rows.add(new AlerteRow(alerte));
    }

    private static class AlerteRow {
        final SimpleStringProperty code = new SimpleStringProperty("");
        final SimpleStringProperty capteur = new SimpleStringProperty("");
        final SimpleStringProperty gravite = new SimpleStringProperty("");
        final SimpleStringProperty statut = new SimpleStringProperty("");
        final SimpleStringProperty message = new SimpleStringProperty("");

        AlerteRow(String[] arr) {
            if (arr.length > 0) code.set(arr[0]);
            if (arr.length > 1) capteur.set(arr[1]);
            if (arr.length > 2) gravite.set(arr[2]);
            if (arr.length > 3) statut.set(arr[3]);
            if (arr.length > 4) message.set(arr[4]);
        }
    }
}

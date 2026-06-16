package ui.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class RelevesController {

    private final List<String[]> backingList;
    private final ObservableList<ReleveRow> rows = FXCollections.observableArrayList();
    private final TableView<ReleveRow> table = new TableView<>();
    private FilteredList<ReleveRow> filteredRows;

    public RelevesController(List<String[]> backingList) {
        this.backingList = backingList;
        for (String[] releve : backingList) rows.add(new ReleveRow(releve));
        buildTable();
    }

    private void buildTable() {
        TableColumn<ReleveRow, String> cCode = new TableColumn<>("Code");
        cCode.setCellValueFactory(p -> p.getValue().code);
        TableColumn<ReleveRow, String> cCapteur = new TableColumn<>("Capteur");
        cCapteur.setCellValueFactory(p -> p.getValue().capteur);
        TableColumn<ReleveRow, String> cValeur = new TableColumn<>("Valeur");
        cValeur.setCellValueFactory(p -> p.getValue().valeur);
        TableColumn<ReleveRow, String> cUnite = new TableColumn<>("Unité");
        cUnite.setCellValueFactory(p -> p.getValue().unite);
        TableColumn<ReleveRow, String> cNiveau = new TableColumn<>("Niveau");
        cNiveau.setCellValueFactory(p -> p.getValue().niveau);
        TableColumn<ReleveRow, String> cDate = new TableColumn<>("Date");
        cDate.setCellValueFactory(p -> p.getValue().date);

        table.getColumns().addAll(cCode, cCapteur, cValeur, cUnite, cNiveau, cDate);
        filteredRows = new FilteredList<>(rows, row -> true);
        table.setItems(filteredRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public Node getView() {
        VBox wrap = new VBox(8);
        wrap.setPadding(new Insets(6));

        HBox filters = new HBox(8);
        TextField capteurFilter = new TextField();
        capteurFilter.setPromptText("Capteur");
        ComboBox<String> niveauFilter = new ComboBox<>();
        niveauFilter.getItems().addAll("Tous", "NORMAL", "WARNING", "CRITICAL");
        niveauFilter.getSelectionModel().selectFirst();
        TextField dateFilter = new TextField();
        dateFilter.setPromptText("Date");
        Button clearFilters = new Button("Réinitialiser");
        filters.getChildren().addAll(new Label("Filtres:"), capteurFilter, niveauFilter, dateFilter, clearFilters);

        capteurFilter.textProperty().addListener((o, oldValue, newValue) -> applyFilters(capteurFilter, niveauFilter, dateFilter));
        niveauFilter.valueProperty().addListener((o, oldValue, newValue) -> applyFilters(capteurFilter, niveauFilter, dateFilter));
        dateFilter.textProperty().addListener((o, oldValue, newValue) -> applyFilters(capteurFilter, niveauFilter, dateFilter));
        clearFilters.setOnAction(e -> {
            capteurFilter.clear();
            niveauFilter.getSelectionModel().selectFirst();
            dateFilter.clear();
        });

        HBox actions = new HBox(8);
        Button add = new Button("➕ Ajouter");
        Button edit = new Button("✏️ Modifier");
        Button del = new Button("🗑 Supprimer");
        actions.getChildren().addAll(add, edit, del);

        add.setOnAction(e -> doAdd());
        edit.setOnAction(e -> doEdit());
        del.setOnAction(e -> doDelete());

        VBox.setVgrow(table, Priority.ALWAYS);
        wrap.getChildren().addAll(filters, actions, table);
        return wrap;
    }

    private void applyFilters(TextField capteurFilter, ComboBox<String> niveauFilter, TextField dateFilter) {
        String capteur = capteurFilter.getText() == null ? "" : capteurFilter.getText().trim().toLowerCase();
        String niveau = niveauFilter.getValue();
        String date = dateFilter.getText() == null ? "" : dateFilter.getText().trim().toLowerCase();
        filteredRows.setPredicate(row ->
            row.capteur.get().toLowerCase().contains(capteur)
                && ("Tous".equals(niveau) || row.niveau.get().equals(niveau))
                && row.date.get().toLowerCase().contains(date)
        );
    }

    private void doAdd() {
        Dialog<String[]> dlg = buildDialog("Ajouter un relevé", "Ajouter", null);
        dlg.showAndWait().ifPresent(this::addRow);
    }

    private void doEdit() {
        ReleveRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        String originalCode = sel.code.get();
        Dialog<String[]> dlg = buildDialog("Modifier le relevé", "Modifier", sel);
        dlg.showAndWait().ifPresent(arr -> {
            if (!isValidRow(arr)) return;

            sel.code.set(arr[0]);
            sel.capteur.set(arr[1]);
            sel.valeur.set(arr[2]);
            sel.unite.set(arr[3]);
            sel.niveau.set(arr[4]);
            sel.date.set(arr[5]);
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

    private Dialog<String[]> buildDialog(String title, String action, ReleveRow row) {
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle(title);
        ButtonType ok = new ButtonType(action, ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        TextField code = new TextField(row == null ? "" : row.code.get());
        TextField capteur = new TextField(row == null ? "" : row.capteur.get());
        TextField valeur = new TextField(row == null ? "" : row.valeur.get());
        TextField unite = new TextField(row == null ? "" : row.unite.get());
        TextField niveau = new TextField(row == null ? "NORMAL" : row.niveau.get());
        TextField date = new TextField(row == null ? "" : row.date.get());

        grid.add(new Label("Code:"), 0, 0); grid.add(code, 1, 0);
        grid.add(new Label("Capteur:"), 0, 1); grid.add(capteur, 1, 1);
        grid.add(new Label("Valeur:"), 0, 2); grid.add(valeur, 1, 2);
        grid.add(new Label("Unité:"), 0, 3); grid.add(unite, 1, 3);
        grid.add(new Label("Niveau:"), 0, 4); grid.add(niveau, 1, 4);
        grid.add(new Label("Date:"), 0, 5); grid.add(date, 1, 5);
        dlg.getDialogPane().setContent(grid);

        dlg.setResultConverter(bt -> bt == ok
            ? new String[]{code.getText(), capteur.getText(), valeur.getText(), unite.getText(), niveau.getText(), date.getText()}
            : null);
        return dlg;
    }

    private void addRow(String[] arr) {
        if (!isValidRow(arr)) return;

        boolean exists = backingList.stream().anyMatch(b -> b.length > 0 && b[0].equals(arr[0]));
        if (exists) {
            new Alert(Alert.AlertType.ERROR, "Un relevé avec ce code existe déjà.", ButtonType.OK).showAndWait();
            return;
        }
        backingList.add(arr);
        rows.add(new ReleveRow(arr));
    }

    private boolean isValidRow(String[] arr) {
        if (arr[0] == null || arr[0].isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Le code du relevé est requis.", ButtonType.OK).showAndWait();
            return false;
        }
        try {
            if (arr[2] != null && !arr[2].isBlank()) Double.parseDouble(arr[2]);
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "La valeur du relevé doit être un nombre.", ButtonType.OK).showAndWait();
            return false;
        }
        return true;
    }

    private void doDelete() {
        ReleveRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer le relevé " + sel.code.get() + " ?", ButtonType.YES, ButtonType.NO);
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
        for (String[] releve : backingList) rows.add(new ReleveRow(releve));
    }

    private static class ReleveRow {
        final SimpleStringProperty code = new SimpleStringProperty("");
        final SimpleStringProperty capteur = new SimpleStringProperty("");
        final SimpleStringProperty valeur = new SimpleStringProperty("");
        final SimpleStringProperty unite = new SimpleStringProperty("");
        final SimpleStringProperty niveau = new SimpleStringProperty("");
        final SimpleStringProperty date = new SimpleStringProperty("");

        ReleveRow(String[] arr) {
            if (arr.length > 0) code.set(arr[0]);
            if (arr.length > 1) capteur.set(arr[1]);
            if (arr.length > 2) valeur.set(arr[2]);
            if (arr.length > 3) unite.set(arr[3]);
            if (arr.length > 4) niveau.set(arr[4]);
            if (arr.length > 5) date.set(arr[5]);
        }
    }
}

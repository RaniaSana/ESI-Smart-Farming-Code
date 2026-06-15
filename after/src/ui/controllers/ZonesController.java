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

public class ZonesController {

    private final List<String[]> backingList;
    private final ObservableList<ZoneRow> rows = FXCollections.observableArrayList();
    private final TableView<ZoneRow> table = new TableView<>();
    private FilteredList<ZoneRow> filteredRows;

    public ZonesController(List<String[]> backingList) {
        this.backingList = backingList;
        for (String[] a : backingList) rows.add(new ZoneRow(a));
        buildTable();
    }

    private void buildTable() {
        TableColumn<ZoneRow, String> cCode = new TableColumn<>("Code");
        cCode.setCellValueFactory(p -> p.getValue().code);
        TableColumn<ZoneRow, String> cName = new TableColumn<>("Nom");
        cName.setCellValueFactory(p -> p.getValue().name);
        TableColumn<ZoneRow, String> cType = new TableColumn<>("Type");
        cType.setCellValueFactory(p -> p.getValue().type);
        TableColumn<ZoneRow, String> cStat = new TableColumn<>("Statut");
        cStat.setCellValueFactory(p -> p.getValue().status);

        table.getColumns().addAll(cCode, cName, cType, cStat);
        filteredRows = new FilteredList<>(rows, row -> true);
        table.setItems(filteredRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public Node getView() {
        VBox wrap = new VBox(8);
        wrap.setPadding(new Insets(6));

        HBox filters = new HBox(8);
        TextField nameFilter = new TextField();
        nameFilter.setPromptText("Nom/code");
        TextField typeFilter = new TextField();
        typeFilter.setPromptText("Type");
        ComboBox<String> statutFilter = new ComboBox<>();
        statutFilter.getItems().addAll("Tous", "ACTIVE", "SUSPENDUE", "INACTIVE");
        statutFilter.getSelectionModel().selectFirst();
        Button clearFilters = new Button("Réinitialiser");
        filters.getChildren().addAll(new Label("Filtres:"), nameFilter, typeFilter, statutFilter, clearFilters);

        nameFilter.textProperty().addListener((o, oldValue, newValue) -> applyFilters(nameFilter, typeFilter, statutFilter));
        typeFilter.textProperty().addListener((o, oldValue, newValue) -> applyFilters(nameFilter, typeFilter, statutFilter));
        statutFilter.valueProperty().addListener((o, oldValue, newValue) -> applyFilters(nameFilter, typeFilter, statutFilter));
        clearFilters.setOnAction(e -> {
            nameFilter.clear();
            typeFilter.clear();
            statutFilter.getSelectionModel().selectFirst();
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

    private void applyFilters(TextField nameFilter, TextField typeFilter, ComboBox<String> statutFilter) {
        String nameOrCode = nameFilter.getText() == null ? "" : nameFilter.getText().trim().toLowerCase();
        String type = typeFilter.getText() == null ? "" : typeFilter.getText().trim().toLowerCase();
        String statut = statutFilter.getValue();
        filteredRows.setPredicate(row ->
            (row.code.get().toLowerCase().contains(nameOrCode) || row.name.get().toLowerCase().contains(nameOrCode))
                && row.type.get().toLowerCase().contains(type)
                && ("Tous".equals(statut) || row.status.get().equals(statut))
        );
    }

    private void doAdd() {
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle("Ajouter une zone");
        ButtonType ok = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        TextField code = new TextField(); TextField name = new TextField(); TextField type = new TextField(); TextField stat = new TextField();
        grid.add(new Label("Code:"), 0, 0); grid.add(code, 1, 0);
        grid.add(new Label("Nom:"), 0, 1); grid.add(name, 1, 1);
        grid.add(new Label("Type:"), 0, 2); grid.add(type, 1, 2);
        grid.add(new Label("Statut:"), 0, 3); grid.add(stat, 1, 3);
        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> bt == ok ? new String[]{code.getText(), name.getText(), type.getText(), stat.getText()} : null);
        dlg.showAndWait().ifPresent(arr -> {
            if (arr[0] == null || arr[0].isBlank()) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Le code de la zone est requis.", ButtonType.OK);
                a.showAndWait();
                return;
            }
            // avoid duplicate codes
            boolean exists = backingList.stream().anyMatch(b -> b.length>0 && b[0].equals(arr[0]));
            if (exists) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Une zone avec ce code existe déjà.", ButtonType.OK);
                a.showAndWait();
                return;
            }
            backingList.add(arr);
            rows.add(new ZoneRow(arr));
        });
    }

    private void doEdit() {
        ZoneRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle("Modifier la zone");
        ButtonType ok = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(8); grid.setVgap(8);
        // keep original code to update backing list correctly
        String originalCode = sel.code.get();
        TextField code = new TextField(originalCode); TextField name = new TextField(sel.name.get()); TextField type = new TextField(sel.type.get()); TextField stat = new TextField(sel.status.get());
        grid.add(new Label("Code:"), 0, 0); grid.add(code, 1, 0);
        grid.add(new Label("Nom:"), 0, 1); grid.add(name, 1, 1);
        grid.add(new Label("Type:"), 0, 2); grid.add(type, 1, 2);
        grid.add(new Label("Statut:"), 0, 3); grid.add(stat, 1, 3);
        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> bt == ok ? new String[]{code.getText(), name.getText(), type.getText(), stat.getText()} : null);
        dlg.showAndWait().ifPresent(arr -> {
            sel.code.set(arr[0]); sel.name.set(arr[1]); sel.type.set(arr[2]); sel.status.set(arr[3]);
            // update backing list by matching original code
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
        ZoneRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer la zone " + sel.code.get() + " ?", ButtonType.YES, ButtonType.NO);
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
        for (String[] a : backingList) rows.add(new ZoneRow(a));
    }

    private static class ZoneRow {
        final SimpleStringProperty code = new SimpleStringProperty("");
        final SimpleStringProperty name = new SimpleStringProperty("");
        final SimpleStringProperty type = new SimpleStringProperty("");
        final SimpleStringProperty status = new SimpleStringProperty("");

        ZoneRow(String[] arr) {
            if (arr.length > 0) code.set(arr[0]);
            if (arr.length > 1) name.set(arr[1]);
            if (arr.length > 2) type.set(arr[2]);
            if (arr.length > 3) status.set(arr[3]);
        }
    }
}

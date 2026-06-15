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

public class CapteursController {

    private final List<String[]> backingList;
    private final ObservableList<CapteurRow> rows = FXCollections.observableArrayList();
    private final TableView<CapteurRow> table = new TableView<>();
    private FilteredList<CapteurRow> filteredRows;

    public CapteursController(List<String[]> backingList) {
        this.backingList = backingList;
        for (String[] capteur : backingList) rows.add(new CapteurRow(capteur));
        buildTable();
    }

    private void buildTable() {
        TableColumn<CapteurRow, String> cCode = new TableColumn<>("Code");
        cCode.setCellValueFactory(p -> p.getValue().code);
        TableColumn<CapteurRow, String> cZone = new TableColumn<>("Zone");
        cZone.setCellValueFactory(p -> p.getValue().zone);
        TableColumn<CapteurRow, String> cType = new TableColumn<>("Type");
        cType.setCellValueFactory(p -> p.getValue().type);
        TableColumn<CapteurRow, String> cMesure = new TableColumn<>("Mesure");
        cMesure.setCellValueFactory(p -> p.getValue().mesure);
        TableColumn<CapteurRow, String> cSeuils = new TableColumn<>("Seuils");
        cSeuils.setCellValueFactory(p -> p.getValue().seuils);
        TableColumn<CapteurRow, String> cStatut = new TableColumn<>("Statut");
        cStatut.setCellValueFactory(p -> p.getValue().statut);

        table.getColumns().addAll(cCode, cZone, cType, cMesure, cSeuils, cStatut);
        filteredRows = new FilteredList<>(rows, row -> true);
        table.setItems(filteredRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public Node getView() {
        VBox wrap = new VBox(8);
        wrap.setPadding(new Insets(6));

        HBox filters = new HBox(8);
        TextField zoneFilter = new TextField();
        zoneFilter.setPromptText("Zone");
        TextField typeFilter = new TextField();
        typeFilter.setPromptText("Type");
        ComboBox<String> statutFilter = new ComboBox<>();
        statutFilter.getItems().addAll("Tous", "ACTIVE", "FAULTY", "SUSPENDED");
        statutFilter.getSelectionModel().selectFirst();
        Button clearFilters = new Button("Réinitialiser");
        filters.getChildren().addAll(new Label("Filtres:"), zoneFilter, typeFilter, statutFilter, clearFilters);

        zoneFilter.textProperty().addListener((o, oldValue, newValue) -> applyFilters(zoneFilter, typeFilter, statutFilter));
        typeFilter.textProperty().addListener((o, oldValue, newValue) -> applyFilters(zoneFilter, typeFilter, statutFilter));
        statutFilter.valueProperty().addListener((o, oldValue, newValue) -> applyFilters(zoneFilter, typeFilter, statutFilter));
        clearFilters.setOnAction(e -> {
            zoneFilter.clear();
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

    private void applyFilters(TextField zoneFilter, TextField typeFilter, ComboBox<String> statutFilter) {
        String zone = zoneFilter.getText() == null ? "" : zoneFilter.getText().trim().toLowerCase();
        String type = typeFilter.getText() == null ? "" : typeFilter.getText().trim().toLowerCase();
        String statut = statutFilter.getValue();
        filteredRows.setPredicate(row ->
            row.zone.get().toLowerCase().contains(zone)
                && row.type.get().toLowerCase().contains(type)
                && ("Tous".equals(statut) || row.statut.get().equals(statut))
        );
    }

    private void doAdd() {
        Dialog<String[]> dlg = buildDialog("Ajouter un capteur", "Ajouter", null);
        dlg.showAndWait().ifPresent(this::addRow);
    }

    private void doEdit() {
        CapteurRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        String originalCode = sel.code.get();
        Dialog<String[]> dlg = buildDialog("Modifier le capteur", "Modifier", sel);
        dlg.showAndWait().ifPresent(arr -> {
            sel.code.set(arr[0]);
            sel.zone.set(arr[1]);
            sel.type.set(arr[2]);
            sel.mesure.set(arr[3]);
            sel.seuils.set(arr[4]);
            sel.statut.set(arr[5]);
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

    private Dialog<String[]> buildDialog(String title, String action, CapteurRow row) {
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle(title);
        ButtonType ok = new ButtonType(action, ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        TextField code = new TextField(row == null ? "" : row.code.get());
        TextField zone = new TextField(row == null ? "" : row.zone.get());
        TextField type = new TextField(row == null ? "" : row.type.get());
        TextField mesure = new TextField(row == null ? "" : row.mesure.get());
        TextField seuils = new TextField(row == null ? "" : row.seuils.get());
        TextField statut = new TextField(row == null ? "ACTIVE" : row.statut.get());

        grid.add(new Label("Code:"), 0, 0); grid.add(code, 1, 0);
        grid.add(new Label("Zone:"), 0, 1); grid.add(zone, 1, 1);
        grid.add(new Label("Type:"), 0, 2); grid.add(type, 1, 2);
        grid.add(new Label("Mesure:"), 0, 3); grid.add(mesure, 1, 3);
        grid.add(new Label("Seuils:"), 0, 4); grid.add(seuils, 1, 4);
        grid.add(new Label("Statut:"), 0, 5); grid.add(statut, 1, 5);
        dlg.getDialogPane().setContent(grid);

        dlg.setResultConverter(bt -> bt == ok
            ? new String[]{code.getText(), zone.getText(), type.getText(), mesure.getText(), seuils.getText(), statut.getText()}
            : null);
        return dlg;
    }

    private void addRow(String[] arr) {
        if (arr[0] == null || arr[0].isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Le code du capteur est requis.", ButtonType.OK).showAndWait();
            return;
        }
        boolean exists = backingList.stream().anyMatch(b -> b.length > 0 && b[0].equals(arr[0]));
        if (exists) {
            new Alert(Alert.AlertType.ERROR, "Un capteur avec ce code existe déjà.", ButtonType.OK).showAndWait();
            return;
        }
        backingList.add(arr);
        rows.add(new CapteurRow(arr));
    }

    private void doDelete() {
        CapteurRow sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer le capteur " + sel.code.get() + " ?", ButtonType.YES, ButtonType.NO);
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
        for (String[] capteur : backingList) rows.add(new CapteurRow(capteur));
    }

    private static class CapteurRow {
        final SimpleStringProperty code = new SimpleStringProperty("");
        final SimpleStringProperty zone = new SimpleStringProperty("");
        final SimpleStringProperty type = new SimpleStringProperty("");
        final SimpleStringProperty mesure = new SimpleStringProperty("");
        final SimpleStringProperty seuils = new SimpleStringProperty("");
        final SimpleStringProperty statut = new SimpleStringProperty("");

        CapteurRow(String[] arr) {
            if (arr.length > 0) code.set(arr[0]);
            if (arr.length > 1) zone.set(arr[1]);
            if (arr.length > 2) type.set(arr[2]);
            if (arr.length > 3) mesure.set(arr[3]);
            if (arr.length > 4) seuils.set(arr[4]);
            if (arr.length > 5) statut.set(arr[5]);
        }
    }
}

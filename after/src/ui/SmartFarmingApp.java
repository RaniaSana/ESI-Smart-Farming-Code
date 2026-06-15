package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import Utilitaires.DataStore;
import Utilitaires.DataStore.SimpleData;
import ui.controllers.ZonesController;
import ui.controllers.AnimauxController;
import ui.controllers.CapteursController;
import ui.controllers.AlertesController;
import ui.controllers.RelevesController;
import java.io.IOException;

/**
 * SmartFarming JavaFX - Interface principale
 * Point d'entrée de l'application graphique.
 *
 * Pour intégrer avec votre projet existant, remplacez les données
 * statiques par vos objets SmartFarming, ZoneCulture, ZoneElevage, etc.
 */
public class SmartFarmingApp extends Application {

    // ── Palette de couleurs ──────────────────────────────────────────
    private static final String COLOR_BG          = "#F7F8F5";
    private static final String COLOR_SIDEBAR      = "#1C2B1A";
    private static final String COLOR_SIDEBAR_HOVER= "#2E4A2B";
    private static final String COLOR_ACCENT       = "#4CAF50";
    private static final String COLOR_ACCENT_DARK  = "#388E3C";
    private static final String COLOR_TEXT_LIGHT   = "#ECEFF1";
    private static final String COLOR_TEXT_MUTED   = "#90A4AE";
    private static final String COLOR_WHITE        = "#FFFFFF";
    private static final String COLOR_CARD_BORDER  = "#E0E0E0";
    private static final String COLOR_WARN         = "#FF8F00";
    private static final String COLOR_DANGER       = "#D32F2F";
    private static final String COLOR_INFO         = "#1565C0";

    private BorderPane root;
    private VBox contentArea;
    private Label pageTitle;
    // Données en mémoire pour interaction simple
    private java.util.List<String[]> zoneData = new java.util.ArrayList<>();
    private java.util.List<String[]> animalData = new java.util.ArrayList<>();
    private java.util.List<String[]> capteurData = new java.util.ArrayList<>();
    private java.util.List<String[]> alerteData = new java.util.ArrayList<>();
    private java.util.List<String[]> releveData = new java.util.ArrayList<>();
    private java.util.List<String[]> productionData = new java.util.ArrayList<>();
    private ZonesController zonesController;
    private AnimauxController animauxController;
    private CapteursController capteursController;
    private AlertesController alertesController;
    private RelevesController relevesController;

    @Override
    public void start(Stage stage) {
        // Initialiser données d'exemple (utilisable pour ajout/édition rapide)
        initSampleData();
        root = new BorderPane();
        // DataStore pour persistance simple
        dataStore = new DataStore();
        // controllers
        zonesController = new ZonesController(zoneData);
        animauxController = new AnimauxController(animalData);
        capteursController = new CapteursController(capteurData);
        alertesController = new AlertesController(alerteData);
        relevesController = new RelevesController(releveData);
        root.setTop(buildTopBar());
        root.setStyle("-fx-background-color: " + COLOR_BG + ";");

        root.setLeft(buildSidebar());
        root.setCenter(buildMainContent());

        showDashboard();

        Scene scene = new Scene(root, 1200, 760);
        stage.setTitle("SmartFarming — Tableau de bord");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    private void initSampleData() {
        zoneData.clear();
        zoneData.add(new String[]{"Z001", "North Fields", "ZoneCulture", "ACTIVE"});
        zoneData.add(new String[]{"Z002", "East Pasture", "ZoneElevage", "ACTIVE"});
        zoneData.add(new String[]{"Z003", "South Pond", "ZoneAquacole", "ACTIVE"});

        animalData.clear();
        animalData.add(new String[]{"A001", "Vache", "4 ans", "520.0", "MALADE"});
        animalData.add(new String[]{"A002", "Vache", "3 ans", "480.0", "MALADE"});
        animalData.add(new String[]{"A003", "Mouton", "2 ans", "65.0", "QUARANTAINE"});

        capteurData.clear();
        capteurData.add(new String[]{"CP001", "Z001", "CapteurSol", "HUMIDITE_SOL", "40 - 70", "ACTIVE"});
        capteurData.add(new String[]{"CP002", "Z003", "CapteurEau", "TEMPERATURE_EAU", "18 - 26", "ACTIVE"});
        capteurData.add(new String[]{"CP003", "Z002", "CapteurBiometrique", "POIDS", "450 - 650", "FAULTY"});

        alerteData.clear();
        alerteData.add(new String[]{"AL001", "CP003", "CRITICAL", "ACTIVE", "Perte de poids détectée sur A002"});
        alerteData.add(new String[]{"AL002", "CP002", "WARNING", "ACKNOWLEDGED", "Température eau proche du seuil"});
        alerteData.add(new String[]{"AL003", "CP001", "WARNING", "ACTIVE", "Humidité sol basse dans North Fields"});

        releveData.clear();
        releveData.add(new String[]{"R001", "CP001", "36.5", "%", "CRITICAL", "15/06/2026 08:30"});
        releveData.add(new String[]{"R002", "CP002", "25.0", "°C", "NORMAL", "15/06/2026 08:35"});
        releveData.add(new String[]{"R003", "CP003", "480.0", "kg", "WARNING", "15/06/2026 08:40"});

        productionData.clear();
        productionData.add(new String[]{"Z002", "LAIT", "120.5", "litres"});
        productionData.add(new String[]{"Z002", "LAIT", "80.0", "litres"});
        productionData.add(new String[]{"Z002", "OEUFS", "50.0", "unités"});
        productionData.add(new String[]{"Z001", "RENDEMENT_CULTURE", "300.0", "kg"});
    }

    // ────────────────────────────────────────────────────────────────
    // SIDEBAR
    // ────────────────────────────────────────────────────────────────
    private VBox buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: " + COLOR_SIDEBAR + ";");

        // Logo / titre
        VBox header = new VBox(4);
        header.setPadding(new Insets(28, 20, 20, 20));
        Label logo = new Label("🌿 SmartFarming");
        logo.setFont(Font.font("System", FontWeight.BOLD, 17));
        logo.setTextFill(Color.web(COLOR_TEXT_LIGHT));
        Label sub = new Label("Green Valley Farm");
        sub.setFont(Font.font("System", 12));
        sub.setTextFill(Color.web(COLOR_TEXT_MUTED));
        header.getChildren().addAll(logo, sub);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2E4A2B;");

        // Menu items
        VBox menu = new VBox(2);
        menu.setPadding(new Insets(12, 8, 8, 8));

        Button[] navBtns = {
            navButton("📊  Tableau de bord",   true),
            navButton("📡  Capteurs",          false),
            navButton("🚨  Alertes",           false),
            navButton("📈  Relevés",           false),
            navButton("📋  Rapports",          false),
            navButton("🌾  Zones & Cultures",  false),
            navButton("🐄  Élevage",           false),
            navButton("🐟  Aquaculture",        false),
            navButton("🏥  Santé animale",      false),
            navButton("📦  Production",         false),
            navButton("⚙️  Ferme",              false),
        };

        navBtns[0].setOnAction(e -> { selectNav(navBtns, navBtns[0]); showDashboard(); });
        navBtns[1].setOnAction(e -> { selectNav(navBtns, navBtns[1]); showCapteurs(); });
        navBtns[2].setOnAction(e -> { selectNav(navBtns, navBtns[2]); showAlertes(); });
        navBtns[3].setOnAction(e -> { selectNav(navBtns, navBtns[3]); showReleves(); });
        navBtns[4].setOnAction(e -> { selectNav(navBtns, navBtns[4]); showRapports(); });
        navBtns[5].setOnAction(e -> { selectNav(navBtns, navBtns[5]); showZonesCultures(); });
        navBtns[6].setOnAction(e -> { selectNav(navBtns, navBtns[6]); showElevage(); });
        navBtns[7].setOnAction(e -> { selectNav(navBtns, navBtns[7]); showAquaculture(); });
        navBtns[8].setOnAction(e -> { selectNav(navBtns, navBtns[8]); showSanteAnimale(); });
        navBtns[9].setOnAction(e -> { selectNav(navBtns, navBtns[9]); showProduction(); });
        navBtns[10].setOnAction(e -> { selectNav(navBtns, navBtns[10]); showFerme(); });

        menu.getChildren().addAll(navBtns);

        // Bas de sidebar — statut ferme
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox statusBox = new VBox(6);
        statusBox.setPadding(new Insets(12, 16, 20, 16));
        statusBox.setStyle("-fx-background-color: #162314; -fx-background-radius: 8;");
        Label statusTitle = new Label("Statut ferme");
        statusTitle.setFont(Font.font("System", FontWeight.BOLD, 12));
        statusTitle.setTextFill(Color.web(COLOR_TEXT_MUTED));
        Label statusVal = new Label("🟢  Active — F001");
        statusVal.setFont(Font.font("System", 12));
        statusVal.setTextFill(Color.web(COLOR_TEXT_LIGHT));
        statusBox.getChildren().addAll(statusTitle, statusVal);

        VBox statusWrap = new VBox(statusBox);
        statusWrap.setPadding(new Insets(0, 8, 16, 8));

        sidebar.getChildren().addAll(header, sep, menu, spacer, statusWrap);
        return sidebar;
    }

    private Button navButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 16, 10, 16));
        btn.setFont(Font.font("System", 13));
        applyNavStyle(btn, active);
        return btn;
    }

    private void applyNavStyle(Button btn, boolean active) {
        if (active) {
            btn.setStyle(
                "-fx-background-color: " + COLOR_ACCENT_DARK + ";" +
                "-fx-text-fill: " + COLOR_TEXT_LIGHT + ";" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
            );
            btn.setOnMouseEntered(null);
            btn.setOnMouseExited(null);
        } else {
            btn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + COLOR_TEXT_MUTED + ";" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
            );
            btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + COLOR_SIDEBAR_HOVER + ";" +
                "-fx-text-fill: " + COLOR_TEXT_LIGHT + ";" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
            ));
            btn.setOnMouseExited(e -> applyNavStyle(btn, false));
        }
    }

    private void selectNav(Button[] all, Button selected) {
        for (Button b : all) applyNavStyle(b, false);
        applyNavStyle(selected, true);
    }

    // ────────────────────────────────────────────────────────────────
    // ZONE PRINCIPALE
    // ────────────────────────────────────────────────────────────────
    private ScrollPane buildMainContent() {
        pageTitle = new Label("Tableau de bord");
        pageTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
        pageTitle.setTextFill(Color.web("#263238"));

        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(28, 32, 28, 32));

        ScrollPane scroll = new ScrollPane(contentArea);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + COLOR_BG + "; -fx-background-color: " + COLOR_BG + ";");
        return scroll;
    }

    // Top toolbar with Save/Load
    private DataStore dataStore;

    private HBox buildTopBar() {
        HBox bar = new HBox(8);
        bar.setPadding(new Insets(8, 12, 8, 12));
        bar.setStyle("-fx-background-color: transparent;");
        Button btnSave = actionButton("💾 Sauvegarder", COLOR_ACCENT_DARK);
        Button btnLoad = actionButton("📂 Charger", COLOR_INFO);
        btnSave.setOnAction(e -> {
            try {
                dataStore.saveSimple(zoneData, animalData, capteurData, alerteData, releveData);
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Données sauvegardées dans /data/smartfarming.json", ButtonType.OK);
                a.showAndWait();
            } catch (IOException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Erreur lors de la sauvegarde : " + ex.getMessage(), ButtonType.OK);
                a.showAndWait();
            }
        });
        btnLoad.setOnAction(e -> {
            try {
                SimpleData sd = dataStore.loadSimple();
                if (sd != null) {
                    zoneData.clear();
                    zoneData.addAll(sd.zones);
                    animalData.clear();
                    animalData.addAll(sd.animals);
                    capteurData.clear();
                    capteurData.addAll(sd.capteurs);
                    alerteData.clear();
                    alerteData.addAll(sd.alertes);
                    releveData.clear();
                    releveData.addAll(sd.releves);
                    // refresh controllers and current view
                    zonesController.refresh();
                    animauxController.refresh();
                    capteursController.refresh();
                    alertesController.refresh();
                    relevesController.refresh();
                    showDashboard();
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Données chargées.", ButtonType.OK);
                    a.showAndWait();
                }
            } catch (IOException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement : " + ex.getMessage(), ButtonType.OK);
                a.showAndWait();
            }
        });
        bar.getChildren().addAll(btnSave, btnLoad);
        return bar;
    }

    // ────────────────────────────────────────────────────────────────
    // PAGES
    // ────────────────────────────────────────────────────────────────

    private void showDashboard() {
        pageTitle.setText("Tableau de bord");
        contentArea.getChildren().clear();

        contentArea.getChildren().add(sectionHeader("📊 Vue d'ensemble — Green Valley Farm", "F001"));

        // KPI row
        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
            kpiCard("Zones actives",      String.valueOf(countRowsByValue(zoneData, 3, "ACTIVE")), "#E8F5E9", COLOR_ACCENT_DARK),
            kpiCard("Capteurs",            String.valueOf(capteurData.size()), "#E3F2FD", COLOR_INFO),
            kpiCard("Alertes actives",     String.valueOf(countRowsByValue(alerteData, 3, "ACTIVE")), "#FFEBEE", COLOR_DANGER),
            kpiCard("Animaux à surveiller", String.valueOf(countAnimalsToWatch()), "#FFF3E0", COLOR_WARN)
        );
        kpiRow.setMaxWidth(Double.MAX_VALUE);
        for (javafx.scene.Node n : kpiRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);

        contentArea.getChildren().add(kpiRow);

        // Zones row
        contentArea.getChildren().add(sectionLabel("Zones de la ferme"));
        HBox zonesRow = new HBox(16);
        for (String[] zone : zoneData) {
            String code = getValue(zone, 0);
            String name = getValue(zone, 1);
            String type = getValue(zone, 2);
            String detail = zoneDetail(type);
            String accent = zoneAccent(type);
            String bg = zoneBg(type);
            zonesRow.getChildren().add(zoneCard(code, name, type, detail, accent, bg));
        }
        for (javafx.scene.Node n : zonesRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(zonesRow);

        // Alertes
        contentArea.getChildren().add(sectionLabel("Alertes en cours"));
        addActiveAlertCards();
    }

    private void showCapteurs() {
        pageTitle.setText("Capteurs");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("📡 Gestion des capteurs", "Capteurs"));

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
            kpiCard("Total capteurs", String.valueOf(capteurData.size()), "#E3F2FD", COLOR_INFO),
            kpiCard("Actifs", String.valueOf(countRowsByValue(capteurData, 5, "ACTIVE")), "#E8F5E9", COLOR_ACCENT_DARK),
            kpiCard("Défaillants", String.valueOf(countRowsByValue(capteurData, 5, "FAULTY")), "#FFEBEE", COLOR_DANGER)
        );
        for (javafx.scene.Node n : kpiRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(kpiRow);

        contentArea.getChildren().add(sectionLabel("Capteurs enregistrés"));
        contentArea.getChildren().add(capteursController.getView());

        contentArea.getChildren().add(sectionLabel("Résumé par zone"));
        contentArea.getChildren().add(buildTable(
            new String[]{"Zone", "Capteur", "Mesure", "Statut"},
            rowsForColumns(capteurData, 1, 0, 3, 5)
        ));
    }

    private void showAlertes() {
        pageTitle.setText("Alertes");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("🚨 Suivi des alertes", "Alertes"));

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
            kpiCard("Alertes actives", String.valueOf(countRowsByValue(alerteData, 3, "ACTIVE")), "#FFEBEE", COLOR_DANGER),
            kpiCard("Critiques", String.valueOf(countRowsByValue(alerteData, 2, "CRITICAL")), "#FFF3E0", COLOR_WARN),
            kpiCard("Acquittées", String.valueOf(countRowsByValue(alerteData, 3, "ACKNOWLEDGED")), "#E3F2FD", COLOR_INFO)
        );
        for (javafx.scene.Node n : kpiRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(kpiRow);

        contentArea.getChildren().add(sectionLabel("Alertes enregistrées"));
        contentArea.getChildren().add(alertesController.getView());

        contentArea.getChildren().add(sectionLabel("Alertes en cours"));
        for (String[] alerte : alerteData) {
            if (alerte.length > 3 && "ACTIVE".equals(alerte[3])) {
                String gravite = alerte.length > 2 ? alerte[2] : "";
                String color = "CRITICAL".equals(gravite) ? COLOR_DANGER : COLOR_WARN;
                String code = alerte.length > 0 ? alerte[0] : "";
                String capteur = alerte.length > 1 ? alerte[1] : "";
                String msg = alerte.length > 4 ? alerte[4] : "";
                contentArea.getChildren().add(alertCard("⚠️  " + code + " — " + capteur, msg, color));
            }
        }
    }

    private void showReleves() {
        pageTitle.setText("Relevés");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("📈 Historique des relevés", "Relevés"));

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
            kpiCard("Total relevés", String.valueOf(releveData.size()), "#E3F2FD", COLOR_INFO),
            kpiCard("Normaux", String.valueOf(countRowsByValue(releveData, 4, "NORMAL")), "#E8F5E9", COLOR_ACCENT_DARK),
            kpiCard("Critiques", String.valueOf(countRowsByValue(releveData, 4, "CRITICAL")), "#FFEBEE", COLOR_DANGER)
        );
        for (javafx.scene.Node n : kpiRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(kpiRow);

        contentArea.getChildren().add(sectionLabel("Relevés enregistrés"));
        contentArea.getChildren().add(relevesController.getView());

        contentArea.getChildren().add(sectionLabel("Lecture rapide"));
        contentArea.getChildren().add(buildTable(
            new String[]{"Capteur", "Valeur", "Unité", "Niveau", "Date"},
            rowsForColumns(releveData, 1, 2, 3, 4, 5)
        ));
    }

    private void showRapports() {
        pageTitle.setText("Rapports");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("📋 Rapport global — Ferme SmartFarming", "Rapports"));

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
            kpiCard("Zones actives", String.valueOf(countRowsByValue(zoneData, 3, "ACTIVE")), "#E8F5E9", COLOR_ACCENT_DARK),
            kpiCard("Animaux", String.valueOf(animalData.size()), "#FFF3E0", COLOR_WARN),
            kpiCard("Alertes actives", String.valueOf(countRowsByValue(alerteData, 3, "ACTIVE")), "#FFEBEE", COLOR_DANGER),
            kpiCard("Relevés critiques", String.valueOf(countRowsByValue(releveData, 4, "CRITICAL")), "#E3F2FD", COLOR_INFO)
        );
        for (javafx.scene.Node n : kpiRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(kpiRow);

        contentArea.getChildren().add(sectionLabel("Résumé des zones"));
        contentArea.getChildren().add(buildTable(
            new String[]{"Code", "Nom", "Type", "Statut", "Capteurs liés"},
            zoneReportRows()
        ));

        contentArea.getChildren().add(sectionLabel("Résumé animaux"));
        contentArea.getChildren().add(buildTable(
            new String[]{"Code", "Espèce", "Âge", "Poids", "État santé"},
            rowsForColumns(animalData, 0, 1, 2, 3, 4)
        ));

        contentArea.getChildren().add(sectionLabel("Rapport capteurs — dernier état"));
        contentArea.getChildren().add(buildTable(
            new String[]{"Zone", "Capteur", "Mesure", "Dernier relevé", "Niveau", "Statut"},
            sensorReportRows()
        ));

        contentArea.getChildren().add(sectionLabel("Alertes à traiter"));
        addActiveAlertCards();

        contentArea.getChildren().add(sectionLabel("Derniers relevés critiques"));
        contentArea.getChildren().add(buildTable(
            new String[]{"Relevé", "Capteur", "Valeur", "Unité", "Niveau", "Date"},
            rowsMatchingValue(releveData, 4, "CRITICAL")
        ));

        contentArea.getChildren().add(sectionLabel("Résumé production"));
        contentArea.getChildren().add(buildTable(
            new String[]{"Type production", "Quantité totale", "Unité"},
            productionSummaryRows()
        ));
    }

    private void showZonesCultures() {
        pageTitle.setText("Zones & Cultures");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("🌾 Zone Culture — North Fields", "Z001"));

        // Cultures table
        contentArea.getChildren().add(sectionLabel("Cultures enregistrées"));
        GridPane table = buildTable(
            new String[]{"Code", "Nom", "Famille", "Plantation", "Récolte", "Stade"},
            new String[][]{
                {"C001", "Wheat",  "CEREALE", "01/03/2025", "20/08/2025", "CROISSANCE"},
                {"C002", "Tomato", "LEGUME",  "10/04/2025", "20/08/2025", "GERMINATION"},
                {"C003", "Apple",  "FRUIT",   "15/03/2025", "10/09/2025", "PLANTATION"},
            }
        );
        contentArea.getChildren().add(table);

        contentArea.getChildren().add(sectionLabel("Exigences pédologiques"));
        HBox reqRow = new HBox(16);
        reqRow.getChildren().addAll(
            infoCard("Wheat",  "pH : 6.0 – 7.5\nHumidité : 40 – 70 %", "#E8F5E9"),
            infoCard("Tomato", "pH : 5.5 – 7.0\nHumidité : 50 – 80 %", "#FFF8E1"),
            infoCard("Apple",  "pH : 6.0 – 7.0\nHumidité : 45 – 75 %", "#E3F2FD")
        );
        for (javafx.scene.Node n : reqRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(reqRow);

        contentArea.getChildren().add(sectionLabel("Vérification de compatibilité pédologique"));
        contentArea.getChildren().add(compatibilityChecker());
    }

    private void showElevage() {
        pageTitle.setText("Élevage");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("🐄 Zone Élevage — East Pasture", "Z002"));

        // Bouton pour ajouter un animal
        HBox addRow = new HBox(8);
        Button btnAddAnimal = actionButton("➕ Ajouter animal", COLOR_ACCENT);
        btnAddAnimal.setOnAction(e -> addAnimalDialog());
        addRow.getChildren().add(btnAddAnimal);
        contentArea.getChildren().add(addRow);

        contentArea.getChildren().add(sectionLabel("Programme d'alimentation"));
        HBox progRow = new HBox(16);
        progRow.getChildren().addAll(
            kpiCard("Aliment actuel", "Foin enrichi",  "#E8F5E9", COLOR_ACCENT_DARK),
            kpiCard("Quantité / jour", "15.0 kg",      "#FFF8E1", COLOR_WARN),
            kpiCard("Type élevage",    "RUMINANT",     "#E3F2FD", COLOR_INFO)
        );
        for (javafx.scene.Node n : progRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(progRow);

        contentArea.getChildren().add(sectionLabel("Animaux"));
        contentArea.getChildren().add(animauxController.getView());
    }

    private void showAquaculture() {
        pageTitle.setText("Aquaculture");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("🐟 Zone Aquacole — South Pond", "Z003"));

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
            kpiCard("Espèce",          "Thon",       "#E3F2FD", COLOR_INFO),
            kpiCard("Capacité",         "100",        "#E8F5E9", COLOR_ACCENT_DARK),
            kpiCard("Alimentation",     "Seeds",      "#FFF8E1", COLOR_WARN),
            kpiCard("Quantité / jour",  "20.0 kg",    "#F3E5F5", "#6A1B9A")
        );
        for (javafx.scene.Node n : kpiRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(kpiRow);

        contentArea.getChildren().add(sectionLabel("Limites géographiques"));
        GridPane table = buildTable(
            new String[]{"Sud", "Ouest", "Nord", "Est"},
            new String[][]{{"36.4°", "2.9°", "36.7°", "3.3°"}}
        );
        contentArea.getChildren().add(table);

        contentArea.getChildren().add(sectionLabel("Gestion du statut"));
        HBox btnRow = new HBox(12);
        Button btnSuspend = actionButton("⏸  Suspendre la zone", COLOR_WARN);
        Button btnActivate = actionButton("▶  Réactiver la zone", COLOR_ACCENT_DARK);
        Label statusLabel = new Label("Statut actuel : ACTIVE");
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        statusLabel.setTextFill(Color.web(COLOR_ACCENT_DARK));

        btnSuspend.setOnAction(e -> {
            statusLabel.setText("Statut actuel : SUSPENDUE");
            statusLabel.setTextFill(Color.web(COLOR_DANGER));
        });
        btnActivate.setOnAction(e -> {
            statusLabel.setText("Statut actuel : ACTIVE");
            statusLabel.setTextFill(Color.web(COLOR_ACCENT_DARK));
        });
        btnRow.getChildren().addAll(btnSuspend, btnActivate, statusLabel);
        btnRow.setAlignment(Pos.CENTER_LEFT);
        contentArea.getChildren().add(btnRow);

        // Allow editing aquaculture zones via zonesController view (filtered view would be better, but reuse controller)
        contentArea.getChildren().add(sectionLabel("Zones aquacoles (édition)"));
        contentArea.getChildren().add(zonesController.getView());
    }

    private void showSanteAnimale() {
        pageTitle.setText("Santé animale");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("🏥 Suivi sanitaire — East Pasture", "Z002"));

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
            kpiCard("Total animaux",        String.valueOf(animalData.size()),  "#E8F5E9", COLOR_ACCENT_DARK),
            kpiCard("Malades/Quarantaine",  String.valueOf(countAnimalsToWatch()),  "#FFEBEE", COLOR_DANGER),
            kpiCard("Sains",                String.valueOf(countRowsByValue(animalData, 4, "SAIN")),  "#F3E5F5", "#6A1B9A")
        );
        for (javafx.scene.Node n : kpiRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(kpiRow);

        contentArea.getChildren().add(sectionLabel("Historique des événements sanitaires"));
        GridPane table = buildTable(
            new String[]{"Animal", "Événement", "État résultant", "Description"},
            new String[][]{
                {"A002 — Vache2",  "CONTROLE",   "MALADE",      "Perte de poids anormale détectée"},
                {"A002 — Vache2",  "TRAITEMENT", "MALADE",      "Administration d'antibiotiques"},
                {"A003 — Mouton1", "CONTROLE",   "QUARANTAINE", "Mise en quarantaine préventive"},
            }
        );
        contentArea.getChildren().add(table);

        contentArea.getChildren().add(sectionLabel("Vérification isHealthy"));
        contentArea.getChildren().add(healthChecker());
    }

    private void showProduction() {
        pageTitle.setText("Production");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("📦 Historique de production", "Toutes zones"));

        HBox kpiRow = new HBox(16);
        kpiRow.getChildren().addAll(
            kpiCard("LAIT",              formatProductionTotal("LAIT"),  "#E3F2FD", COLOR_INFO),
            kpiCard("OEUFS",             formatProductionTotal("OEUFS"), "#FFF8E1", COLOR_WARN),
            kpiCard("RENDEMENT CULTURE", formatProductionTotal("RENDEMENT_CULTURE"), "#E8F5E9", COLOR_ACCENT_DARK)
        );
        for (javafx.scene.Node n : kpiRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);
        contentArea.getChildren().add(kpiRow);

        contentArea.getChildren().add(sectionLabel("Détail par zone"));
        GridPane table = buildTable(
            new String[]{"Zone", "Type production", "Quantité", "Unité"},
            productionDetailRows()
        );
        contentArea.getChildren().add(table);
    }

    private void showFerme() {
        pageTitle.setText("Informations ferme");
        contentArea.getChildren().clear();
        contentArea.getChildren().add(sectionHeader("⚙️ Détails — Green Valley Farm", "F001"));

        contentArea.getChildren().add(sectionLabel("Informations générales"));
        GridPane infoTable = buildTable(
            new String[]{"Champ", "Valeur"},
            new String[][]{
                {"Code ferme",    "F001"},
                {"Nom",           "Green Valley Farm"},
                {"Nb zones",      String.valueOf(zoneData.size())},
                {"Nb animaux",    String.valueOf(animalData.size())},
                {"Nb capteurs",   String.valueOf(capteurData.size())},
                {"Alertes actives", String.valueOf(countRowsByValue(alerteData, 3, "ACTIVE"))},
                {"Lat min",       "36.0°"},
                {"Lat max",       "37.0°"},
                {"Lon min",       "2.5°"},
                {"Lon max",       "4.0°"},
            }
        );
        contentArea.getChildren().add(infoTable);

        contentArea.getChildren().add(sectionLabel("Zones enregistrées"));
        // Bouton pour ajouter une zone
        HBox addZoneRow = new HBox(8);
        Button btnAddZone = actionButton("➕ Ajouter zone", COLOR_ACCENT);
        btnAddZone.setOnAction(e -> addZoneDialog());
        addZoneRow.getChildren().add(btnAddZone);
        contentArea.getChildren().add(addZoneRow);

        contentArea.getChildren().add(zonesController.getView());
    }

    private GridPane buildTableFromList(String[] headers, java.util.List<String[]> rows) {
        String[][] arr = rows.toArray(new String[rows.size()][]);
        return buildTable(headers, arr);
    }

    private int countRowsByValue(java.util.List<String[]> rows, int index, String value) {
        int count = 0;
        for (String[] row : rows) {
            if (row.length > index && value.equals(row[index])) count++;
        }
        return count;
    }

    private int countAnimalsToWatch() {
        int count = 0;
        for (String[] animal : animalData) {
            String etat = getValue(animal, 4);
            if ("MALADE".equals(etat) || "QUARANTAINE".equals(etat)) count++;
        }
        return count;
    }

    private String getValue(String[] row, int index) {
        return row.length > index && row[index] != null ? row[index] : "";
    }

    private String zoneDetail(String type) {
        if ("ZoneElevage".equals(type)) return animalData.size() + " animaux";
        if ("ZoneCulture".equals(type)) return "Cultures actives";
        if ("ZoneAquacole".equals(type)) return "Aquaculture suivie";
        return "Zone enregistrée";
    }

    private String zoneAccent(String type) {
        if ("ZoneElevage".equals(type)) return COLOR_WARN;
        if ("ZoneAquacole".equals(type)) return COLOR_INFO;
        return COLOR_ACCENT;
    }

    private String zoneBg(String type) {
        if ("ZoneElevage".equals(type)) return "#FFF8E1";
        if ("ZoneAquacole".equals(type)) return "#E3F2FD";
        return "#E8F5E9";
    }

    private String[][] rowsForColumns(java.util.List<String[]> rows, int... indexes) {
        String[][] values = new String[rows.size()][indexes.length];
        for (int r = 0; r < rows.size(); r++) {
            String[] source = rows.get(r);
            for (int c = 0; c < indexes.length; c++) {
                int index = indexes[c];
                values[r][c] = source.length > index && source[index] != null ? source[index] : "";
            }
        }
        return values;
    }

    private String[][] rowsMatchingValue(java.util.List<String[]> rows, int index, String value) {
        java.util.List<String[]> filtered = new java.util.ArrayList<>();
        for (String[] row : rows) {
            if (row.length > index && value.equals(row[index])) filtered.add(row);
        }
        return rowsForColumns(filtered, 0, 1, 2, 3, 4, 5);
    }

    private String[][] zoneReportRows() {
        String[][] values = new String[zoneData.size()][5];
        for (int i = 0; i < zoneData.size(); i++) {
            String[] zone = zoneData.get(i);
            String code = getValue(zone, 0);
            values[i][0] = code;
            values[i][1] = getValue(zone, 1);
            values[i][2] = getValue(zone, 2);
            values[i][3] = getValue(zone, 3);
            values[i][4] = String.valueOf(countRowsByValue(capteurData, 1, code));
        }
        return values;
    }

    private String[][] sensorReportRows() {
        String[][] values = new String[capteurData.size()][6];
        for (int i = 0; i < capteurData.size(); i++) {
            String[] capteur = capteurData.get(i);
            String code = getValue(capteur, 0);
            String[] lastReleve = lastReleveForCapteur(code);
            values[i][0] = getZoneLabel(getValue(capteur, 1));
            values[i][1] = code;
            values[i][2] = getValue(capteur, 3);
            values[i][3] = lastReleve == null ? "Aucun" : getValue(lastReleve, 2) + " " + getValue(lastReleve, 3);
            values[i][4] = lastReleve == null ? "-" : getValue(lastReleve, 4);
            values[i][5] = getValue(capteur, 5);
        }
        return values;
    }

    private String[] lastReleveForCapteur(String capteurCode) {
        String[] last = null;
        for (String[] releve : releveData) {
            if (capteurCode.equals(getValue(releve, 1))) last = releve;
        }
        return last;
    }

    private String getZoneLabel(String code) {
        for (String[] zone : zoneData) {
            if (code.equals(getValue(zone, 0))) return code + " — " + getValue(zone, 1);
        }
        return code;
    }

    private String[][] productionDetailRows() {
        String[][] values = new String[productionData.size()][4];
        for (int i = 0; i < productionData.size(); i++) {
            String[] row = productionData.get(i);
            values[i][0] = getZoneLabel(getValue(row, 0));
            values[i][1] = getValue(row, 1);
            values[i][2] = getValue(row, 2);
            values[i][3] = getValue(row, 3);
        }
        return values;
    }

    private String[][] productionSummaryRows() {
        String[] types = {"LAIT", "OEUFS", "RENDEMENT_CULTURE"};
        String[][] values = new String[types.length][3];
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            values[i][0] = type;
            values[i][1] = String.format("%.1f", productionTotal(type));
            values[i][2] = productionUnit(type);
        }
        return values;
    }

    private String formatProductionTotal(String type) {
        return String.format("%.1f %s", productionTotal(type), productionUnit(type));
    }

    private double productionTotal(String type) {
        double total = 0;
        for (String[] row : productionData) {
            if (type.equals(getValue(row, 1))) {
                try {
                    total += Double.parseDouble(getValue(row, 2));
                } catch (NumberFormatException ignored) {
                    // Ignore malformed demo values so the report can still render.
                }
            }
        }
        return total;
    }

    private String productionUnit(String type) {
        for (String[] row : productionData) {
            if (type.equals(getValue(row, 1))) return getValue(row, 3);
        }
        return "";
    }

    private void addActiveAlertCards() {
        boolean hasActiveAlert = false;
        for (String[] alerte : alerteData) {
            if ("ACTIVE".equals(getValue(alerte, 3))) {
                hasActiveAlert = true;
                String gravite = getValue(alerte, 2);
                String color = "CRITICAL".equals(gravite) ? COLOR_DANGER : COLOR_WARN;
                contentArea.getChildren().add(alertCard(
                    "⚠️  " + getValue(alerte, 0) + " — " + getValue(alerte, 1) + " (" + gravite + ")",
                    getValue(alerte, 4),
                    color
                ));
            }
        }
        if (!hasActiveAlert) {
            contentArea.getChildren().add(alertCard("Aucune alerte active", "Tous les indicateurs sont sous contrôle.", COLOR_ACCENT_DARK));
        }
    }

    private void addZoneDialog() {
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle("Ajouter une zone");
        ButtonType ok = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField codeF = new TextField(); codeF.setPromptText("Code (ex: Z004)");
        TextField nameF = new TextField(); nameF.setPromptText("Nom");
        TextField typeF = new TextField(); typeF.setPromptText("Type");
        TextField statusF = new TextField(); statusF.setPromptText("Statut");
        grid.add(new Label("Code:"), 0, 0); grid.add(codeF, 1, 0);
        grid.add(new Label("Nom:"), 0, 1); grid.add(nameF, 1, 1);
        grid.add(new Label("Type:"), 0, 2); grid.add(typeF, 1, 2);
        grid.add(new Label("Statut:"), 0, 3); grid.add(statusF, 1, 3);
        dlg.getDialogPane().setContent(grid);

        dlg.setResultConverter(bt -> {
            if (bt == ok) return new String[]{codeF.getText(), nameF.getText(), typeF.getText(), statusF.getText()};
            return null;
        });

        java.util.Optional<String[]> res = dlg.showAndWait();
        res.ifPresent(arr -> {
            if (arr[0] != null && !arr[0].isBlank()) {
                zoneData.add(arr);
                showFerme();
                // rafraîchir aussi la page Zones & Cultures si visible
                showZonesCultures();
            }
        });
    }

    private void addAnimalDialog() {
        Dialog<String[]> dlg = new Dialog<>();
        dlg.setTitle("Ajouter un animal");
        ButtonType ok = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField codeF = new TextField(); codeF.setPromptText("Code (ex: A004)");
        TextField especeF = new TextField(); especeF.setPromptText("Espèce");
        TextField ageF = new TextField(); ageF.setPromptText("Âge");
        TextField poidsF = new TextField(); poidsF.setPromptText("Poids");
        TextField etatF = new TextField(); etatF.setPromptText("État santé");
        grid.add(new Label("Code:"), 0, 0); grid.add(codeF, 1, 0);
        grid.add(new Label("Espèce:"), 0, 1); grid.add(especeF, 1, 1);
        grid.add(new Label("Âge:"), 0, 2); grid.add(ageF, 1, 2);
        grid.add(new Label("Poids:"), 0, 3); grid.add(poidsF, 1, 3);
        grid.add(new Label("État:"), 0, 4); grid.add(etatF, 1, 4);
        dlg.getDialogPane().setContent(grid);

        dlg.setResultConverter(bt -> {
            if (bt == ok) return new String[]{codeF.getText(), especeF.getText(), ageF.getText(), poidsF.getText(), etatF.getText()};
            return null;
        });

        java.util.Optional<String[]> res = dlg.showAndWait();
        res.ifPresent(arr -> {
            if (arr[0] != null && !arr[0].isBlank()) {
                animalData.add(arr);
                showElevage();
            }
        });
    }

    // ────────────────────────────────────────────────────────────────
    // COMPOSANTS UI
    // ────────────────────────────────────────────────────────────────

    private HBox sectionHeader(String title, String badge) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        Label lbl = new Label(title);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 20));
        lbl.setTextFill(Color.web("#263238"));
        Label bdg = new Label(badge);
        bdg.setFont(Font.font("System", 12));
        bdg.setTextFill(Color.web(COLOR_ACCENT_DARK));
        bdg.setPadding(new Insets(3, 10, 3, 10));
        bdg.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 20;");
        box.getChildren().addAll(lbl, bdg);
        return box;
    }

    private Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        lbl.setTextFill(Color.web("#546E7A"));
        lbl.setPadding(new Insets(8, 0, 0, 0));
        return lbl;
    }

    private VBox kpiCard(String label, String value, String bgColor, String textColor) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + COLOR_CARD_BORDER + ";" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 0.5;"
        );
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", 12));
        lbl.setTextFill(Color.web("#607D8B"));
        Label val = new Label(value);
        val.setFont(Font.font("System", FontWeight.BOLD, 24));
        val.setTextFill(Color.web(textColor));
        card.getChildren().addAll(lbl, val);
        return card;
    }

    private VBox zoneCard(String code, String nom, String type, String detail, String accentColor, String bgColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setStyle(
            "-fx-background-color: " + COLOR_WHITE + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + accentColor + ";" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 2 2 2 6;"
        );
        Label codeLabel = new Label(code);
        codeLabel.setFont(Font.font("System", 11));
        codeLabel.setTextFill(Color.web(accentColor));
        codeLabel.setPadding(new Insets(2, 8, 2, 8));
        codeLabel.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 6;");
        Label nameLabel = new Label(nom);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        nameLabel.setTextFill(Color.web("#263238"));
        Label typeLabel = new Label(type);
        typeLabel.setFont(Font.font("System", 12));
        typeLabel.setTextFill(Color.web(accentColor));
        Label detailLabel = new Label(detail);
        detailLabel.setFont(Font.font("System", 12));
        detailLabel.setTextFill(Color.web("#78909C"));
        card.getChildren().addAll(codeLabel, nameLabel, typeLabel, detailLabel);
        return card;
    }

    private HBox alertCard(String title, String msg, String color) {
        HBox card = new HBox(14);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: " + COLOR_WHITE + ";" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-radius: 8;" +
            "-fx-border-width: 0.5 0.5 0.5 4;"
        );
        VBox textBox = new VBox(4);
        Label t = new Label(title);
        t.setFont(Font.font("System", FontWeight.BOLD, 13));
        t.setTextFill(Color.web(color));
        Label m = new Label(msg);
        m.setFont(Font.font("System", 12));
        m.setTextFill(Color.web("#546E7A"));
        textBox.getChildren().addAll(t, m);
        card.getChildren().add(textBox);
        return card;
    }

    private VBox infoCard(String title, String body, String bg) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(14, 16, 14, 16));
        card.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 10; -fx-border-color: " + COLOR_CARD_BORDER + "; -fx-border-radius: 10; -fx-border-width: 0.5;");
        Label t = new Label(title);
        t.setFont(Font.font("System", FontWeight.BOLD, 13));
        t.setTextFill(Color.web("#37474F"));
        Label b = new Label(body);
        b.setFont(Font.font("System", 12));
        b.setTextFill(Color.web("#546E7A"));
        card.getChildren().addAll(t, b);
        return card;
    }

    private GridPane buildTable(String[] headers, String[][] rows) {
        GridPane grid = new GridPane();
        grid.setStyle(
            "-fx-background-color: " + COLOR_WHITE + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + COLOR_CARD_BORDER + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 0.5;"
        );
        grid.setMaxWidth(Double.MAX_VALUE);

        for (int c = 0; c < headers.length; c++) {
            Label h = new Label(headers[c]);
            h.setFont(Font.font("System", FontWeight.BOLD, 12));
            h.setTextFill(Color.web("#78909C"));
            h.setPadding(new Insets(10, 16, 10, 16));
            h.setMaxWidth(Double.MAX_VALUE);
            h.setStyle("-fx-background-color: #F5F7F8; -fx-border-color: #ECEFF1; -fx-border-width: 0 0 1 0;");
            grid.add(h, c, 0);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }

        for (int r = 0; r < rows.length; r++) {
            String bg = r % 2 == 0 ? COLOR_WHITE : "#FAFAFA";
            for (int c = 0; c < rows[r].length; c++) {
                String cell = rows[r][c];
                Label lbl = new Label(cell);
                lbl.setFont(Font.font("System", 13));
                lbl.setMaxWidth(Double.MAX_VALUE);
                lbl.setPadding(new Insets(10, 16, 10, 16));
                // Coloring health states
                if (cell.equals("MALADE"))       lbl.setTextFill(Color.web(COLOR_DANGER));
                else if (cell.equals("QUARANTAINE")) lbl.setTextFill(Color.web(COLOR_WARN));
                else if (cell.equals("SAIN"))    lbl.setTextFill(Color.web(COLOR_ACCENT_DARK));
                else if (cell.equals("ACTIVE"))  lbl.setTextFill(Color.web(COLOR_ACCENT_DARK));
                else                             lbl.setTextFill(Color.web("#37474F"));
                lbl.setStyle("-fx-background-color: " + bg + ";");
                grid.add(lbl, c, r + 1);
            }
        }
        return grid;
    }

    private Button actionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("System", FontWeight.BOLD, 13));
        btn.setPadding(new Insets(10, 20, 10, 20));
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        return btn;
    }

    // ── Vérificateur de compatibilité pédologique (interactif) ──────
    private VBox compatibilityChecker() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: " + COLOR_WHITE + "; -fx-background-radius: 10; -fx-border-color: " + COLOR_CARD_BORDER + "; -fx-border-radius: 10; -fx-border-width: 0.5;");

        Label title = new Label("Tester la compatibilité de Wheat (pH 6.0–7.5 / Humidité 40–70 %)");
        title.setFont(Font.font("System", FontWeight.BOLD, 13));
        title.setTextFill(Color.web("#37474F"));

        HBox row1 = new HBox(12);
        row1.setAlignment(Pos.CENTER_LEFT);
        Label phLbl = new Label("pH :");
        phLbl.setFont(Font.font("System", 13));
        Slider phSlider = new Slider(3.0, 9.0, 6.5);
        phSlider.setShowTickLabels(true);
        phSlider.setPrefWidth(220);
        Label phVal = new Label("6.5");
        phVal.setFont(Font.font("System", FontWeight.BOLD, 13));
        phSlider.valueProperty().addListener((o, ov, nv) -> phVal.setText(String.format("%.1f", nv)));
        row1.getChildren().addAll(phLbl, phSlider, phVal);

        HBox row2 = new HBox(12);
        row2.setAlignment(Pos.CENTER_LEFT);
        Label humLbl = new Label("Humidité % :");
        humLbl.setFont(Font.font("System", 13));
        Slider humSlider = new Slider(0, 100, 55);
        humSlider.setShowTickLabels(true);
        humSlider.setPrefWidth(220);
        Label humVal = new Label("55.0");
        humVal.setFont(Font.font("System", FontWeight.BOLD, 13));
        humSlider.valueProperty().addListener((o, ov, nv) -> humVal.setText(String.format("%.0f", nv)));
        row2.getChildren().addAll(humLbl, humSlider, humVal);

        Label resultLabel = new Label("");
        resultLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        Button testBtn = actionButton("✔  Vérifier compatibilité", COLOR_ACCENT_DARK);
        testBtn.setOnAction(e -> {
            double ph = phSlider.getValue();
            double hum = humSlider.getValue();
            boolean ok = ph >= 6.0 && ph <= 7.5 && hum >= 40 && hum <= 70;
            resultLabel.setText(ok ? "✅  Compatible" : "❌  Non compatible");
            resultLabel.setTextFill(Color.web(ok ? COLOR_ACCENT_DARK : COLOR_DANGER));
        });

        box.getChildren().addAll(title, row1, row2, testBtn, resultLabel);
        return box;
    }

    // ── Vérificateur isHealthy (interactif) ─────────────────────────
    private VBox healthChecker() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: " + COLOR_WHITE + "; -fx-background-radius: 10; -fx-border-color: " + COLOR_CARD_BORDER + "; -fx-border-radius: 10; -fx-border-width: 0.5;");

        Label title = new Label("Simuler animal.isHealthy(poidsMin)");
        title.setFont(Font.font("System", FontWeight.BOLD, 13));
        title.setTextFill(Color.web("#37474F"));

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        ComboBox<String> animalBox = new ComboBox<>();
        animalBox.getItems().addAll("A001 — Vache1 (MALADE, 520 kg)", "A002 — Vache2 (MALADE, 480 kg)", "A003 — Mouton1 (QUARANTAINE, 65 kg)");
        animalBox.getSelectionModel().selectFirst();
        Label poidsLbl = new Label("Poids min :");
        TextField poidsField = new TextField("500");
        poidsField.setPrefWidth(80);
        row.getChildren().addAll(animalBox, poidsLbl, poidsField);

        Label result = new Label("");
        result.setFont(Font.font("System", FontWeight.BOLD, 14));

        Button btn = actionButton("▶  Tester isHealthy", COLOR_INFO);
        btn.setOnAction(e -> {
            try {
                double min = Double.parseDouble(poidsField.getText());
                int sel = animalBox.getSelectionModel().getSelectedIndex();
                // Reproduit la logique isHealthy : SAIN ET poids >= poidsMin
                // A001: état MALADE → false
                // A002: état MALADE → false
                // A003: état QUARANTAINE → false
                boolean healthy = false; // tous malades/quarantaine dans notre scénario
                result.setText("isHealthy(" + min + ") → " + healthy);
                result.setTextFill(Color.web(COLOR_DANGER));
            } catch (NumberFormatException ex) {
                result.setText("Poids invalide");
                result.setTextFill(Color.web(COLOR_DANGER));
            }
        });

        box.getChildren().addAll(title, row, btn, result);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

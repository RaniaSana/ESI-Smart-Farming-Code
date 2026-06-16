package Utilitaires;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.*;

import Classe_Principale.SmartFarming;
import Zones.Zone;

/**
 * Simple utilitaire pour sauvegarder / charger l'état de la ferme en JSON.
 * Utilise un adaptateur personnalisé pour sérialiser les sous-classes de Zone.
 */
public class DataStore {

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path DEFAULT_FILE = DATA_DIR.resolve("smartfarming.json");

    private final Gson gson;

    public DataStore() {
        // Gson sans l'adaptateur pour sérialiser les sous-objets internes
        Gson base = new GsonBuilder().setPrettyPrinting().create();

        // Adapter pour Zone : ajoute le nom de classe lors de la sérialisation
        JsonSerializer<Zone> serializer = (zone, typeOfSrc, context) -> {
            JsonObject obj = base.toJsonTree(zone).getAsJsonObject();
            obj.addProperty("__class", zone.getClass().getName());
            return obj;
        };

        JsonDeserializer<Zone> deserializer = (json, typeOfT, context) -> {
            JsonObject obj = json.getAsJsonObject();
            JsonElement clsEl = obj.get("__class");
            if (clsEl == null) {
                throw new JsonParseException("Missing __class property for Zone deserialization");
            }
            String className = clsEl.getAsString();
            try {
                Class<?> cls = Class.forName(className);
                return (Zone) base.fromJson(obj, (Type) cls);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        };

        this.gson = new GsonBuilder()
            .registerTypeAdapter(Zone.class, serializer)
            .registerTypeAdapter(Zone.class, deserializer)
            .setPrettyPrinting()
            .create();
    }

    public void save(SmartFarming farm) throws IOException {
        if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
        try (Writer w = Files.newBufferedWriter(DEFAULT_FILE)) {
            gson.toJson(farm, w);
        }
    }

    public SmartFarming load() throws IOException {
        if (!Files.exists(DEFAULT_FILE)) return null;
        try (Reader r = Files.newBufferedReader(DEFAULT_FILE)) {
            return gson.fromJson(r, SmartFarming.class);
        }
    }

    // Simple save/load for UI lists (zones/animals) represented as arrays of strings
    public static class SimpleData {
        public java.util.List<String[]> zones = new java.util.ArrayList<>();
        public java.util.List<String[]> animals = new java.util.ArrayList<>();
        public java.util.List<String[]> capteurs = new java.util.ArrayList<>();
        public java.util.List<String[]> alertes = new java.util.ArrayList<>();
        public java.util.List<String[]> releves = new java.util.ArrayList<>();
    }

    public void saveSimple(java.util.List<String[]> zones, java.util.List<String[]> animals) throws IOException {
        saveSimple(zones, animals, java.util.List.of(), java.util.List.of(), java.util.List.of());
    }

    public void saveSimple(
        java.util.List<String[]> zones,
        java.util.List<String[]> animals,
        java.util.List<String[]> capteurs,
        java.util.List<String[]> alertes,
        java.util.List<String[]> releves
    ) throws IOException {
        if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
        JsonObject root = new JsonObject();
        root.add("zones", toJsonArray(zones));
        root.add("animals", toJsonArray(animals));
        root.add("capteurs", toJsonArray(capteurs));
        root.add("alertes", toJsonArray(alertes));
        root.add("releves", toJsonArray(releves));
        try (Writer w = Files.newBufferedWriter(DEFAULT_FILE)) {
            gson.toJson(root, w);
        }
    }

    public SimpleData loadSimple() throws IOException {
        SimpleData sd = new SimpleData();
        if (!Files.exists(DEFAULT_FILE)) return sd;
        try (Reader r = Files.newBufferedReader(DEFAULT_FILE)) {
            JsonElement el = JsonParser.parseReader(r);
            if (!el.isJsonObject()) return sd;
            JsonObject obj = el.getAsJsonObject();
            if (obj.has("zones") && obj.get("zones").isJsonArray()) {
                for (JsonElement ze : obj.getAsJsonArray("zones")) {
                    JsonArray arr = ze.getAsJsonArray();
                    String[] row = new String[arr.size()];
                    for (int i = 0; i < arr.size(); i++) row[i] = arr.get(i).isJsonNull() ? null : arr.get(i).getAsString();
                    sd.zones.add(row);
                }
            }
            if (obj.has("animals") && obj.get("animals").isJsonArray()) {
                for (JsonElement ae : obj.getAsJsonArray("animals")) {
                    JsonArray arr = ae.getAsJsonArray();
                    String[] row = new String[arr.size()];
                    for (int i = 0; i < arr.size(); i++) row[i] = arr.get(i).isJsonNull() ? null : arr.get(i).getAsString();
                    sd.animals.add(row);
                }
            }
            loadRows(obj, "capteurs", sd.capteurs);
            loadRows(obj, "alertes", sd.alertes);
            loadRows(obj, "releves", sd.releves);
        }
        return sd;
    }

    private JsonArray toJsonArray(java.util.List<String[]> values) {
        JsonArray jsonRows = new JsonArray();
        for (String[] valuesRow : values) {
            JsonArray row = new JsonArray();
            for (String value : valuesRow) {
                row.add(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
            }
            jsonRows.add(row);
        }
        return jsonRows;
    }

    private void loadRows(JsonObject obj, String key, java.util.List<String[]> target) {
        if (!obj.has(key) || !obj.get(key).isJsonArray()) return;
        for (JsonElement element : obj.getAsJsonArray(key)) {
            JsonArray arr = element.getAsJsonArray();
            String[] row = new String[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                row[i] = arr.get(i).isJsonNull() ? null : arr.get(i).getAsString();
            }
            target.add(row);
        }
    }
}

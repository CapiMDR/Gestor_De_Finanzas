package accounts.account_model;

import org.json.JSONArray;
import org.json.JSONObject;

import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;

import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;
import goals.goals_model.Goal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Clase encargada de serializar y deserializar datos relacionados con cuentas,
 * movimientos, metas y categorías hacia y desde archivos JSON.
 * @author Martín Jesús Pool Chuc
 */
public class JsonDataHandler {

    /** Ruta del archivo donde se guardan los datos de cuentas. */
    private static final String FILE_PATH = "accounts_data.json";

    /** Ruta del archivo donde se guardan los datos de categorías. */
    private static final String CATEGORIES_FILE_PATH = "categories_data.json";

    // SERIALIZATION

    /**
     * Convierte una lista de cuentas en un JSONArray.
     *
     * @param accounts lista de cuentas a convertir
     * @return JSONArray representando las cuentas
     */
    public JSONArray accountsToJson(List<Account> accounts) {
        JSONArray jsonArray = new JSONArray();

        for (Account account : accounts) {
            JSONObject accountJson = new JSONObject();
            accountJson.put("id", account.getId());
            accountJson.put("name", account.getName());
            accountJson.put("type", account.getType());
            accountJson.put("coin", account.getCoin());
            accountJson.put("initialBalance", account.getInitialBalance());

            accountJson.put("movements", movementsToJson(account.getMovements()));
            accountJson.put("goals", goalsToJson(account.getGoals()));

            jsonArray.put(accountJson);
        }
        return jsonArray;
    }

    /**
     * Convierte una lista de movimientos en un JSONArray.
     *
     * @param movements lista de movimientos
     * @return JSONArray representando movimientos
     */
    private JSONArray movementsToJson(List<Movement> movements) {
        JSONArray jsonArray = new JSONArray();
        for (Movement movement : movements) {
            JSONObject movementJson = new JSONObject();
            movementJson.put("idMovement", movement.getIdMovement());
            movementJson.put("description", movement.getDescription());
            movementJson.put("amount", movement.getAmount().toString());
            movementJson.put("date", movement.getDate().toString());
            movementJson.put("categoryName", movement.getCategory().getName());
            movementJson.put("categoryType", movement.getCategory().getType().name());

            jsonArray.put(movementJson);
        }
        return jsonArray;
    }

    /**
     * Convierte una lista de metas en un JSONArray.
     *
     * @param goals lista de metas
     * @return JSONArray representando metas
     */
    private JSONArray goalsToJson(List<Goal> goals) {
        JSONArray jsonArray = new JSONArray();

        for (Goal goal : goals) {
            JSONObject goalJson = new JSONObject();
            goalJson.put("name", goal.getName());
            goalJson.put("targetAmount", goal.getTargetAmount());
            goalJson.put("amount", goal.getCurrentAmount());
            goalJson.put("currentAmount", goal.getCurrentAmount());
            goalJson.put("description", goal.getDescription());

            jsonArray.put(goalJson);
        }

        return jsonArray;
    }

    /**
     * Convierte un mapa de categorías en un JSONArray.
     *
     * @param categories mapa de categorías
     * @return JSONArray representando categorías
     */
    public JSONArray categoriesToJson(HashMap<String, MovementCategory> categories) {
        JSONArray jsonArray = new JSONArray();

        for (MovementCategory category : categories.values()) {
            JSONObject categoryJson = new JSONObject();
            categoryJson.put("name", category.getName());
            categoryJson.put("type", category.getType().name());
            jsonArray.put(categoryJson);
        }
        return jsonArray;
    }

    // DESERIALIZATION

    /**
     * Convierte un JSONArray en una lista de cuentas.
     *
     * @param jsonArray JSONArray con datos de cuentas
     * @return lista de cuentas deserializadas
     */
    public List<Account> jsonToAccounts(JSONArray jsonArray) {
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject accountJson = jsonArray.getJSONObject(i);

            int id = accountJson.getInt("id");
            String name = accountJson.getString("name");
            AccountType type = AccountType.valueOf(accountJson.getString("type"));
            Coin coin = Coin.valueOf(accountJson.getString("coin"));

            String initialBalanceStr = accountJson.get("initialBalance").toString();
            BigDecimal initialBalance = new BigDecimal(initialBalanceStr);

            Account account = new Account(id, name, type, coin, initialBalance);

            JSONArray movementsJson = accountJson.getJSONArray("movements");
            List<Movement> movements = jsonToMovements(movementsJson, account);

            JSONArray goalsJson = accountJson.getJSONArray("goals");
            List<Goal> goals = jsonToGoals(goalsJson);

            account.setMovements(movements);
            account.setGoals(goals);

            BigDecimal balance = account.getInitialBalance();
            for (Movement mov : movements) {
                if (mov.getCategory().getType() == MovementType.INCOME) {
                    balance = balance.add(mov.getAmount());
                } else {
                    balance = balance.subtract(mov.getAmount());
                }
            }

            account.updateBalance(balance);

            accounts.add(account);
        }
        return accounts;
    }

    /**
     * Convierte un JSONArray en una lista de movimientos.
     *
     * @param movementsJson JSONArray de movimientos
     * @param account cuenta asociada al movimiento
     * @return lista de movimientos deserializados
     */
    private List<Movement> jsonToMovements(JSONArray movementsJson, Account account) {
        List<Movement> movements = new ArrayList<>();
        for (int i = 0; i < movementsJson.length(); i++) {
            JSONObject movementJson = movementsJson.getJSONObject(i);

            UUID idMovement = UUID.fromString(movementJson.getString("idMovement"));
            String description = movementJson.getString("description");

            String amountStr = movementJson.get("amount").toString();
            BigDecimal amount = new BigDecimal(amountStr);

            LocalDateTime date = LocalDateTime.parse(movementJson.getString("date"));

            String categoryName = movementJson.getString("categoryName");
            MovementType categoryType = MovementType.valueOf(movementJson.getString("categoryType"));
            MovementCategory category = new MovementCategory(categoryName, categoryType);

            Movement movement = new Movement(idMovement, description, amount, category, account, date);

            movements.add(movement);
        }
        return movements;
    }

    /**
     * Convierte un JSONArray en una lista de metas.
     *
     * @param goalsJson JSONArray con metas
     * @return lista de metas deserializadas
     */
    private List<Goal> jsonToGoals(JSONArray goalsJson) {
        List<Goal> goals = new ArrayList<>();
        for (int i = 0; i < goalsJson.length(); i++) {
            JSONObject goalJson = goalsJson.getJSONObject(i);

            String name = goalJson.getString("name");

            String targetAmountStr = goalJson.get("targetAmount").toString();
            BigDecimal targetAmount = new BigDecimal(targetAmountStr);

            String description = goalJson.getString("description");

            Goal goal = new Goal(name, targetAmount, description);

            goals.add(goal);
        }
        return goals;
    }

    /**
     * Convierte un JSONArray en un mapa de categorías.
     *
     * @param jsonArray JSONArray con categorías
     * @return mapa de categorías
     */
    public HashMap<String, MovementCategory> jsonToCategories(JSONArray jsonArray) {
        HashMap<String, MovementCategory> categories = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject categoryJson = jsonArray.getJSONObject(i);

            String name = categoryJson.getString("name");
            MovementType type = MovementType.valueOf(categoryJson.getString("type"));

            MovementCategory category = new MovementCategory(name, type);
            categories.put(name, category);
        }
        return categories;
    }

    /**
     * Guarda la lista de cuentas en un archivo JSON.
     *
     * @param accounts lista de cuentas a guardar
     */
    public void saveAccounts(List<Account> accounts) {
        JSONArray jsonArray = accountsToJson(accounts);
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(jsonArray.toString(2));
            System.out.println("Cuentas guardadas en " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda las categorías en un archivo JSON.
     *
     * @param categories mapa de categorías
     */
    public void saveCategories(HashMap<String, MovementCategory> categories) {
        JSONArray jsonArray = categoriesToJson(categories);
        try (FileWriter file = new FileWriter(CATEGORIES_FILE_PATH)) {
            file.write(jsonArray.toString(2));
            System.out.println("Categorías guardadas en " + CATEGORIES_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga las cuentas desde el archivo JSON.
     *
     * @return lista de cuentas cargadas o lista vacía en caso de error
     */
    public List<Account> loadAccounts() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Archivo de datos no encontrado o vacío. Iniciando con lista vacía.");
            return new ArrayList<>();
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);
            return jsonToAccounts(jsonArray);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al parsear el JSON: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Carga las categorías desde el archivo JSON.
     *
     * @return mapa de categorías cargado o vacío en caso de error
     */
    public HashMap<String, MovementCategory> loadCategories() {
        File file = new File(CATEGORIES_FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Archivo de categorías no encontrado o vacío. Iniciando con lista vacía.");
            return new HashMap<>();
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(CATEGORIES_FILE_PATH)));
            JSONArray jsonArray = new JSONArray(content);
            return jsonToCategories(jsonArray);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de categorías: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al parsear el JSON de categorías: " + e.getMessage());
        }
        return new HashMap<>();
    }
}
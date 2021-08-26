import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataBase {
    Connection connection;

    public Connection getConnection() {             // конектимся с ба3ой
        try {
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (Exception ex) {
            System.out.println("Driver not found");
        }

        String coonectionString = "jdbc:postgresql://localhost:5432/pred_project";

        try {
            connection = DriverManager.getConnection(coonectionString, "alex",
                    "alex");
        } catch (SQLException throwables) {
            throwables.getMessage();
        }
        return connection;
    }

    public void dropUsersTable(String name_table) {
        String SQL = "DROP TABLE " + name_table;
        connection = getConnection();
        try {
            PreparedStatement psSt = connection.prepareStatement(SQL);
            psSt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createUsersTable(String nameTable) {            // создаем таблицу
        String SQL = "CREATE TABLE IF NOT EXISTS " + nameTable +
                " (id        VARCHAR(50) PRIMARY KEY, " +
                "    firstName VARCHAR(50) not NULL, " +
                "    lastName  VARCHAR(50) not NULL, " +
                "    age       INTEGER     not NULL);";
        connection = getConnection();
        try {
            PreparedStatement psSt = connection.prepareStatement(SQL);
            psSt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addDatabase(User user, String nameTale) {             //добавляем в таблицу
        String insert = "INSERT INTO " + nameTale + " (id ,firstName, lastName,age)  " +
                "VALUES  (?,?,?,?)";
        try {
            PreparedStatement prST = getConnection().prepareStatement(insert);
            prST.setString(1, String.valueOf(user.getId()));
            prST.setString(2, user.getFirstName());
            prST.setString(3, user.getLastName());
            prST.setInt(4, user.getAge());
            prST.addBatch();
            prST.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeUserName(String nameTable, UUID uuid) {           // удаляем по id
        String SQL = "DELETE FROM " + nameTable + " WHERE id =?";
        connection = getConnection();
        try {

            PreparedStatement psSt = connection.prepareStatement(SQL);
            String str = uuid.toString();
            psSt.setString(1, str);
            psSt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<User> getAllUsers(String name_table) {              // достаем всех из таблицы
        String s = "SELECT * FROM " + name_table;
        connection = getConnection();
        List<User> usersList = new ArrayList<>();
        try {
            PreparedStatement psSt = connection.prepareStatement(s);
            ResultSet resultSet = psSt.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(UUID.fromString(resultSet.getString(1)));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getInt(4));
                usersList.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return usersList;
    }

    public User getUsersId(String name_table, UUID uuid) {                 //достаем юзера по id
        String s = "SELECT * FROM " + name_table + " WHERE id =?";
        connection = getConnection();
        User user = null;
        try {
            PreparedStatement psSt = connection.prepareStatement(s);
            String str = uuid.toString();
            psSt.setString(1, str);
            ResultSet resultSet = psSt.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(UUID.fromString(resultSet.getString(1)));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getInt(4));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public void reUserName(String name_table, UUID uuid, User user) {           // изменяем по id
        User userDost = getUsersId(name_table, uuid);
        try {
            userDost.setFirstName(user.getFirstName());
            userDost.setLastName(user.getLastName());
            removeUserName(name_table, uuid);
            addDatabase(userDost, name_table);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeUsers(String nameTable) {           // удаляем всех
        String SQL = "DELETE FROM " + nameTable;
        connection = getConnection();
        try {

            PreparedStatement psSt = connection.prepareStatement(SQL);
            psSt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

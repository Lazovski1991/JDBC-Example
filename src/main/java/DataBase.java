import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//todo все параметры соединения типа имени пароля, бд вынеси в отдельный enum
public class DataBase {
    Connection connection;//todo может модификатор какой нпишешь?

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

    public void dropUsersTable(String name_table) {//todo nameTable а не name_Table посмотри везде
        String SQL = "DROP TABLE " + name_table;
        connection = getConnection();
        try {
            PreparedStatement psSt = connection.prepareStatement(SQL);
            psSt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
//todo ты назвал метод создания юзеров таблицы, хотя имя таблицы передаешь параметром. логично назвать просто createTable. Ему ж без разницы какую таблицу создавать
    public void createUsersTable(String nameTable) {            // создаем таблицу 
        String SQL = "CREATE TABLE IF NOT EXISTS " + nameTable +
                " (id        VARCHAR(50) PRIMARY KEY, " +
                "    firstName VARCHAR(50) not NULL, " + //todo а тут как раз таки first_name и т.д
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
//todo ты добавляешь БД или юзера?
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
//todo ты что удаляешь имя или юзера? delete а не remove
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
        String s = "SELECT * FROM " + name_table; //todo почему в каждом методе отдна переменная называется оп разному?
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
//todo ты достаешь id юзера? или может юзера по id? getUserById?
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
//todo откуда вы взяли такое дибильное имя метода? может updateUsers?
    public void reUserName(String name_table, UUID uuid, User user) {           // изменяем по id
        User userDost = getUsersId(name_table, uuid);// а че за userDost? я не понимать
        try {
            userDost.setFirstName(user.getFirstName());
            userDost.setLastName(user.getLastName());
            removeUserName(name_table, uuid);//todo delete?
            addDatabase(userDost, name_table);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
//todo delete
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

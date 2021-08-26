import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        String nameTable = "users";
        DataBase dataBase = new DataBase();
        dataBase.dropUsersTable(nameTable);
        dataBase.createUsersTable(nameTable);
        UUID uuid = UUID.randomUUID();
        UUID uuid1 = UUID.randomUUID();
        User user1 = new User(uuid, "Yuzik", "Labeckiy", 33);
        User user2 = new User(uuid1, "Arkadiy", "Petyshinskiy", 31);
        User user3 = new User(UUID.randomUUID(), "Andrei", "Kynin", 32);
        User user4 = new User(UUID.randomUUID(), "Sasha", "Soroka", 41);

        dataBase.addDatabase(user1, nameTable);
        dataBase.addDatabase(user2, nameTable);
        dataBase.addDatabase(user3, nameTable);

//        dataBase.removeUserName(nameTable, uuid);

        List<User> list = dataBase.getAllUsers(nameTable);
        System.out.println(list);

        User user = dataBase.getUsersId(nameTable, uuid);
        System.out.println(user);

        dataBase.reUserName(nameTable, uuid1, user4);

        dataBase.removeUsers(nameTable);
    }
}

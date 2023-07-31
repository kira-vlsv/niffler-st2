package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.entity.UserEntity;

public interface NifflerUsersDAO {

    int createUser(UserEntity user);

    UserEntity getUser(String username);

    int updateUser(UserEntity user);

    int deleteUser(String username);

    String getUserId(String userName);
}

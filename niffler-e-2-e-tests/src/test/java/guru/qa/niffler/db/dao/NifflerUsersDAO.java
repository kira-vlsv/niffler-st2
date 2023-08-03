package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface NifflerUsersDAO {

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    int createUser(UserEntity user);

    UserEntity getUser(String username);

    int updateUser(UserEntity user);

    int deleteUser(UserEntity user);

    String getUserId(String userName);
}

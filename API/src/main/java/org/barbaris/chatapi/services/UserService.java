package org.barbaris.chatapi.services;

import org.barbaris.chatapi.models.IUserModel;
import org.barbaris.chatapi.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserModel {
    @Autowired
    private JdbcTemplate template;

    @Override
    public String regUser(UserModel user) {
        String login = user.getLogin();
        String password = user.getPassword();

        String sql = String.format("INSERT INTO chatusers (login, pass) VALUES ('%s', '%s');", login, password);
        try {
            template.execute(sql);
        } catch (Exception ex) {
            return "Error with putting data to database";
        }

        return "OK";
    }

}

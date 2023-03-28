package org.barbaris.chatapi.services;

import org.barbaris.chatapi.models.IUserModel;
import org.barbaris.chatapi.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserModel {
    @Autowired
    private JdbcTemplate template;

    @Override
    public String regUser(UserModel user) {
        String login = user.getLogin();
        String password = user.getPassword();

        String sql = String.format("SELECT * FROM chatusers WHERE login='%s';", login);
        try {
            List<Map<String, Object>> rows = template.queryForList(sql);

            if(rows.size() > 0) {
                return "Login is already taken";
            }

        } catch (Exception ex) {
            return "DB Error";
        }

        sql = String.format("INSERT INTO chatusers (login, pass) VALUES ('%s', '%s');", login, password);
        try {
            template.execute(sql);
        } catch (Exception ex) {
            return "Error with putting data to database";
        }

        return "OK";
    }

    @Override
    public String logUser(UserModel user) {
        String login = user.getLogin();
        String password = user.getPassword();

        String sql = String.format("SELECT * FROM chatusers WHERE login='%s' AND pass='%s';", login, password);

        try {
            List<Map<String, Object>> rows = template.queryForList(sql);

            if(rows.size() == 1) {
                return "OK";
            } else {
                return "Not found";
            }
        } catch (Exception ex) {
            return "DB Error";
        }
    }

}










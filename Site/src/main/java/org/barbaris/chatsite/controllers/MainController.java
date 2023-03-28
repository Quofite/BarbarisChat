package org.barbaris.chatsite.controllers;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.barbaris.chatsite.models.UserModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class MainController {
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        if(session.getAttribute("login") != null) {
            session.removeAttribute("login");
        }

        model.addAttribute("is_hidden", "true");
        model.addAttribute("error-details", "-");
        return "login";
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        if(session.getAttribute("login") != null) {
            session.removeAttribute("login");
        }

        model.addAttribute("is_hidden", "true");
        model.addAttribute("error-details", "-");
        return "register";
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        if(session.getAttribute("login") == null) {
            return "redirect:/login";
        }

        return "index";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String login, @RequestParam String pass, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        if(session.getAttribute("login") != null) {
            session.removeAttribute("login");
        }

        try {
            URL url = new URL("http://192.168.100.5:8888/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            Gson gson = new Gson();
            UserModel user = new UserModel();
            if(DataVisorController.isDataValid(login, pass)) {
                user.setLogin(login);
                user.setPassword(pass);
            } else {
                model.addAttribute("is_hidden", "false");
                model.addAttribute("error_details", "Bad data request");
                return "login";
            }

            OutputStreamWriter sw = new OutputStreamWriter(connection.getOutputStream());
            sw.write(gson.toJson(user));
            sw.flush();
            sw.close();

            int response = connection.getResponseCode();
            String message;

            if(response == 200) {
                session.setAttribute("login", login);
                return "redirect:/";
            } else if(response == 404) {
                message = "Wrong login or password";
            } else if(response == 400) {
                message = "Bad request";
            } else {
                message = "Internal server error";
            }

            model.addAttribute("is_hidden", "false");
            model.addAttribute("error_details", message);
            return "login";

        } catch (Exception ex) {
            model.addAttribute("is_hidden", "false");
            model.addAttribute("error_details", "Couldn't create connection with API");
            return "login";
        }
    }

    @PostMapping("/register")
    public String registerPost(@RequestParam String login, @RequestParam String pass, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        if(session.getAttribute("login") != null) {
            session.removeAttribute("login");
        }

        try {
            URL url = new URL("http://192.168.100.5:8888/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            Gson gson = new Gson();
            UserModel user = new UserModel();

            if(DataVisorController.isDataValid(login, pass)) {
                user.setLogin(login);
                user.setPassword(pass);
            } else {
                model.addAttribute("is_hidden", "false");
                model.addAttribute("error_details", "Bad data request");
                return "register";
            }

            OutputStreamWriter sw = new OutputStreamWriter(connection.getOutputStream());
            sw.write(gson.toJson(user));
            sw.flush();
            sw.close();

            int response = connection.getResponseCode();
            String message;

            if(response == 201) {
                model.addAttribute("is_hidden", "true");
                model.addAttribute("error_details", "-");
                return "login";
            } else if(response == 403) {
                message = "Login is already taken";
            } else if(response == 400) {
                message = "Bad request";
            } else {
                message = "Internal server error";
            }

            model.addAttribute("is_hidden", "false");
            model.addAttribute("error_details", message);
            return "register";

        } catch (Exception ex) {
            model.addAttribute("is_hidden", "false");
            model.addAttribute("error_details", "Couldn't connect to API");
            return "register";
        }
    }
}




















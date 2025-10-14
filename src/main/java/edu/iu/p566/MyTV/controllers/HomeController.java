
package edu.iu.p566.MyTV.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homepage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
            @RequestParam String password,
            Model model) {

        if ("user".equals(username) && "pass".equals(password)) {
            return "redirect:/vid";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}

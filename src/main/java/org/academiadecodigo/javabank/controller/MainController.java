package org.academiadecodigo.javabank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for rendering the initial page of the application
 */
@Controller
public class MainController {

    /**
     * Renders the home page view
     *
     * @return the view
     */
    @RequestMapping("/")
    public String home() {
        return "redirect:/customer/list";
    }
}

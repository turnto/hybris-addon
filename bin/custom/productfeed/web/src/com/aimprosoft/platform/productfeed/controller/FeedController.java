package com.aimprosoft.platform.productfeed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FeedController {

    @RequestMapping(value = "/")
    public String main() {
        return "main";
    }

    @RequestMapping(value = "/api")
    public String getProducts(final Model model) {
        model.addAttribute("products", "products");
        return "product";
    }

}

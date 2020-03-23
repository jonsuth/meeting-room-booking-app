package com.johnlewis.bookingapp.home

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView


@Controller
class HomeController {
    @RequestMapping("/")
    fun home(model: Model): RedirectView {
        return RedirectView("/dashboard")
    }
}
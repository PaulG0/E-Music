package sio.Javanaise.emusic.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({ "/cours", "/cours/" })
public class CoursController {

    @GetMapping("{id}")
    public String indexCoursAction() {
        return "cours/index";
    }
}

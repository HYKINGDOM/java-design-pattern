package org.fkjava.gift.controller;

import org.fkjava.gift.domain.Tally;
import org.fkjava.gift.service.PresentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/tally")
class TallyController {
    @Autowired
    private PresentService presentService;
    @GetMapping
    @ResponseBody
    public List<Tally> getAllTally(){
        return presentService.findAllTags();
    }
}

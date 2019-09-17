package org.fkjava.gift.controller;

import org.fkjava.commons.converter.StringToDateTimePropertyEditor;
import org.fkjava.commons.domain.Result;
import org.fkjava.gift.domain.Present;
import org.fkjava.gift.service.PresentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/present")
public class PresentController {
    @Autowired
    private PresentService presentService;
    @GetMapping
    public Page<Present> find(
            @RequestParam(name = "pn", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "kw", required = false) String keyword,
            @RequestParam(name = "tallyId", required =false) List<String> tallyId
            ) {
        return presentService.findPresent(pageNumber,keyword,tallyId);
    }
    @PostMapping
    public Result save(@RequestBody Present present){
            return this.presentService.savePresent(present);
    }
    @GetMapping("{id}")
    public Present findById(
            @PathVariable("id") String id
    ) {
        return presentService.findPresentById(id);
    }

    // 注册控制器全局的转换器，所有发送给当前控制器的数据，都会使用这里注册的转换器。
    @InitBinder
    public void bindDate(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new StringToDateTimePropertyEditor());
    }

    @PostMapping("{id}")
    public Result save(@PathVariable("id") String id, @RequestBody Present present) {
        present.setId(id);
        return this.presentService.savePresent(present);
    }
//    @GetMapping("{id}")
//    public Result delete(@PathVariable String id){
//        return this.presentService.delere(id);
//    }

}

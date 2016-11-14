package com.prod.custSuptMaven.site;
//Class notes- for Spring MVC this class replaces index.jsp, see chap 13, pg 385

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class IndexController
{
    @RequestMapping("/")
    public View index()
    {
        return new RedirectView("/ticket/list", true, false);
    }
}

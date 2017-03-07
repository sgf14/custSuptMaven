package com.prod.custSuptMaven.site;
//part of MVC-CSR- this Controller(C in CSR) replaced SessionListServlet typical of Spring Controllers replacing servlet classes
//see chap 13, pg 385

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.prod.custSuptMaven.config.annotation.WebController;

import javax.inject.Inject;

import java.util.Map;

@WebController
@RequestMapping("session")
public class SessionListController
{
    @Inject SessionRegistry sessionRegistry;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Map<String, Object> model)
    {
        model.put("timestamp", System.currentTimeMillis());
        model.put("numberOfSessions", this.sessionRegistry.getNumberOfSessions());
        model.put("sessionList", this.sessionRegistry.getAllSessions());

        return "session/list";
    }
}

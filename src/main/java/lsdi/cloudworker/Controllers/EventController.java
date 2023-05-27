package lsdi.cloudworker.Controllers;

import lsdi.cloudworker.Services.EsperService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EventController {
    EsperService esperService = EsperService.getInstance();

    @PostMapping("/event/{eventType}")
    public void event(@RequestBody Map<String, Object> event, @PathVariable String eventType) {
        //TODO receive event from cloudcollector
        esperService.sendEvent(event, eventType);
    }
}

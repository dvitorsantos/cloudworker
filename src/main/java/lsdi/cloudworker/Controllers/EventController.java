package lsdi.cloudworker.Controllers;

import lsdi.cloudworker.Services.EsperService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EventController {
    EsperService esperService = EsperService.getInstance();

    @PostMapping("/event")
    public void event(@RequestBody Map<String, Object> event) {
        //TODO receive event from cloudcollector
        String eventType = event.keySet().stream().findFirst().get().toString();
        esperService.sendEvent(event, eventType);
    }
}

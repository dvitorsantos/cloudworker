package lsdi.cloudworker.Listeners;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lsdi.cloudworker.DataTransferObjects.Event;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class EventListener implements UpdateListener {
    RestTemplate restTemplate = new RestTemplate();
    private final String ruleUuid;
    private final String webhookUrl;

    private final String consumerUrl = System.getenv("CLOUDCONSUMER_URL");

    public EventListener(String ruleUuid, String webhookUrl) {
        this.ruleUuid = ruleUuid;
        this.webhookUrl = webhookUrl;
    }

    @Override
    public void update(EventBean[] newData, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> event = mapper.readValue(mapper.writeValueAsString(newData[0].getUnderlying()), Map.class);
            Event eventDto = new Event();
            eventDto.setWebhookUrl(webhookUrl);
            eventDto.setEvent(event);
            //TODO enviroment variable cloudconsumer_url
            restTemplate.postForObject(consumerUrl + "/event", eventDto, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
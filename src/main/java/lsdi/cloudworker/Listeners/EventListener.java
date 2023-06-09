package lsdi.cloudworker.Listeners;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lsdi.cloudworker.DataTransferObjects.Event;
import lsdi.cloudworker.DataTransferObjects.RuleRequestResponse;
import lsdi.cloudworker.Services.MqttService;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class EventListener implements UpdateListener {
    RestTemplate restTemplate = new RestTemplate();
    private final RuleRequestResponse rule;

    private final String consumerUrl = System.getenv("CLOUDCONSUMER_URL");

    private MqttService mosquittoService = MqttService.getInstance();

    public EventListener(RuleRequestResponse rule) {
        this.rule = rule;
    }

    @Override
    public void update(EventBean[] newData, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> event = mapper.readValue(mapper.writeValueAsString(newData[0].getUnderlying()), Map.class);

            switch (rule.getTarget()) {
                case "CLOUD" -> mosquittoService.publish("cdpo/CLOUD/event/" + rule.getOutputEventType(), mapper.writeValueAsBytes(event));
                case "WEBHOOK" -> {
                    Event eventDto = new Event();
                    eventDto.setWebhookUrl(rule.getWebhookUrl());
                    eventDto.setEvent(event);
                    //TODO enviroment variable cloudconsumer_url
                    restTemplate.postForObject(consumerUrl + "/event", eventDto, Map.class);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
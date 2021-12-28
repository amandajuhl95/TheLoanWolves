package dk.lw.loanwolvesservice.application;

import com.google.gson.Gson;
import dk.lw.loanwolvesservice.DTO.log.LogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoggingProducer {

    private final Gson gson = new Gson();
    private static final String TOPIC = "logging";

    @Autowired
    private KafkaTemplate<String, String> requestTemplate;

    public void sendLogs(String serviceName, String errorMessage, int errorCode)
    {
        LogDTO logDTO = new LogDTO(serviceName, errorMessage, errorCode);
        requestTemplate.send(TOPIC, gson.toJson(logDTO));
        requestTemplate.flush();
    }
}

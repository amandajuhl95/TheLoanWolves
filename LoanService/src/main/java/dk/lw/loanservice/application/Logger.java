package dk.lw.loanservice.application;

import com.google.gson.Gson;
import dk.lw.loanservice.DTO.LogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Logger {

    private static final Gson gson = new Gson();
    private static final String TOPIC = "logging";

    @Autowired
    private static KafkaTemplate<String, String> requestTemplate;

    public static void sendLogs(String serviceName, String errorMessage, int errorCode)
    {
        LogDTO logDTO = new LogDTO(serviceName, errorMessage, errorCode);
        requestTemplate.send(TOPIC, gson.toJson(logDTO));
        requestTemplate.flush();
    }
}

package dk.lw.loggingservice.application;

import com.google.gson.Gson;
import dk.lw.loggingservice.DTO.LogDTO;
import dk.lw.loggingservice.domain.Log;
import dk.lw.loggingservice.infrastructure.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private final Gson gson = new Gson();

    @Autowired
    private LogRepository logRepository;

    @KafkaListener(topics = "logging")
    public void logging(String request)
    {
        LogDTO logDTO = gson.fromJson(request, LogDTO.class);
        Log log = new Log(logDTO);
        logRepository.save(log);
    }

}

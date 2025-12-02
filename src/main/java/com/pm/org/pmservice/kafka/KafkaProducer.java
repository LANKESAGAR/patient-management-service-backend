package com.pm.org.pmservice.kafka;

import com.pm.org.pmservice.grpc.BillingServiceGrpcClient;
import com.pm.org.pmservice.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED ")
                .build();
        try {
            kafkaTemplate.send("patient-events", event.toByteArray());
        } catch(Exception e){
            logger.error("Error sending PatientCreated even: {}", event);
        }
    }
}
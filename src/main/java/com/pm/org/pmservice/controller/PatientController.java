package com.pm.org.pmservice.controller;

import com.pm.org.pmservice.dto.PatientRequestDTO;
import com.pm.org.pmservice.dto.PatientResponseDTO;
import com.pm.org.pmservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patientsList =  patientService.getPatients();
        return ResponseEntity.ok().body(patientsList);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponse = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponse);
    }
}

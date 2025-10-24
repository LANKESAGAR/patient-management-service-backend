package com.pm.org.pmservice.service;

import com.pm.org.pmservice.dto.PatientRequestDTO;
import com.pm.org.pmservice.dto.PatientResponseDTO;
import com.pm.org.pmservice.mapper.PatientMapper;
import com.pm.org.pmservice.model.Patient;
import com.pm.org.pmservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOList = patients.stream()
                .map(PatientMapper::toDto).collect(Collectors.toList());
        return patientResponseDTOList;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        Patient newPatient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));
        return PatientMapper.toDto(newPatient);
    }
}

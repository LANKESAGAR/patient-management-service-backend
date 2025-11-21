package com.pm.org.pmservice.service;

import com.pm.org.pmservice.dto.PatientRequestDTO;
import com.pm.org.pmservice.dto.PatientResponseDTO;
import com.pm.org.pmservice.grpc.BillingServiceGrpcClient;
import com.pm.org.pmservice.mapper.PatientMapper;
import com.pm.org.pmservice.model.Patient;
import com.pm.org.pmservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pm.org.pmservice.exception.EmailAlreadyExistsException;
import com.pm.org.pmservice.exception.PatientNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BillingServiceGrpcClient billingServiceGrpcClient;

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOList = patients.stream()
                .map(PatientMapper::toDto).collect(Collectors.toList());
        return patientResponseDTOList;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw  new EmailAlreadyExistsException("A patient with this email "
            + "already exists " + patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),
                newPatient.getName(), newPatient.getEmail());
        return PatientMapper.toDto(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw  new EmailAlreadyExistsException("A patient with this email "
                    + "already exists" + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDto(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}

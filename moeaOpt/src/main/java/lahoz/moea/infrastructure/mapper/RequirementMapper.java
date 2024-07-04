package lahoz.moea.infrastructure.mapper;

import lahoz.moea.entities.RequirementEntity;
import org.springframework.stereotype.Component;

import lahoz.moea.infrastructure.dto.RequirementDTO;

@Component
public class RequirementMapper {

    public RequirementDTO toDTO(RequirementEntity requirement) {
        RequirementDTO dto = new RequirementDTO();
        dto.setId(requirement.getId());
        dto.setId_stop_departure(requirement.getId_stop_departure());
        dto.setId_stop_arrival(requirement.getId_stop_arrival());
        dto.setCategory(requirement.getCategory());
        dto.setPickup_time(requirement.getPickup_time());
        dto.setLoading(requirement.getLoading());
        return dto;
    }


    public RequirementEntity toEntity(RequirementDTO dto) {
        RequirementEntity requirement = new RequirementEntity();
        requirement.setId(dto.getId());
        requirement.setId_stop_departure(dto.getId_stop_departure());
        requirement.setId_stop_arrival(dto.getId_stop_arrival());
        requirement.setCategory(dto.getCategory());
        requirement.setPickup_time(dto.getPickup_time());
        requirement.setLoading(dto.getLoading());
        return requirement;
    }
}
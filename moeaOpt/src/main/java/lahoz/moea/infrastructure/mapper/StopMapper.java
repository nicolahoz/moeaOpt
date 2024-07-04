package lahoz.moea.infrastructure.mapper;

import lahoz.moea.entities.StopEntity;
import org.springframework.stereotype.Component;

import lahoz.moea.infrastructure.dto.StopDTO;

@Component
public class StopMapper {

    public StopDTO toDTO(StopEntity stop) {
        StopDTO dto = new StopDTO();
        dto.setId(stop.getId());
        dto.setName(stop.getName());
        dto.setLatitud(stop.getLatitud());
        dto.setLongitud(stop.getLongitud());
        return dto;
    }


    public StopEntity toEntity(StopDTO dto) {
        StopEntity stop = new StopEntity();
        stop.setId(dto.getId());
        stop.setName(dto.getName());
        stop.setLatitud(dto.getLatitud());
        stop.setLongitud(dto.getLongitud());
        return stop;
    }
}
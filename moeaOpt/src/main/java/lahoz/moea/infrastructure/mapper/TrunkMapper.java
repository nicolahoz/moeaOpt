package lahoz.moea.infrastructure.mapper;

import lahoz.moea.entities.TrunkEntity;
import org.springframework.stereotype.Component;

import lahoz.moea.infrastructure.dto.TrunkDTO;

@Component
public class TrunkMapper {
    public TrunkDTO toDTO(TrunkEntity trunk) {
        TrunkDTO dto = new TrunkDTO();
        dto.setId(trunk.getId());
        dto.setCapacity(trunk.getCapacity());
        dto.setId_stop_parking(trunk.getId_stop_parking());
		dto.setCategory(trunk.getCategory());
        return dto;
    }

    public TrunkEntity toEntity(TrunkDTO dto) {
        TrunkEntity trunk = new TrunkEntity();
        trunk.setId(dto.getId());
        trunk.setCapacity(dto.getCapacity());
        trunk.setId_stop_parking(dto.getId_stop_parking());
		trunk.setCategory(dto.getCategory());

        return trunk;
    }
}
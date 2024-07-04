package lahoz.moea.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lahoz.moea.infrastructure.dto.FrameDTO;
import lahoz.moea.infrastructure.mapper.FramesMapper;
import lahoz.moea.repositories.FramesRepository;


@Service
public class FramesService {
	protected final FramesRepository repository;
	protected final FramesMapper mapper;

	public FramesService(FramesRepository repository, FramesMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	public Page<FrameDTO> get(String orderField, String orderCriterial, Integer pageNumber, Integer pageSize) {
		Pageable page;

		if (orderCriterial.equalsIgnoreCase("desc")) {
			page = PageRequest.of(pageNumber, pageSize, Sort.by(orderField).descending());
		}else{
			page = PageRequest.of(pageNumber, pageSize, Sort.by(orderField).ascending());
		}

		return repository.findAll(page).map(this.mapper::toDTO);
	}
}
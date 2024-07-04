package lahoz.moea.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lahoz.moea.infrastructure.dto.TrunkDTO;
import lahoz.moea.infrastructure.mapper.TrunkMapper;
import lahoz.moea.repositories.TrunkRepository;


@Service
public class TrunkService {
	protected final TrunkRepository repository;
	protected final TrunkMapper mapper;

	public TrunkService(TrunkRepository repository, TrunkMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	public Page<TrunkDTO> get(String orderField, String orderCriterial, Integer pageNumber, Integer pageSize) {
		Pageable page;

		if (orderCriterial.equalsIgnoreCase("desc")) {
			page = PageRequest.of(pageNumber, pageSize, Sort.by(orderField).descending());
		}else{
			page = PageRequest.of(pageNumber, pageSize, Sort.by(orderField).ascending());
		}

		return repository.findAll(page).map(this.mapper::toDTO);
	}
}
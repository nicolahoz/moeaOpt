package lahoz.moea.repositories;

import lahoz.moea.entities.FramesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FramesRepository extends PagingAndSortingRepository<FramesEntity, Long>, CrudRepository<FramesEntity, Long> {

}

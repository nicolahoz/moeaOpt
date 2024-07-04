package lahoz.moea.repositories;

import lahoz.moea.entities.StopEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends PagingAndSortingRepository<StopEntity, Long>, CrudRepository<StopEntity, Long> {

}

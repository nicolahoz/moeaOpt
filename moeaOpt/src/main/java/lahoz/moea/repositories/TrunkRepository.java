package lahoz.moea.repositories;

import lahoz.moea.entities.TrunkEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrunkRepository extends PagingAndSortingRepository<TrunkEntity, Long>, CrudRepository<TrunkEntity, Long> {

}

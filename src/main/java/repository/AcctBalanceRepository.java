package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import entity.AcctBalance;

@Repository
public interface AcctBalanceRepository extends CrudRepository<AcctBalance, Long>  , PagingAndSortingRepository<AcctBalance, Long>{

}

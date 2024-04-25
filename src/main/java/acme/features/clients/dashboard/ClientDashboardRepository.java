
package acme.features.clients.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.roles.Client;

@Repository
public interface ClientDashboardRepository extends AbstractRepository {

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 25 and pl.contract.draftmode = false")
	int findTotalLogLessThan25(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 50 and 25 <= pl.completeness and pl.contract.draftmode = false")
	int findTotalLogLessBetween25And50(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 75 and 50 <= pl.completeness and pl.contract.draftmode = false")
	int findTotalLogLessBetween50And75(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and 75 <= pl.completeness and pl.contract.draftmode = false")
	int findTotalLogAbove75(Client client);

	@Query("select avg(c.budget.amount) from Contract c where c.client = :client and c.draftmode = false")
	Double findAverageBudgetOfContracts(Client client);

	@Query("select stddev(c.budget.amount) from Contract c where c.client = :client and c.draftmode = false")
	Double findDeviationBudgetOfContracts(Client client);

	@Query("select min(c.budget.amount) from Contract c where c.client = :client and c.draftmode = false")
	Double findMinimunBudgetOfContracts(Client client);

	@Query("select max(c.budget.amount) from Contract c where c.client = :client and c.draftmode = false")
	Double findMaximumBudgetOfContracts(Client client);

	@Query("select c from Client c where c.userAccount.id = :id")
	Client findOneClientByUserAccountId(int id);

}

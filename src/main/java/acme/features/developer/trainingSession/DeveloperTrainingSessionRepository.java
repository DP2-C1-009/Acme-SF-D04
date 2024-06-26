
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;

@Repository
public interface DeveloperTrainingSessionRepository extends AbstractRepository {

	@Query("select tm from TrainingModule tm where tm.id = :id")
	TrainingModule findOneTrainingModuleById(int id);

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :id")
	Collection<TrainingSession> findManyTrainingSessionsByTrainingModuleId(int id);

	@Query("select ts from TrainingSession ts where ts.id = :id")
	TrainingSession findOneTrainingSessionById(int id);

	@Query("select ts.code from TrainingSession ts")
	Collection<String> findManyTrainingSessionCodes();

	@Query("select tm.draftMode from TrainingModule tm where tm.id = :id")
	boolean findTrainingModuleDraftModeById(int id);

	@Query("select ts.trainingModule from TrainingSession ts where ts.id = :tsId")
	TrainingModule findOneTrainingModuleByTrainingSessionId(int tsId);

}

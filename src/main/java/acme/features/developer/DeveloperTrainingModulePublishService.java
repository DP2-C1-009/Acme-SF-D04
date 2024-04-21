
package acme.features.developer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.training.DifficultyLevel;
import acme.entities.training.TrainingModule;
import acme.entities.training.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModulePublishService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		TrainingModule object;
		Principal principal;
		int tmId;

		tmId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingModuleById(tmId);

		principal = super.getRequest().getPrincipal();

		status = object != null && object.isDraftMode() && object.getDeveloper().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "estimatedTotalTime");
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		boolean isCodeChanged = false;
		final Collection<String> allTMCodes = this.repository.findManyTrainingModuleCodes();
		final TrainingModule tm = this.repository.findOneTrainingModuleById(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			isCodeChanged = !tm.getCode().equals(object.getCode());
			super.state(!isCodeChanged || !allTMCodes.contains(object.getCode()), "code", "developer.training-module.error.codeDuplicate");
		}

		if (object.getUpdateMoment() != null && !super.getBuffer().getErrors().hasErrors("updateMoment"))
			super.state(MomentHelper.isAfter(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "developer.training-module.error.update-date-before");

		Collection<TrainingSession> sessions = this.repository.findManyTrainingSessionsByTrainingModuleId(object.getId());
		super.state(sessions.size() >= 1, "*", "developer.training-module.error.not-enough-training-sessions");
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		Project objectProject = object.getProject();

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "estimatedTotalTime", "draftMode");
		dataset.put("projectCode", objectProject.getCode());
		dataset.put("difficultyLevel", choices.getSelected().getKey());
		dataset.put("difficultyLevels", choices);

		super.getResponse().addData(dataset);
	}

}

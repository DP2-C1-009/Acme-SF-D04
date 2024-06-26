
package acme.features.developer.trainingModule;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;

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
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleCreateService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;

		Principal principal = super.getRequest().getPrincipal();

		if (principal.hasRole(Developer.class))
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		Developer dev;
		Date moment;

		dev = this.repository.findOneDeveloperById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new TrainingModule();
		object.setDeveloper(dev);
		object.setDraftMode(true);

		moment = MomentHelper.getCurrentMoment();
		object.setCreationMoment(moment);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		super.bind(object, "code", "details", "difficultyLevel", "optionalLink", "estimatedTotalTime");
		object.setProject(project);
	}

	@Override
	public void validate(final TrainingModule object) {
		Project project = object.getProject();

		final Collection<String> allTMCodes = this.repository.findManyTrainingModuleCodes();

		if (project == null)
			super.state(false, "project", "developer.training-module.error.project");

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!allTMCodes.contains(object.getCode()), "code", "developer.training-module.error.codeDuplicate");

		if (object.getUpdateMoment() != null && !super.getBuffer().getErrors().hasErrors("updateMoment"))
			super.state(MomentHelper.isAfterOrEqual(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "developer.training-module.error.update-date-before");
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		Date moment;
		moment = MomentHelper.getCurrentMoment();
		object.setCreationMoment(moment);

		object.setDraftMode(true);

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choicesProjects;
		SelectChoices choicesDifficultyLevels;

		projects = this.repository.findManyPublishedProjects();
		choicesProjects = SelectChoices.from(projects, "code", object.getProject());
		choicesDifficultyLevels = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "estimatedTotalTime", "draftMode");
		dataset.put("project", choicesProjects.getSelected().getKey());
		dataset.put("projects", choicesProjects);
		dataset.put("difficultyLevel", choicesDifficultyLevels.getSelected().getKey());
		dataset.put("difficultyLevels", choicesDifficultyLevels);

		if (object.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);
	}

}


package acme.features.manager.project;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectListMineService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerProjectRepository repository;

	// AbstractService interface ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Project> objects;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		objects = this.repository.findAllProjectsByManagerId(managerId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "code", "title", "pAbstract", "fatalErrors", "cost", "optionalLink");

		if (object.isDraftMode()) {

			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");

		} else
			dataset.put("draftMode", "No");

		super.getResponse().addData(dataset);
	}

}

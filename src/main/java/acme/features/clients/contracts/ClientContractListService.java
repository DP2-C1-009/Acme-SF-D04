
package acme.features.clients.contracts;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contract.Contract;
import acme.roles.Client;

@Service
public class ClientContractListService extends AbstractService<Client, Contract> {

	@Autowired
	private ClientContractRepository repository;


	@Override
	public void authorise() {
		boolean status = false;

		Principal principal = super.getRequest().getPrincipal();

		if (principal.hasRole(Client.class))
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Contract> objects;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		objects = this.repository.findContractByClientId(principal.getActiveRoleId());

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "code", "providerName", "draftmode");

		if (object.isDraftmode()) {
			final Locale local = super.getRequest().getLocale();
			String draftmodeText;
			if (local.equals(Locale.ENGLISH))
				draftmodeText = "Yes";
			else
				draftmodeText = "Sí";
			dataset.put("draftmode", draftmodeText);
		} else
			dataset.put("draftmode", "No");

		super.getResponse().addData(dataset);
	}

}

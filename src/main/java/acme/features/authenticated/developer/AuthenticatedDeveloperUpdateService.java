
package acme.features.authenticated.developer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.roles.Developer;

@Service
public class AuthenticatedDeveloperUpdateService extends AbstractService<Authenticated, Developer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedDeveloperRepository repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Developer object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		object = this.repository.findOneDeveloperByUserAccountId(userAccountId);

		userAccount = this.repository.findOneUserAccountById(userAccountId);
		object.setEmail(userAccount.getIdentity().getEmail());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Developer object) {
		assert object != null;

		super.bind(object, "degree", "specialisation", "skills", "optionalLink");
	}

	@Override
	public void validate(final Developer object) {
		assert object != null;
	}

	@Override
	public void perform(final Developer object) {
		assert object != null;

		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object.setEmail(userAccount.getIdentity().getEmail());

		this.repository.save(object);
	}

	@Override
	public void unbind(final Developer object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "degree", "specialisation", "skills", "email", "optionalLink");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}


package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeAudits.AuditRecord;
import acme.entities.codeAudits.CodeAudit;
import acme.entities.projects.Project;
import acme.roles.Auditor;

@Repository
public interface AuditorCodeAuditRepository extends AbstractRepository {

	@Query("Select ca from CodeAudit ca Where ca.auditor.id = :id ")
	Collection<CodeAudit> findCodeAuditByAuditor(int id);

	@Query("Select ca from CodeAudit ca Where ca.id = :id")
	CodeAudit findCodeAuditById(int id);

	@Query("Select ca.code from CodeAudit ca")
	Collection<String> findAllCodes();

	@Query("Select a from Auditor a Where a.id = :id")
	Auditor findAuditorById(int id);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findProjectsDraftModeFalse();

	@Query("Select p from Project p Where p.id = :pId")
	Project findProjectById(int pId);

	@Query("Select ar from AuditRecord ar Where ar.codeAudit.id = :id")
	Collection<AuditRecord> findAuditRecordsByCodeAudit(int id);

}

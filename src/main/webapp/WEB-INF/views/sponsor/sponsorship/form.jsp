<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="sponsor.sponsorship.form.label.code" path="code"/>
	
	<jstl:if test="${acme:anyOf(_command, 'show|update|publish|delete')}">
		<acme:input-moment code="sponsor.sponsorship.form.label.moment" path="moment" readonly="true"/>
	</jstl:if>
	
	<acme:input-moment code="sponsor.sponsorship.form.label.start" path="start"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.end" path="end"/>
	<acme:input-money code="sponsor.sponsorship.form.label.amount" path="amount"/>
	<acme:input-email code="sponsor.sponsorship.form.label.email" path="email"/>
	<acme:input-textbox code="sponsor.sponsorship.form.label.further-info" path="furtherInfo"/>
	<acme:input-select code="sponsor.sponsorship.form.label.type" path="type" choices="${types}"/>
	
	<jstl:choose>
	
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:input-select code="sponsor.sponsorship.form.label.projects" path="project" choices="${projects}"/>
			<acme:input-checkbox code="sponsor.sponsorship.form.label.draft-mode" path="draftMode" readonly="true"/>
			
			<acme:submit code="sponsor.sponsorship.form.button.delete" action="/sponsor/sponsorship/delete"/>
			<acme:submit code="sponsor.sponsorship.form.button.update" action="/sponsor/sponsorship/update"/>
			<acme:submit code="sponsor.sponsorship.form.button.publish" action="/sponsor/sponsorship/publish"/>
			
			<acme:button code="sponsor.sponsorship.form.button.invoices" action="/sponsor/invoice/list?sponsorshipId=${id}"/>

		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false}">
			<acme:input-select code="sponsor.sponsorship.form.label.projects" path="project" choices="${projects}" readonly="true"/>
			
			<acme:input-checkbox code="sponsor.sponsorship.form.label.draft-mode" path="draftMode" readonly="true"/>
			
			<acme:button code="sponsor.sponsorship.form.button.invoices" action="/sponsor/invoice/list?sponsorshipId=${id}"/>

		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="sponsor.sponsorship.form.label.projects" path="project" choices="${projects}"/>
			
			<acme:submit code="sponsor.sponsorship.form.button.create" action="/sponsor/sponsorship/create"/>
		</jstl:when>
		
	</jstl:choose>
	
</acme:form>


import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager

def userManager = ComponentAccessor.getUserManager()
def projectRoleManager = ComponentAccessor.getComponentOfType(ProjectRoleManager)
def role = projectRoleManager.getProjectRole("Integration list")
def reporterField = getFieldById("reporter")
def assigneeField = getFieldById("assignee")
def reporter = userManager.getUserByName(reporterField.getValue().toString())
def assignee = userManager.getUserByName(assigneeField.getValue().toString())

// Restrict reporter
if(!projectRoleManager.isUserInProjectRole(reporter, role, issueContext.projectObject) && (reporter != null)) {
    reporterField.setError("Автором может быть только пользователь из согласованного интеграционного списка")
	reporterField.setFormValue(null)
} else {
    reporterField.clearError()
}

// Restrict assignee
if (!projectRoleManager.isUserInProjectRole(assignee, role, issueContext.projectObject) && (assignee != null)) {
    assigneeField.setError("Исполнителем может быть только пользователь из согласованного интеграционного списка")
	assigneeField.setFormValue(null)
} else {
    assigneeField.clearError()
}

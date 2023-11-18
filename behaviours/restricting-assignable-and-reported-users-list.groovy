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
    reporterField.setError("Reporter can only be a user from the agreed integration list")
	reporterField.setFormValue(null)
} else {
    reporterField.clearError()
}

// Restrict assignee
if (!projectRoleManager.isUserInProjectRole(assignee, role, issueContext.projectObject) && (assignee != null)) {
    assigneeField.setError("Assignee can only be a user from the agreed integration list")
	assigneeField.setFormValue(null)
} else {
    assigneeField.clearError()
}

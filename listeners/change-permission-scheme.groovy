import com.atlassian.jira.component.ComponentAccessor

// get scheme to which you need to change the current project scheme
def permissionSchemeManager = ComponentAccessor.getPermissionSchemeManager()
def scheme = permissionSchemeManager.getSchemeObject('Scheme name')
def project = issue.getProjectObject()

// changing the scheme in the project
permissionSchemeManager.removeSchemesFromProject(project)
permissionSchemeManager.addSchemeToProject(project, scheme)

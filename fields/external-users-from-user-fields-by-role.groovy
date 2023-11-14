import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager

ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def customerParticipants = customFieldManager.getCustomFieldObject(11111L)
def partnerParticipants = customFieldManager.getCustomFieldObject(22222L)
def roleCustomer = projectRoleManager.getProjectRole(33333L)
def rolePartner = projectRoleManager.getProjectRole(44444L)
def reporter = issue.getReporter()
def assignee = issue.getAssignee()
def customerList = issue.getCustomFieldValue(customerParticipants) as List
def partnerList = issue.getCustomFieldValue(partnerParticipants) as List
List users = []

if( customerList ) {
    users = customerList
} else if( partnerList ) {
    users = partnerList
}

if( projectRoleManager.isUserInProjectRole(reporter, roleCustomer, issue.getProjectObject()) || projectRoleManager.isUserInProjectRole(reporter, rolePartner, issue.getProjectObject()) ) {
    users.add(reporter)
} else if( projectRoleManager.isUserInProjectRole(assignee, rolePartner, issue.getProjectObject()) ) {
    users.add(assignee)
}
users.unique()

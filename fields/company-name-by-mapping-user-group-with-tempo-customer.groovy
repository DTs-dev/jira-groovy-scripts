import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.security.roles.ProjectRoleManager
import com.onresolve.scriptrunner.runner.customisers.PluginModule
import com.onresolve.scriptrunner.runner.customisers.WithPlugin
import com.tempoplugin.accounts.account.api.AccountManager

ProjectRoleManager projectRoleManager = ComponentAccessor.getComponent(ProjectRoleManager)
def groupManager = ComponentAccessor.groupManager
def customFieldManager = ComponentAccessor.getCustomFieldManager()

def customerParticipants = customFieldManager.getCustomFieldObject(11111L)
def role = projectRoleManager.getProjectRole(22222L) // Customer role
def groupPrefix = 'Customers - '
def excludedWords = [ '24 hours', 'worktime' ]
def excluded = excludedWords.collect { groupPrefix + it }

def users = issue.getCustomFieldValue(customerParticipants) as List
ApplicationUser user

if( users ) {
    user = users[0]
} else if( projectRoleManager.isUserInProjectRole(issue.reporter, role, issue.getProjectObject()) ) {
    user = issue.reporter
} else {
    return
}

@WithPlugin("com.tempoplugin.tempo-accounts")
@PluginModule
AccountManager customers
def customer
def customerName

groupManager.getGroupNamesForUser(user).find {
    if( it.contains(groupPrefix) && !(it in excluded) ) {
        customer = it
        customers.getViewableCustomersForLoggedInUser().find {
            if( it.getKey() == customer.minus(groupPrefix) ) {
                customerName = it.getName()
                return
            }
        }
    }
}
customerName

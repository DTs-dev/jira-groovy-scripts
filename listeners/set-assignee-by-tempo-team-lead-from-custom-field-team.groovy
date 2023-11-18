import com.atlassian.jira.component.ComponentAccessor
import com.onresolve.scriptrunner.runner.customisers.PluginModule
import com.onresolve.scriptrunner.runner.customisers.WithPlugin
import com.tempoplugin.team.api.TeamManager

def issueService = ComponentAccessor.getIssueService()
def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def tempoTeamCustomField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(11111L)

/* Сomment out line below if it's not a listener (e.g. post-function) */
def issue = event.issue

@WithPlugin("com.tempoplugin.tempo-teams")
@PluginModule
TeamManager teamManager

/* Get tempo team */
def teamId = issue.getCustomFieldValue(tempoTeamCustomField).getId()
def team = teamManager.getTeam(teamId)

/* Get username of team lead */
def teamLead = team?.tempoLead.getKey()

/* Uncomment line below and comment out everything else if it's not a listener (e.g. post-function) */
//issue.setAssigneeId(teamLead)

def issueInputParameters = issueService.newIssueInputParameters()
issueInputParameters.setAssigneeId(teamLead)

def updateValidationResult = issueService.validateUpdate(user, issue.getId(), issueInputParameters)

if( updateValidationResult.isValid() ) {
    if( !issueService.update(user, updateValidationResult).isValid() ) {
        log.warn('Update validation for "' + issue.getKey() + '" is not valid')
    }
} else {
    log.warn('Update result for "' + issue.getKey() + '" is not valid')
}

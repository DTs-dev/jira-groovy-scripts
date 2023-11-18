import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.IssueInputParametersImpl

def issueLinkManager = ComponentAccessor.getIssueLinkManager()
def constantsManager = ComponentAccessor.getConstantsManager()
def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def issueService = ComponentAccessor.getIssueService()
def parentIssueIsValid = false
def childIssueIsDone = false
def issue = event.issue									// comment out if it's not a listener (e.g. post-function)

def actionId = 71									// action id in child linked issue
def parentProjectKey = 'TEST'
def inwardLinkName = 'Blocks'
def outwardLinkName = 'Depends on'
def parentIssueTypeId = '12000'
def childIssueTypeId = '13000'
def childDoneStatusId = '15000'								// finality status id in child issue
def parentIssueStatusId = '13000'
def parentIssueStatus = constantsManager.getStatus(parentIssueStatusId).getName()	// status name of parent issue

def parentIssue
def childIssue

for( inIssueLink in issueLinkManager.getInwardLinks(issue.getId()) ) {

    /* If the project key of the parent issue is equal to the specified key in the variable "parentProjectKey"
       And if parent issue type is equal to the specified type in the variable "parentIssueTypeId"
       And if name of the child issue incoming link is equal to the name in the variable "inwardLinkName"
       And if the current status of the parent issue is equal to the status in the variable "parentIssueStatusId"
    */

    if( inIssueLink.getSourceObject().projectObject.key == parentProjectKey && 
    inIssueLink.getSourceObject().getIssueTypeId() == parentIssueTypeId && 
    inIssueLink.getIssueLinkType().getInward() == inwardLinkName && 
    inIssueLink.getSourceObject().getStatusId() == parentIssueStatusId ) {
        // on the second iteration of the loop (if the parent issue is duplicated) - the script ends
        if( parentIssueIsValid ) {
            def errorMessage = 'Unable to transition dependent issue "' + parentIssue + '" to status "' + parentIssueStatus + '": there is a duplicate of this issue type'
            log.warn( errorMessage )
            return false
        }
        parentIssue = inIssueLink.getSourceObject()
        parentIssueIsValid = true
    }
}
if( !parentIssueIsValid ) {
    def errorMessage = 'There is no suitable dependent issue to transfer to "' + parentIssueStatus + '" status.'
    log.warn( errorMessage )
    return false
}
for( outIssueLink in issueLinkManager.getOutwardLinks(parentIssue.getId()) ) {
    childIssue = outIssueLink.getDestinationObject()

    /* If the child issue type is equal to the specified type in the variable "childIssueTypeId"
       And if the parent issue outward link name is equal to the name in the variable "outwardLinkName"
    */

    if( childIssue.getIssueTypeId() == childIssueTypeId && 
    outIssueLink.getIssueLinkType().getOutward() == outwardLinkName ) {
        // if the status of at least one child issue is not final, exit the script
        if( childIssue.getStatus().getId() != childDoneStatusId ) {
            return false
        }
        childIssueIsDone = true
    }
}
if( parentIssueIsValid && childIssueIsDone ) {
    def transitionValidationResult = issueService.validateTransition(user, parentIssue.getId(), actionId, new IssueInputParametersImpl())
    if( transitionValidationResult.isValid() ) {
        if( !issueService.transition(user, transitionValidationResult).isValid() ) {
            log.warn('Transition result for "' + parentIssue.getKey() + '" with action id "' + actionId + '" is not valid')
        }
    } else {
        log.warn('Transition validation for "' + parentIssue.getKey() + '" with action id "' + actionId + '" is not valid')
    }
}

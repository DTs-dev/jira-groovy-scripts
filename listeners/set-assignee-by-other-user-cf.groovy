import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.MutableIssue

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cfResponsible = customFieldManager.getCustomFieldObject(12345L).getFieldName() // user customfield
def change = event?.getChangeLog()?.getRelated("ChildChangeItem")?.find { it.field == cfResponsible }
if( change ) {
	def issue = event.getIssue() as MutableIssue
	def category = issue.projectObject.getProjectCategory().getId()
	def categoryPartner = 11111
	if( category != categoryPartner ) { // Category checking
		return
	}
	def issueManager = ComponentAccessor.getIssueManager()
	def assignee = change.newvalue as String
	issue.setAssigneeId(assignee)
	issueManager.updateIssue(event.user, issue, EventDispatchOption.ISSUE_ASSIGNED, true)
}

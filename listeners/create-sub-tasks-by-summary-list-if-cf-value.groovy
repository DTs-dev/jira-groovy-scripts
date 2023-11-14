import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.component.ComponentAccessor

def constantManager = ComponentAccessor.getConstantsManager()
def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def issueFactory = ComponentAccessor.getIssueFactory()
def subTaskManager = ComponentAccessor.getSubTaskManager()
def issueManager = ComponentAccessor.getIssueManager()
def department = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(11111L)
def lead = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(22222L)
def issue = event.issue

if( !issue.getCustomFieldValue( department ).grep('IT-service') ) {
    return
}

def summaryList = [
    'Title 1',
    'Title 2',
    'Title 3',
    'Title 4',
    'Title 5'
]

summaryList.each {
    MutableIssue newSubTask = issueFactory.getIssue()
    newSubTask.setReporterId(issue.reporterId)   
    newSubTask.setAssigneeId(issue.assigneeId)
    newSubTask.setSummary(it)
    newSubTask.setCustomFieldValue(lead, issue.getCustomFieldValue(lead))
    newSubTask.setParentObject(issue)
    newSubTask.setProjectObject(issue.getProjectObject())
    newSubTask.setIssueTypeId(constantManager.getAllIssueTypeObjects().find{ it.getName() == "Sub-task" }.id)
    def newIssueParams = ["issue" : newSubTask] as Map<String,Object>
    issueManager.createIssueObject(user, newIssueParams)
    subTaskManager.createSubTaskIssueLink(issue, newSubTask, user)
}

import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter
import com.opensymphony.workflow.InvalidInputException

def mngIssueTypeId = '11111' // Project management

if( issue.issueTypeId == mngIssueTypeId ) {
    def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
    def jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser)
    def searchService = ComponentAccessor.getComponent(SearchService)
    def query = jqlQueryParser.parseQuery( "project = " + issue.projectId + " AND issuetype = " + issue.issueTypeId )
    def results = searchService.search(user, query, PagerFilter.getUnlimitedFilter())
    if(!results.getResults().isEmpty()) {
        def errorMessage = 'Issue with type "' + issue.issueType.getName() + '" is exists in project "' + issue.projectObject.getKey() + '"'
        throw new InvalidInputException( errorMessage )
    }
}

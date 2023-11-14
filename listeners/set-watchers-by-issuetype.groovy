import com.atlassian.jira.component.ComponentAccessor

def watcherManager = ComponentAccessor.getWatcherManager()
def userManager = ComponentAccessor.getUserManager()
def issue = event.issue
def category = issue.projectObject.getProjectCategory().getId()
def type = issue.getIssueType().getName()

def categoryPartner = 11111
def typeList1 = [ 'Story', 'Sale' ]
def typeList2 = [ 'Bug', 'Bugfix', 'Fail', 'Develop' ]
def userList1 = [ 'user1', 'user2', 'user3', 'user4', 'user5' ]
def userList2 = [ 'user6', 'user7', 'user8' ]

if( category != categoryPartner ) { // Category checking
	return
}

if( type in typeList1 ) {
	userList1.each {
		def user = userManager.getUserByName(it)
		watcherManager.startWatching(user, issue)
	}
}
else if( type in typeList2 ) {
	userList2.each {
		def user = userManager.getUserByName(it)
		watcherManager.startWatching(user, issue)
	}
}
else {
    return
}

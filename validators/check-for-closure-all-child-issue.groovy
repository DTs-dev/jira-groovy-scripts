def outwardLinkName = 'Depends on'
def childDoneStatusId = '11111'
def childIssueTypeId = '12222'

def childIssue

for( outIssueLink in issueLinkManager.getOutwardLinks(issue.getId()) ) {
    childIssue = outIssueLink.getDestinationObject()
    
    if( childIssue.getIssueTypeId() == childIssueTypeId && 
    outIssueLink.getIssueLinkType().getOutward() == outwardLinkName ) {
        if( childIssue.getStatus().getId() != childDoneStatusId ) {
            return false
        }
    }
}
return true

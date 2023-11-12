import com.atlassian.jira.issue.Issue
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*
import groovyx.net.http.ContentType
import java.net.URLEncoder

Issue issue = event.getIssue()

def issueType = '15'

if( issue.getIssueTypeId() == issueType ) {                     // Condition (issue type)
    def baseurl = 'https://smsc.ru'                             // Request URL (using the example of the smsc.ru service)
    def login = 'login'
    def password = 'p@ssw0rd'
    def notifPhones = '+XXXXXXXXXXX'                            // Phones for sms (separated by commas)
    def message = 'New task: ' + issue.getKey()                 // Message

    def uri = '/sys/send.php?login=' + login + 
              '&psw=' + password + 
              '&phones=' + notifPhones + 
              '&mes=' + URLEncoder.encode(message, 'UTF-8') + 
              '&fmt=3&charset=utf-8'                            // Request URI

    def http = new HTTPBuilder( baseurl + uri )
    http.request( GET, ContentType.JSON ) { req ->
        response.success = { resp, json ->
            if( json['error'] ) {
                 log.error( 'SMS sending for issue ' + issue.getKey() + ' occurred error: ' + json['error'] )
            }   
        }
        response.failure = { resp, json ->
            log.error( 'Failed request for SMS sending on issue ' + issue.getKey() + ': ' + json )
        }
    }
}  

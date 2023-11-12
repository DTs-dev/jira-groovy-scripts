import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.config.properties.APKeys
import groovy.transform.BaseScript
import javax.ws.rs.core.MultivaluedMap
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*
import groovyx.net.http.ContentType
import groovy.json.JsonBuilder
import javax.ws.rs.core.Response

final token = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"   // Replace with your user token

@BaseScript CustomEndpointDelegate delegate

assignable(httpMethod: "GET") { MultivaluedMap queryParams ->
    def funnyCat = queryParams.getFirst("funnyCat") as String
    def username = queryParams.getFirst("username") as String
    def projectKeys = queryParams.getFirst("projectKeys") as String
    def issueKey = queryParams.getFirst("issueKey") as String
    def maxResults = queryParams.getFirst("maxResults") as Long
    def timestamp = queryParams.getFirst("_") as Long
    def baseUrl = ComponentAccessor.applicationProperties.getString(APKeys.JIRA_BASEURL)
    def http = new HTTPBuilder( baseUrl )
    def result
    
    if( funnyCat == 'Gabriel' ) {
        http.setHeaders([
            "Authorization": "Bearer $token",
            "X-Atlassian-Token": "no-check",
            "ContentType": "application/json"
        ])
    }

    http.request( GET, ContentType.JSON ) { req ->
        uri.path = '/rest/api/latest/user/assignable/search'   
        uri.query = [ username:username, projectKeys:projectKeys, issueKey:issueKey, maxResults:maxResults, _:timestamp ]

        response.success = { resp, json ->
            result = Response.ok(new JsonBuilder(json).toString()).build()
        }
        response.failure = { resp, json ->
            result = Response.ok(new JsonBuilder(json).toString()).build()
            log.warn result
        }
    }
    return result
}

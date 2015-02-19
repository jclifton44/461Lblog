package com.example.pogsBlog;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.log.*;
import java.io.PrintWriter;
import java.util.Calendar;

// Get request logs along with their app log lines and display them 5 at
// a time, using a Next link to cycle through to the next 5.
public class logHelper extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
         throws IOException {

    resp.setContentType("text/html");
    PrintWriter writer = resp.getWriter();
    // We use this to break out of our iteration loop, limiting record
    // display to 5 request logs at a time.
    int limit = 999999999;

    // This retrieves the offset from the Next link upon user click.
    String offset = req.getParameter("offset");

    // We want the App logs for each request log
    LogQuery query = LogQuery.Builder.withDefaults();
    query.includeAppLogs(true);

    // Set the offset value retrieved from the Next link click.
    if (offset != null) {
      query.offset(offset);
    }

    // This gets filled from the last request log in the iteration
    String lastOffset = null;
    int i = 0;

    // Display a few properties of each request log.
    for (RequestLogs record : LogServiceFactory.getLogService().fetch(query)) {
      writer.println("<br />REQUEST LOG <br />");
      Calendar cal = Calendar.getInstance();
      cal.setTimeInMillis(record.getStartTimeUsec() / 1000);

      writer.println("IP: " + record.getIp()+"<br />");
      writer.println("Method: " + record.getMethod()+"<br />");
      writer.println("Resource " + record.getResource()+"<br />");
      writer.println(String.format("<br />Date: %s", cal.getTime().toString()));

      lastOffset = record.getOffset();

      // Display all the app logs for each request log.
      for (AppLogLine appLog : record.getAppLogLines()) {
        writer.println("<br />"+ "APPLICATION LOG" +"<br />");
        Calendar appCal = Calendar.getInstance();
        appCal.setTimeInMillis(appLog.getTimeUsec() / 1000);
        writer.println(String.format("<br />Date: %s",
                            appCal.getTime().toString()));
        writer.println("<br />Level: "+appLog.getLogLevel()+"<br />");
        writer.println("Message: "+ appLog.getLogMessage()+"<br /> <br />");
      } //for each log line

      if (++i >= limit) {
        break;
      }
    } // for each record

    // When the user clicks this link, the offset is processed in the
    // GET handler and used to cycle through to the next 5 request logs.
    writer.println(String.format("<br><a href=\"/?offset=%s\">Next</a>",
                             lastOffset));
  }  // end doGet
} //end class
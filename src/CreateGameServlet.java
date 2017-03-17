// Vamshi Garikapati (vkg5xt) and Rohan Raval (rsr3ve)

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.portable.InputStream;

/**
 * Servlet implementation class CreateGameServlet
 */
@WebServlet("/CreateGameServlet")
public class CreateGameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateGameServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	ServletContext context = getServletContext();
        response.setContentType ("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html>"
        			+ "<head>"
        			+ "<meta charset=\"utf-8\">"
					+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
					+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
					+ "<title>Jeopardy!</title>"
					+ "<!-- Bootstrap -->"
					+ "<link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">"
					+ "<!-- jQuery library -->"
					+ "<script src=\"https://code.jquery.com/jquery-3.1.1.min.js\" integrity=\"sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8=\"   crossorigin=\"anonymous\"></script>"
					+ "<!-- Javascript file -->"
					+ "<script src=\"create_question.js\"></script>"
					+ "<!-- CSS file -->"
					+ "<link href=\"stylesheet.css\" rel=\"stylesheet\">"
					+ "</head>");
        
        out.println("<body> "
        		+ " <center> "
        		+ "	<form method=\"post\">"
					+ "	<table class=\"table\" border = \"3\" table-align =\"center\"> " + "<h3> Q/A Jeopardy Menu by Vamshi Garikapati and Rohan Raval </h3>");
        
        URL url = new URL("http://plato.cs.virginia.edu/~rsr3ve/cs4640/Jeopardy_v3/submission.txt");
        java.io.InputStream is = url.openStream();
        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String text;
            int counter = 0;
                        
            while ((text = reader.readLine()) != null) {
            	if(text.isEmpty()) {
            		out.println("</td>"
            				+ "<td><label>Row </label> <input type = \"text\" name = \"row" +  "\" style=\"width:40%\"></input></td>"
            				+ "<td><label>Column </label> <input type = \"text\" name = \"column" +  "\" style=\"width:40%\"></input></td>"
            				+ "<td><label>Score </label> <input type = \"text\" name = \"score" +  "\" style=\"width:40%\"></input></td>"
            				+ "</tr>");
            	} else if (text.contains("Submission")) {
            		out.println("<tr><td name=\"submission\" >");
                	counter++;
            	} else if(text.contains("Submitted Question")) { 
            		out.println("<strong>"+text.substring(0,19)+"</strong>  <input name=\"question" +  "\" value=\" " + text.substring(22)+"\" style=\"width:70%\"><br>");
            	} else if(text.contains("Correct Option")) {
            		out.println("<strong>"+text.substring(0,15)+"</strong>  <input name=\"answer" +  "\" value=\" " + text.substring(18)+"\" style=\"width:70%\"><br>");
            	} else if(text.contains("Submitted Answer")) {
            		out.println("<strong>"+text.substring(0,17)+"</strong>  <input name=\"answer" +  "\" value=\" " + text.substring(20)+"\" style=\"width:70%\"><br>");
            	}
        	}
            out.println("<input hidden name=\"count\" value=\"" + counter + "\">");
        }
        out.println("</table>"
        		+ "<button type=\"submit\" class=\"btn btn-primary\" formaction=\"http://plato.cs.virginia.edu/~rsr3ve/cs4640/Jeopardy_v3/create_question.php\">Add Q/A</button>  "
        		+ "<input type=\"submit\" value=\"Create Game\" class=\"btn btn-primary\">"
        		+ " </form>"
        		+ "</center>"
        		+ "</body>"
        		+ "</html>");
        out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext context = getServletContext();
        response.setContentType ("text/html");
        PrintWriter out = response.getWriter();
        
        // FILE OUTPUT BASED ON POST DATA
        
        FileWriter fileoutput = new FileWriter("/Users/Rohan/Documents/cs4640/apache/webapps/cs4640/Jeopardy_v4/WebContent/WEB-INF/postData.txt");
    
        String[] rows = request.getParameterValues("row");
        String[] cols = request.getParameterValues("column");
        String[] scores = request.getParameterValues("score");
        String[] questions = request.getParameterValues("question");
        String[] answers = request.getParameterValues("answer");

        int rowMax = 0;
        int colMax = 0;
        
        int countSubmissions = Integer.parseInt(request.getParameter("count"));
        int validSubmissions = 0;
        for(int i = 0; i < countSubmissions; i++) {        	
        	if(rows[i] != null && rows[i] != "" && cols[i] != null && cols[i] != "" && scores[i] != null && scores[i] != "") {
        		validSubmissions++;
        		fileoutput.write("Question: " + questions[i] + "; ");
        		fileoutput.write("Answer: " + answers[i] + "; ");
        		fileoutput.write("Row: " + rows[i] + "; ");
        		fileoutput.write("Col: " + cols[i] + "; ");
        		fileoutput.write("Score: " + scores[i] + ". \n");
        		
        		if(Integer.parseInt(rows[i]) > rowMax)
            		rowMax = Integer.parseInt(rows[i]);
            	if(Integer.parseInt(cols[i]) > colMax)
            		colMax = Integer.parseInt(cols[i]);
        	}
        }
        out.println(rowMax);
        out.println(colMax);

        
        //FILL GRID WITH POST DATA
        String[][] positions = new String[rowMax+1][colMax+1];
       
    	for(int i = 0; i < validSubmissions; i++) {
    		positions[ Integer.parseInt(rows[i]) ][ Integer.parseInt(cols[i]) ] = scores[i];
    		out.println(positions[ Integer.parseInt(rows[i]) ][ Integer.parseInt(cols[i]) ]);
    	}
    	
    	
    	//print grid into table
    	for(int currRow = 0; currRow < rowMax; currRow++) {
    		for(int currCol = 0; currCol < colMax; currCol++) {
    			out.println(positions[currRow][currCol]);
    		}
    	}
    	fileoutput.close();
	}

}
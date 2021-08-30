import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/process")
public class Process extends HttpServlet{

    public String getDataBaseFromGoogleSheet(String sheetId,String sheetName) throws MalformedURLException, IOException{
        String database = "No Sheet Found";

        String tagetURL = "https://docs.google.com/spreadsheets/d/"+sheetId+"/gviz/tq?tqx=out:csv&sheet"+sheetName;
        HttpURLConnection connection = (HttpURLConnection)new URL(tagetURL).openConnection();
        InputStream stream = connection.getInputStream();
        int c = 0;
        StringBuilder sb = new StringBuilder();
        do{
            c = stream.read();
            sb.append((char)c);
        }while(c!=-1);
        database = sb.toString().replace("\"","");
        return database;
    }

    public String getStudentDetails(String rollNo) throws MalformedURLException, IOException{
        String database = getDataBaseFromGoogleSheet("1Njnz3U8skMg8m1M5-k6HY6tCPp88Qj8mg-JEwnOBE-I","II-IT");
        
        String result = "No data found";
        String[] lines = database.split("\n");
        for(String line:lines){
            String[] words = line.split(",");
            if(words[0].equals(rollNo)){
                result = ""
                +"\nReg No:"+words[1]
                +"\nName:"+words[2]
                +"\nGmeetName:"+words[3];

            }
        }

        return result;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rollNo = req.getParameter("rollno");
        String output = getStudentDetails(rollNo);
        PrintWriter pw = resp.getWriter();
        pw.println(output);
    }
}

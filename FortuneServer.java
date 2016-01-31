import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FortuneServer
{
   public static void main(String[] args) throws IOException
   {
      ServerSocket serverSocket = null;
      ProcessBuilder pb = new ProcessBuilder();
      File f = null;

      pb.command("/usr/local/bin/fortune");
      pb.redirectOutput(ProcessBuilder.Redirect.to(new File("fortune.txt")));
      Socket clientSocket = null;

      while (true)
      {

         if (serverSocket == null)
         {
            try
            {
               serverSocket = new ServerSocket(8017);
            }
            catch (IOException e)
            {
               System.err.println("Could not listen on port: 8017.");
               System.exit(1);
            }
         }
         else if (clientSocket == null)
         {
            try
            {
               clientSocket = serverSocket.accept();
            }
            catch (IOException e)
            {
               clientSocket = null;
            }
            if (clientSocket != null)
            {
               PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);
               BufferedReader in = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));

               String inputLine = "";
               Scanner fileReader = null;
               pb = new ProcessBuilder();
               pb.command("/usr/local/bin/fortune");
               pb.redirectOutput(ProcessBuilder.Redirect.to(new File(
                        "fortune.txt")));
               pb.start();

               while (f == null)
               {
                  f = new File("fortune.txt");

                  fileReader = new Scanner(f);
                  inputLine = "";

                  while (fileReader.hasNextLine())
                  {
                     inputLine += fileReader.nextLine()
                              + System.lineSeparator();
                  }
                  if (!inputLine.equals(""))
                  {
                     out.println(inputLine);
                     out.flush();
                  }
                  else
                  {
                     f = null;
                     fileReader.close();
                     fileReader = null;
                  }
               }

               out.close();
               in.close();
               clientSocket.close();
               clientSocket = null;
               serverSocket.close();
               serverSocket = null;

               if (fileReader != null)
               {
                  fileReader.close();
                  fileReader = null;
               }

               if (f != null)
               {
                  f = null;
               }
            }
         }
      }
   }
}
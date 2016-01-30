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

      try
      {
         serverSocket = new ServerSocket(10007);
      }
      catch (IOException e)
      {
         System.err.println("Could not listen on port: 10007.");
         System.exit(1);
      }

      Socket clientSocket = null;
      System.out.println("Waiting for connection.....");

      try
      {
         clientSocket = serverSocket.accept();
      }
      catch (IOException e)
      {
         // ignore, try again shortly.
      }

      // System.out.println("Connection successful");
      // System.out.println("Waiting for input.....");

      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(
               clientSocket.getInputStream()));

      String inputLine = "";
      File f = new File("fortune.txt");
      Scanner fileReader;

      while (true)
      {
         // Display message from Client and echo it back.
         if ((inputLine = in.readLine()) != null)
         {
            // System.out.println("Client> " + inputLine);
            pb.command("/usr/local/bin/fortune");
            pb.redirectOutput(ProcessBuilder.Redirect.to(f));
            pb.start();
            fileReader = new Scanner(f);
            inputLine = "";

            while (fileReader.hasNextLine())
            {
               inputLine += fileReader.nextLine() + " ";
            }
            out.println(inputLine);
            out.flush();
            break;
         }
      }

      out.close();
      in.close();
      clientSocket.close();
      serverSocket.close();
   }
}
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class FortuneServerClient
{
   public static void main(String[] args) throws IOException
   {

      String serverHostname = new String("127.0.0.1");

      if (args.length > 0)
         serverHostname = args[0];
      System.out.println("Attemping to connect to host " + serverHostname
               + " on port 8017.");

      Socket echoSocket = null;
      PrintWriter out = null;
      BufferedReader in = null;

      try
      {
         // echoSocket = new Socket("taranis", 7);
         echoSocket = new Socket(serverHostname, 8017);
         out = new PrintWriter(echoSocket.getOutputStream(), true);
         in = new BufferedReader(new InputStreamReader(
                  echoSocket.getInputStream()));
      }
      catch (UnknownHostException e)
      {
         System.err.println("Don't know about host: " + serverHostname);
         System.exit(1);
      }
      catch (IOException e)
      {
         System.err.println("Couldn't get I/O for " + "the connection to: "
                  + serverHostname);
         System.exit(1);
      }

      BufferedReader stdIn = new BufferedReader(
               new InputStreamReader(System.in));
      String userInput = "";

      System.out.print("Client> ");

      while (true)
      {
         // Send input from client.
         if ((userInput = stdIn.readLine()) != null)
         {
            out.println(userInput);
            out.flush();
            if (!(userInput == null) && userInput.equals("logout"))
               break;
            System.out.println("Server> " + in.readLine());
            System.out.print("Client> ");
         }

      }

      out.close();
      in.close();
      stdIn.close();
      echoSocket.close();
   }
}
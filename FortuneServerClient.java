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
      // System.out.println("Attemping to connect to host " + serverHostname
      // + " on port 8017.");

      Socket echoSocket = null;
      PrintWriter out = null;
      BufferedReader in = null;

      try
      {
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
      boolean gotInput = false;

      while (true)
      {
         while ((userInput = in.readLine()) != null)
         {
            System.out.println(userInput);
            gotInput = true;
         }

         if (gotInput)
            break;

      }

      out.close();
      in.close();
      stdIn.close();
      echoSocket.close();
   }
}
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is designed to open a Server Socket to listen on the default port
 * number as indicated below. Note that an arbitrary port and command could be
 * specified in the future, should anyone choose to do so the keyword final
 * would need to be removed from both the port number and the command variables.
 */
public class FortuneServer
{
   private final static String COMMAND_PATH        = "/usr/games/fortune";
   private final static int    DEFAULT_PORT_NUMBER = 8017;

   /**
    * Main method to open a Socket, listen on a port and accept any client. If a
    * client connects they will be sent a fortune and then disconnected.
    * 
    * @param args
    *           Not used
    * @throws IOException
    *            If an error occurs during connecting, disconnecting, or reading
    *            from any of the ports used.
    */
   public static void main(String[] args) throws IOException
   {
      // Local variables.
      ProcessBuilder processHandler = new ProcessBuilder(COMMAND_PATH);
      ServerSocket serverSocket = null;
      Socket clientSocket = null;
      PrintStream clientOutputStream;
      BufferedReader processOutputReader;
      String commandOutputLine;

      // Be prepared to handle Exceptions from IO.
      try
      {
         // Open our Server Socket.
         serverSocket = new ServerSocket(DEFAULT_PORT_NUMBER);

         // Loop infinitely, listening for connections. If a connection
         // occurs, serve up a fortune and then disconnect the client.
         while (true)
         {
            // Wait for a client to connect...
            clientSocket = serverSocket.accept();

            // If a client connects, then get their output stream to output to.
            clientOutputStream = new PrintStream(
                     clientSocket.getOutputStream(), true);

            // Start the process, then we create an input buffer to read
            // the output from.
            processOutputReader = new BufferedReader(new InputStreamReader(
                     processHandler.start().getInputStream()));

            // Read the output of the process and send it out to our client
            // socket.
            while ((commandOutputLine = processOutputReader.readLine()) != null)
            {
               clientOutputStream.println(commandOutputLine);
            }

            // Finally, close the client Socket, and the related buffers and
            // resume listening for connections.
            processOutputReader.close();
            clientOutputStream.close();
            clientSocket.close();
         }
      }
      catch (IOException e)
      {
         // Print a stack trace to the Server-side.
         e.printStackTrace();
      }
      finally
      {
         // Close our clientSocket if it is still open.
         if (clientSocket != null && !clientSocket.isClosed())
         {
            clientSocket.close();
         }

         // Close our serverSocket if it is still open.
         if (serverSocket != null && !serverSocket.isClosed())
         {
            serverSocket.close();
         }
      }
   }
}
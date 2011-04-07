package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class is responsible for reading system commands in the terminal
 * @author Marcel Boersma & Anton Timmermans
 *
 */
public class SystemCommands implements Runnable {

	/**
	 * This is the run function in the tread, it will continuously scan for input commands given by the user.
	 */
	@Override
	public void run() {
		
		BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
		
		while(true)
		{
			
			try {
				if(line.ready())
				{
					String command = line.readLine();
					
					if(command.equals("stop"))
					{
						System.out.println("Terminating the server...");
						ServerController.sharedApplication().terminate();
					}
					else{
						System.out.println("No such command found..");
					}
				}
			} catch (IOException e) {
				
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
		}

	}

}

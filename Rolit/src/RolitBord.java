/**
 * 
 */

/**
 * @author marcelboersma
 *
 */
public class RolitBord implements AbstractModule {

}


Game g = new Game();
g.addModule(new RolitBord());
g.addModule(new ChatClient());
g.addModule(new Hints());

g.addDefaultProtocol(new P2Protocol);
P2Protocol.read();
P2Protocol.parse();

Player p = new Player();// player created, server know's what he can handle so the observers can be created
p->setProtocol(new PreferredProtocolByPlayer<Protocol>());
g.addObserver(p, RolitBord);
g.addObserver(p, ChatClient);
g.addObserver(p, Hints);

g.ready() //checks if all modules have the required information	
g.start();



//player get's notify from observer
p.notified(dataObject)
{
	//send message with players requested protocol
	socketSend(p->protocol()->sendMessage(dataObject));
	
}
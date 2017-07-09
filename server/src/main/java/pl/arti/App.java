package pl.arti;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.client.ClusterClientReceptionist;
import akka.routing.FromConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import pl.arti.actor.PrintActor;

import java.io.IOException;
import java.util.Scanner;

public class App {
  public static void main(String[] args) throws InterruptedException, IOException {
    String node = args[0];
    String port = args[1];
    System.out.println("Node:Port: " + args[0] + ":" + args[1]);

    Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load());

    ActorSystem system = ActorSystem.create("akk-arti", config);

    if ("node1".equals(node)) {

      ActorRef actorRef = system.actorOf(
        FromConfig.getInstance().props( //musi być from config bo inaczej nie bierze deployment z application.conf
          Props.create(PrintActor.class))
        , "PrintActor");
      ActorRef actorRef1 = system.actorOf(
        FromConfig.getInstance().props( //musi być from config bo inaczej nie bierze deployment z application.conf
          Props.create(PrintActor.class))
        , "PrintActor1");

      /** Receptionist do nastepnych testow */
//      ClusterClientReceptionist.get(system).registerService(actorRef);

      new Scanner(System.in).nextLine();
      for (int i = 0; i < 100; i++)
        actorRef.tell("Cześć " + i + " raz", ActorRef.noSender());
    } else if ("node2".equals(node)) {
      ActorRef actorRef1 = system.actorOf(
        FromConfig.getInstance().props( //musi być from config bo inaczej nie bierze deployment z application.conf
          Props.create(PrintActor.class))
        , "PrintActor");
    }

    new Scanner(System.in).nextLine();
    system.terminate();
  }
}

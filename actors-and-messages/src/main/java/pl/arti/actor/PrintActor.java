package pl.arti.actor;

import akka.actor.AbstractActor;

public class PrintActor extends AbstractActor {

  @Override
  public AbstractActor.Receive createReceive() {
    return receiveBuilder()
      .match(String.class, req -> {
        System.out.println(Thread.currentThread().getName() + " -> " + req);
        Thread.sleep(1000);
      })
      .build();
  }
}

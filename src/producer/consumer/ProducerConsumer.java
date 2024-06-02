 /*
      1- You can Produce a Drink if only Store is empty.
      2- You can Consume a Drink if only Store is full.
      3- REM : you can't access at the variable in the same time  because it's a shared variable (Here, we are talking about Drink class) 
         So, if it's a shared variable, then consumer can't consume if producer produce vice-versa(reciprocally).
         in another word if the producer is producing then consumer must be blocked.
        
_In conclusion_ : 
------------------
we use a synchronous mechanism (NB: Not Asynchronous, but Synchronous ) because one process at the time.
So, Setting up a synchronous mechanism involves using locks (verrou in french)to avoid race conditions and ensure synchronization.
this loks is represented by key word  << Synchronized >>
In programming, a lock (or verrou in French) is generally considered an object(Class in POO) or a mechanism.
 1. Object/Mechanism for Synchronization:
    *Locks are objects that provide mechanisms to control access to shared resources. 
    *They ensure that only one thread at a time can access the resource protected by the lock.
 */
package producer.consumer;

import java.util.Stack;

/**
 *
 * @author addai
 */
public class ProducerConsumer {
     static Store store;
     static Producer producer;
     static Consumer consumer; 

    public static void main(String[] args) {
        
       store = new Store();
       producer = new Producer();
       consumer = new Consumer();

        // Démarrage des threads
        producer.start();
        consumer.start();

        // Attente de la fin de l'exécution des threads
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
    
    // (Pile in french. check you Stack-Overflow )
    public static class Store extends Stack {  
      

        
       final int Max = 100 ;
       private static boolean IsFull = false ; 
       private static boolean IsEmpty = true; // by default
       int N = 0 ;
       
        synchronized void PlaceOnStack(Drink drink) // (Empiler in frensh)
        {
            while(IsFull == true)
            {
               try {
                    System.out.println("Producer waiting: store is full.");
                    wait();  // block the process  
                    
               }catch(InterruptedException ex)
               {
                  Thread.currentThread().interrupt();
               }
           }
 
                //Otherwise 
            this.push(drink);
            N++;
            System.out.println("Product added. Number of products : " + N);
            if(N==1)
            {
                IsEmpty = false ;
                notifyAll(); // notify if empty is true in order to add  
               System.out.println("Store is no longer empty..");
            }else if( N == Max)
            {
                IsFull = true ;
                System.out.println("Store is full.");
            }
         
        }
        
        synchronized void ExtractFromStack() // (Dépiler in frensh)
        {
            while(IsEmpty ){
                
                try {
                  
                    System.out.println("Consumer is waiting  : Store is empty.");
                     wait();  //block the process
                     
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
            } 
            
            this.pop();
            N--;
            System.out.println("Product consumed. Number of products : " + N);
            if(N == Max-1)
            {
                IsFull = false;
                
                notifyAll(); 
                System.out.println("Store is no longer full.");
                
            }else if(N==0)
            {
               IsEmpty = true;
               System.out.println("Store is empty ");
            }
        }
    }

    public static class Producer extends Thread {

      @Override
      public void run()
      {
          while(true)   
          {
              store.PlaceOnStack(new Drink());
          }
      }
    }
    
    public static class Consumer extends Thread {

        @Override
        public void run() {
            while(true)
            {
                store.ExtractFromStack();
            }
        }    
    }
    
    // (Boisson in french)
    public static class Drink extends Object { 
        
    }   
}

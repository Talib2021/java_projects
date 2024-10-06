import java.lang.reflect.Array;
import java.util.*;

public class DISK2{
  public static void main(String[] args){
    Scanner scanner = new Scanner(System.in);
     System.out.print("inter the initial head possiton");
     int initialHead=scanner.nextInt();

     System.out.println("inter the no of possition you want to visit");
     int numCylinders=scanner.nextInt();

     int []cylinderSequence=new int[numCylinders];
     for(int i=0;i<numCylinders;i++){
      cylinderSequence[i]=scanner.nextInt();
     }

     int FCFShead=Calculatefcfs(cylinderSequence,initialHead);
     System.out.print("fcfs headmovement is"+FCFShead);

     System.out.print("inter the direction up or down");
     String direction=scanner.next().toLowerCase();
     int scanhead=calculateSCAN(cylinderSequence,initialHead, direction);
     System.out.print("headmovement int scan is"+scanhead);
        

  }

   public static int Calculatefcfs(int []cylinderSequence, int initialHead){
    int headMovement=0;
    for(int cylinder:cylinderSequence){
      headMovement+=Math.abs(initialHead-cylinder);
      System.out.print(initialHead+"=>");
          initialHead=cylinder;
    }
    return headMovement;

   }


   public static int calculateSCAN(int []cylinderSequence, int initialHead, String direction){
    int headMovement=0;

     Arrays.sort(cylinderSequence);
     int initialHeadIndex=Arrays.binarySearch(cylinderSequence,initialHead);
     
       if(initialHeadIndex<0){
        initialHeadIndex=-initialHeadIndex-1;
       }

    if(direction.equals("up")){
       for(int i=initialHeadIndex;i<cylinderSequence.length;i++){
       headMovement+=Math.abs(initialHead-cylinderSequence[i]);
       System.out.print(initialHead+"=>");
       initialHead=cylinderSequence[i];
    }
    headMovement+=Math.abs(initialHead-cylinderSequence[cylinderSequence.length-1]);
            

             for(int i=initialHeadIndex-1;i>=0; i--){
            headMovement+=Math.abs(initialHead-cylinderSequence[i]);
            System.out.print(initialHead+"=>");
            initialHead=cylinderSequence[i];

   }    System.out.print(initialHead);
  }
          else if(direction.equals("down")){
            for(int i=initialHeadIndex-1;i>=0; i--){
            headMovement+=Math.abs(initialHead-cylinderSequence[i]);
            System.out.print(initialHead+"=>");
            initialHead=cylinderSequence[i];

          }
          headMovement+=Math.abs(initialHead-cylinderSequence[0]);
          for(int i=initialHeadIndex+1;i<cylinderSequence.length-1; i++){
            headMovement+=Math.abs(initialHead-cylinderSequence[i]);
            System.out.print(initialHead+"=>");
            initialHead=cylinderSequence[i];}
             System.out.print(initialHead);
  
          }

    
    return headMovement;
   }
  }
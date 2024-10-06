import java.util.Arrays;
import java.util.Scanner;

public class Disk{

  public static void main(String[] args){
    Scanner scanner= new Scanner(System.in);
    System.out.print("inter the no of sylender ");
    int nocylender= scanner.nextInt();
    System.out.print("give me the initial head possition");
     int initialhead=scanner.nextInt();

    System.out.print("inter the sequence of cylender ");
    int []sequencecylender=new int[nocylender];
    for(int i=0;i<nocylender;i++){
     sequencecylender[i]= scanner.nextInt();
    }

    System.out.println("please prefer up or down for SCAN");
    String scandirection=  scanner.next().toLowerCase();
    int fcfsheadmovement=calculateFCFS(sequencecylender, initialhead);
    
    System.out.println("head movement in fcfs is"+"   " +fcfsheadmovement);
     int scanheadmovement=calculateSCAN(sequencecylender, initialhead, scandirection);

         System.out.println("head movement in SCAN is " + "   " + scanheadmovement);


  }

   public static int calculateFCFS(int[]sequencecylender,int initialhead){
       int headmovement=0;
       for(int cylinder:sequencecylender){
            headmovement+=Math.abs(initialhead-cylinder);
            initialhead=cylinder;
       }

       return headmovement;
   }
    
   public static int calculateSCAN(int[]sequencecylender,int initialhead, String scandirection){

    int headmovement=0;

    Arrays.sort(sequencecylender);
         int initialindex= Arrays.binarySearch(sequencecylender,initialhead);
          
         if(scandirection.equals("up")){
              for(int i=initialindex;i<sequencecylender.length;i++){
                   headmovement+=Math.abs(initialhead-sequencecylender[i]);
                   initialhead=sequencecylender[i];
              }

              headmovement+=Math.abs(initialhead-sequencecylender[sequencecylender.length - 1]);

               headmovement+=Math.abs(initialhead-sequencecylender[0]);
               for(int i=0;i<initialindex;i++){
                   headmovement+=Math.abs(initialhead-sequencecylender[i]);
                    initialhead=sequencecylender[i];

         }
        }

         else if(scandirection.equals("down")){
                for(int i=initialindex-1; i>=0;i--){
                   headmovement+=Math.abs(initialhead-sequencecylender[i]);
                   initialhead=sequencecylender[i];
              }
              headmovement+=Math.abs(initialhead-sequencecylender[0]);
                headmovement+=Math.abs(initialhead-sequencecylender[sequencecylender.length - 1]);
                 for(int i=sequencecylender.length-1;i<initialindex;i--){
                   headmovement+=Math.abs(initialhead-sequencecylender[i]);
                   initialhead=sequencecylender[i];
         }


   }
   return headmovement;

}
}
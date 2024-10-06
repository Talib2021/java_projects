import java.util.Scanner;

public class banker5{


 static final int numprocess=5;
 static final int numresourse = 3;
 static int[]available=new int[numresourse] ;
 static int[][]maxneed=new int[numprocess][numresourse] ;
 static int[][]need=new int[numprocess][numresourse];
 static int[][]allocation= new int[numprocess][numresourse];

public static void main(String[] args){
  Scanner scaner= new Scanner(System.in);
 System.out.print("inter the available resource ");
  for(int i=0;i<numresourse;i++){
    available[i]=scaner.nextInt();
  }
  System.out.println("inter the allocation matrix");
  for(int i=0;i<numprocess;i++){
    for(int j=0;j<numresourse;j++){
      allocation[i][j]=scaner.nextInt();
    }
  }
    System.out.println("inter the maxneed");
  
  for(int i=0;i<numprocess;i++){
    for(int j=0;j<numresourse;j++){
      maxneed[i][j]=scaner.nextInt();
    }
  }
  for(int i=0;i<numprocess;i++){
    for(int j=0;j<numresourse;j++){
      need[i][j]=maxneed[i][j]-allocation[i][j];
    }
  }
  if(issafe()){
     System.out.println("systen does not cause deedlock");
    
  }
  else{
    System.out.print("system cause deadlock");
  }
  int requestingprocess ;
  int []request=new int[numresourse];
  System.out.println("inter the requeating process");
  requestingprocess=scaner.nextInt() ;
  System.out.println("inter the request");
  for(int i=0;i<numresourse;i++){
    request[i]=scaner.nextInt();
  }

  if( requestgranted( requestingprocess , request)){
   System.out.print("request granted");
  }
  else{
    System.out.print("request denied");
  }
}

static boolean issafe(){
  int a=0;
  int []safesequence=new int[numprocess];
  boolean []finish=new boolean[numprocess];
  int  []wait=new int[numresourse];
  for(int i=0;i<numprocess;i++){
    finish[i]=false;
  }
  for(int i= 0;i<numresourse;i++){
    wait[i]=available[i];
  }

  for (int i=0;i<numprocess;i++){
    if(finish[i]) continue;
    boolean procesallowed=true;

    for (int j=0;j<numresourse;j++){
      if(need[i][j]>wait[j]){
       procesallowed=false;
        break;
      }
    }

    if(procesallowed){
      for (int j=0;j<numresourse;j++){
        wait[i]+=allocation[i][j];
      }
        safesequence[a]=i;
        finish[i]=true;
        i=-1;
        a++;

      }   
  }
  for(int i =0;i<numprocess; i++){
        if(finish[i]==false){
          return false;
        }
      }

  for(int i=0;i<numprocess;i++){
    System.out.print(safesequence[i]);
  }
return true ;
}

static boolean requestgranted(int requestingprocess , int []request){
  for (int i=0; i<numresourse;i++){
    if(need[requestingprocess][i]<request[i]|| available[i]<request[i]){
      return false;
    }  
  }
   for(int i=0;i<numresourse;i++){
    need[requestingprocess][i]-=request[i];
    available[i]-=request[i];
    allocation[requestingprocess][i]+=request[i];
   }

   if(issafe()){
    return true;
   }
   else{
    for(int i=0;i<numresourse;i++){
      need[requestingprocess][i]+=request[i];
    available[i]+=request[i];
    allocation[requestingprocess][i]-=request[i];
    }
   }
   return false;

}

}


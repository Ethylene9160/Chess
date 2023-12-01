package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Test {

    static int[] a = {0,0,0,0};
    public static void main(String[] args) {
        System.out.println(Arrays.toString(a));
        setArray(a);
        System.out.println(Arrays.toString(a));
    }

    public static void setArray(int[] a){
        a = new int[]{1,0,0};
        System.out.println(Arrays.toString(a));
    }
}


//class lsqAssignment implements Runnable{
//
//    @Override
//    public void run() {
//        Scanner in = new Scanner(System.in);
//        int n = in.nextInt();
//        int l = in.nextInt();
//        int m[] = new int[n];
//        int a[] = new int[n];
//        a[0]=0;
//        int b[] = new int[n];
//        int L = 0;
//        for (int i = 0; i < n; i++) m[i] = in.nextInt();
//        ArrayList<String> stm=new ArrayList<>();
//        ArrayList<String> tb=new ArrayList<>();
//        for(int i=0;i<n;i++) {
//            int p=0;
//            p=in.nextInt();
//            if(p==1) {
//                String str=in.next();
//                stm.add(str);
//                L=counterStatements(stm);
//                a[i]=L;
//                if(i>0)
//                    b[i]=b[i-1];
//                else
//                    b[i]=0;
//            }
//            if(p==2) {
//                String str=in.next();
//                tb.add(str);
//                l++;
//                if(i>0)
//                    a[i]=a[i-1];
//                if(i>0)
//                    b[i]=pairMatch(stm,str,b[i-1]);
//                System.out.println(a[i]);
//                if(a[i]>l)
//                    l=l-stm.size();
//            }
//            if(p==3) {
//                if(i>0)
//                {
//                    a[i]=a[i-1];
//                    b[i]=b[i-1];
//                }
//                System.out.println(b[i]);
//            }
//        }
//        if(l<0)System.out.println("Fail");
//        else
//        {
//            int a1=0;
//            int b1=0;
//            int m1=0;
//            for(int i=0;i<n;i++)
//                a1+=a[i];
//            for(int j=0;j<n;j++)
//                b1+=b[j];
//            for(int k=0;k<n;k++)
//                m1+=m[k];
//            int S=a1*b1*m1;
//            if(S<0)System.out.println("Fail");
//            else
//                System.out.println("Qi Fei");
//        }
//    }
//
//    public static int counterStatements(ArrayList<String> stm)
//    {
//        int n=stm.size();
//        int arr[]=new int [n];
//        for(int i=0;i<n;i++)
//            arr[i]=stm.get(i).length();
//        for (int i=0;i<arr.length-1;i++){
//            for (int j=0;j<arr.length-1-i;j++){
//                int temp = 0;
//                if (arr[j]>arr[j+1]) {
//                    temp = arr[j];
//                    arr[j] = arr[j+1];
//                    arr[j+1] = temp;
//                }
//            }
//        }
//        if(n%2==0)n=(n-1)/2;
//        else
//            n=n/2;
//        return arr[n];
//    }
//    public static int pairMatch(ArrayList<String>stm,String str,int n)
//    {
//        for(int i=0;i<stm.size();i++)
//        {
//            if(stm.get(i).equals(str))
//                n++;
//        }
//        return n;
//    }
//}

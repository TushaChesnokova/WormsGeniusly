public class Pair<A, B>{
    A A;
    B B;
    Pair(A a, B b){
        A = a;
        B = b;
    }
    public A getKey(){
        return A;
    }
    public B getValue(){
        return B;
    }
}
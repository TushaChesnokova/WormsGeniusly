public class Pair<T, B>{
    T A;
    B B;
    Pair(T a, B b){
        A = a;
        B = b;
    }
    public T getKey(){
        return A;
    }
    public B getValue(){
        return B;
    }
}
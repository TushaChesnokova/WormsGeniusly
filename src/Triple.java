public class Triple<A,B,C> {
    A A;
    B B;
    C C;

    public A getA() {
        return A;
    }

    public B getB() {
        return B;
    }

   Triple (A a, B b, C c) {
        A = a;
        B = b;
        C = c;
    }

    public C getC(){
       return C;
    }

}

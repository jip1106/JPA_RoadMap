package hellojpa.main;

import hellojpa.Address;

public class ValueMain {
    public static void main(String[] args) {
        int a = 10;
        int b = a;

        a = 20;

        System.out.println("a = " + a);
        System.out.println("b = " + b);

        Address address1 = new Address("a","b","c");
        Address address2 = new Address("a", "b", "c");

        System.out.println(address1.equals(address2));


    }
}

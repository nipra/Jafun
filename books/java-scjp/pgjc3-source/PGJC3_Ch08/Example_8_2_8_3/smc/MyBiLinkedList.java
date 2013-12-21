//Filename: MyBiLinkedList.java
package smc;

public class MyBiLinkedList implements ListPool.IBiLink { // (6)

  ListPool.MyLinkedList.BiNode objRef1
           = new ListPool.MyLinkedList.BiNode();     // (7)

//ListPool.MyLinkedList.ILink ref;                   // (8) Compile-time error!
}
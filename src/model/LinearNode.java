package model;

public class LinearNode<T> {

    T data;

    LinearNode<T> next;

    /*
    =================================================

    HUY:

    Node dùng cho Queue

    Example:

    Front

    [B001] → [B002] → [B003]

    Rear

    data = Batch
    next = node kế tiếp

    =================================================
    */

    public LinearNode(T data){

        this.data=data;

        this.next=null;

    }

}
package model;

public class LinearNode<T> {

    T data;

    LinearNode<T> next;


    public LinearNode(T data){
        this.data = data;
        this.next = null;
    }

    // Bổ sung Getter và Setter để thao tác với Queue
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LinearNode<T> getNext() {
        return next;
    }

    public void setNext(LinearNode<T> next) {
        this.next = next;
    }

}
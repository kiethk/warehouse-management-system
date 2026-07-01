package model;

public class MyQueue<T> {

    private LinearNode<T> front;

    private LinearNode<T> rear;
    
    // Thêm biến size để tối ưu hóa hàm size() thành O(1) thay vì O(N)
    private int size;

    /*
    =================================================

    HUY:

    Queue quản lý Batch theo FIFO

    Example:

    Front

    B001
    B002
    B003

    Rear


    enqueue()

    Front

    B001
    B002
    B003
    B004

    Rear


    dequeue()

    Front

    B002
    B003
    B004

    Rear

    =================================================
    */

    public MyQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    // TODO HUY:
    // enqueue(T data)
    public void enqueue(T data) {
        LinearNode<T> newNode = new LinearNode<>(data);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.setNext(newNode);
            rear = newNode;
        }
        size++;
    }

    // TODO HUY:
    // dequeue()
    public T dequeue() {
        if (isEmpty()) {
            return null; // Hàng đợi rỗng
        }
        
        T data = front.getData();
        front = front.getNext();
        size--;
        
        // Nếu lấy ra phần tử cuối cùng, cần cập nhật rear về null
        if (front == null) {
            rear = null;
        }
        
        return data;
    }

    // TODO HUY:
    // peek()
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return front.getData();
    }

    // TODO HUY:
    // isEmpty()
    public boolean isEmpty() {
        return front == null;
    }

    // TODO HUY:
    // size()
    public int size() {
        return size;
    }

    // TODO HUY:
    // display()
    public void display() {
        System.out.println("Front");
        LinearNode<T> current = front;
        while (current != null) {
            System.out.println(current.getData().toString());
            current = current.getNext();
        }
        System.out.println("Rear");
    }
    
    // Hàm getFront() được bổ sung để phục vụ cho Task 4 (duyệt Queue tính tổng)
    public LinearNode<T> getFront() {
        return front;
    }

}
package DataStructures;

public class ChangeTracker<T> {
    private T data;
    private int dataModeCount;
    
    public ChangeTracker() {
        this.data = null;
        this.dataModeCount = -1;
    }
    
    public void setData(T data, int dataModeCount) {
        if (this.wasChanged(dataModeCount)) {
            this.data = data;
            this.dataModeCount = dataModeCount;
        }
    }
    
    public T getData() {
        return data;
    }
    
    public boolean wasChanged(int dataModeCount) {
        return this.dataModeCount == -1 || this.data == null || this.dataModeCount != dataModeCount;
    }
}

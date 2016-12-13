package olegkuro.learnbyear.loader;

/**
 * Created by Roman on 12/12/2016.
 */

public class LoadResult<T> {
    public enum ResultType {
        OK,
        NO_NETWORK,
        EMPTY,
        UNKNOWN_ERROR
    }
    public ResultType type;
    public T data;

    public LoadResult(T data, ResultType type) {
        this.data = data;
        this.type = type;
    }
}

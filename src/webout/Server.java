package webout;

public interface Server extends Runnable{
    /**
     * 对服务器接收到的消息进行处理。
     * <b>不要私自调用这个方法！</b>
     * @param message 服务器接收到的消息
     * @author ethy9160
     */
    void transmit (String message);

    /**
     * webError: 请自行实现这个方法，在客户机断开时服务端得处理方法。如果是使用map，该方法需要额外实现将obj从map里移除。
     * 注意：<b>不要私自调用这个方法！</b>
     * @param obj
     * @author ethy9160
     */
    void webError(Object obj);
}

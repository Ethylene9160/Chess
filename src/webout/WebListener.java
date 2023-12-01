package webout;


public interface WebListener {
    /**
     * 客户机接收到消息后进行操作。
     * <b>不要私自调用这个方法！</b>
     * @param message 接收到的消息
     * @author ethy9160
     */
    void gettingAction(String message);

    /**
     * 如果网络连接出错，这个方法将被代理调用。
     * @author ethy9160
     */
    void showConnectError();
}

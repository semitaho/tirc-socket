package fi.toni.tircsocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.function.Consumer;

/**
 * Created by taho on 15/02/16.
 */
public class TircListenableFutureCallback<T> implements Consumer<T> {
  static Logger log = LoggerFactory.getLogger(TircListenableFutureCallback.class);




  @Override
  public void accept(final T t) {
    log.debug("Success sending data: {}", t);
  }

}

package com.senierr.setasklib.onSubscribes;

import com.senierr.setasklib.internal.Emitter;

import java.util.concurrent.TimeUnit;

/**
 * 延迟订阅事件
 *
 * @author zhouchunjie
 * @date 2017/6/9
 */

public class DelayOnSubscribe implements ObservableOnSubscribe<Long> {

    private long delay;
    private TimeUnit timeUnit;

    private long index = 0;

    public DelayOnSubscribe() {
    }

    public DelayOnSubscribe(long delay, TimeUnit timeUnit) {
        this.delay = delay;
        this.timeUnit = timeUnit;
    }

    @Override
    public void subscribe(Emitter<Long> emitter) throws Exception {
        if (!emitter.isCancel()) {
            emitter.onProcess(index);
            index++;
        }
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}

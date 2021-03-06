package com.senierr.setasklib.internal;

import com.senierr.setasklib.Observer;
import com.senierr.setasklib.onSubscribes.DelayOnSubscribe;
import com.senierr.setasklib.onSubscribes.IntervalOnSubscribe;
import com.senierr.setasklib.onSubscribes.ObservableOnSubscribe;
import com.senierr.setasklib.scheduler.SchedulerHelper;

import java.util.concurrent.Future;

/**
 * 执行器
 *
 * @author zhouchunjie
 * @date 2017/6/8
 */

public class Executor<T> implements Runnable {

    private volatile boolean isCancel = false;

    private ObservableOnSubscribe<T> observableOnSubscribe;
    private Observer<T> observer;

    private Emitter<T> emitter;
    private Future<?> future;

    @Override
    public void run() {
        try {
            observableOnSubscribe.subscribe(emitter);
            // 是否是周期任务
            boolean isInterval = observableOnSubscribe instanceof IntervalOnSubscribe;
            if (!isInterval) {
                emitter.onComplete();
            }
        } catch (Exception e) {
            emitter.onError(e);
        }
    }

    /**
     * 执行被订阅者
     */
    public void executeSubscribe() {
        // 创建分发器
        emitter = new Emitter<>(this);
        // 执行任务
        if (observableOnSubscribe instanceof DelayOnSubscribe) {
            future = SchedulerHelper.getInstance().doOnScheduledScheduler(this,
                    ((DelayOnSubscribe) observableOnSubscribe).getDelay(),
                    ((DelayOnSubscribe) observableOnSubscribe).getTimeUnit());
        } else if (observableOnSubscribe instanceof IntervalOnSubscribe) {
            future = SchedulerHelper.getInstance().doOnScheduledScheduler(this,
                    ((IntervalOnSubscribe) observableOnSubscribe).getDelay(),
                    ((IntervalOnSubscribe) observableOnSubscribe).getPeriod(),
                    ((IntervalOnSubscribe) observableOnSubscribe).getTimeUnit());
        } else {
            future = SchedulerHelper.getInstance().doOnThreadScheduler(this);
        }
    }

    /**
     * 执行执行订阅者
     */
    public void executeObserver(Runnable runnable) {
        if (!isCancel) {
            SchedulerHelper.getInstance().doOnMainScheduler(runnable);
        }
    }

    /**
     * 判断是否解除订阅
     *
     * @return
     */
    public boolean isCancel() {
        return isCancel;
    }

    /**
     * 解除订阅，并试图关闭订阅事件
     */
    public void cancel() {
        isCancel = true;
        observer = null;
        if (future != null) {
            future.cancel(true);
        }
    }

    public ObservableOnSubscribe<T> getObservableOnSubscribe() {
        return observableOnSubscribe;
    }

    public void setObservableOnSubscribe(ObservableOnSubscribe<T> observableOnSubscribe) {
        this.observableOnSubscribe = observableOnSubscribe;
    }

    public Observer<T> getObserver() {
        return observer;
    }

    public void setObserver(Observer<T> observer) {
        this.observer = observer;
    }
}
package com.chillingvan.lib.muxer;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Chilling on 2017/6/10.
 */

public class FramePool {

    private final ItemsPool<Frame> frameItemsPool;

    public FramePool(int poolSize) {
        frameItemsPool = new ItemsPool<>(poolSize);
    }

    public Frame obtain(byte[] data, int offset, int length, BufferInfoEx bufferInfo, int type) {
        Frame frame = frameItemsPool.acquire();
        if (frame == null) {
            frame = new Frame(data, offset, length, bufferInfo, type);
        } else {
            frame.set(data, offset, length, bufferInfo, type);
        }
        return frame;
    }

    public void release(Frame frame) {
        frameItemsPool.release(frame);
    }

    private static class ItemsPool<T> {

        private final Object[] mPool;

        private int mPoolSize;

        /**
         * Creates a new instance.
         *
         * @param maxPoolSize The max pool size.
         * @throws IllegalArgumentException If the max pool size is less than zero.
         */
        public ItemsPool(int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            mPool = new Object[maxPoolSize];
        }

        @SuppressWarnings("unchecked")
        public T acquire() {
            if (mPoolSize > 0) {
                final int lastPooledIndex = mPoolSize - 1;
                T instance = (T) mPool[lastPooledIndex];
                mPool[lastPooledIndex] = null;
                mPoolSize--;
                return instance;
            }
            return null;
        }

        public boolean release(T instance) {
            if (isInPool(instance)) {
                return false;
            }
            if (mPoolSize < mPool.length) {
                mPool[mPoolSize] = instance;
                mPoolSize++;
                return true;
            }
            return false;
        }

        private boolean isInPool(T instance) {
            for (int i = 0; i < mPoolSize; i++) {
                if (mPool[i] == instance) {
                    return true;
                }
            }
            return false;
        }

    }



    public static class Frame {
        public byte[] data;
        public int length;
        public BufferInfoEx bufferInfo;
        public int type;
        public static final int TYPE_VIDEO = 1;
        public static final int TYPE_AUDIO = 2;

        public Frame(byte[] data, int offset, int length, BufferInfoEx bufferInfo, int type) {
            this.data = new byte[length];
            init(data, offset, length, bufferInfo, type);
        }

        public void set(byte[] data, int offset, int length, BufferInfoEx bufferInfo, int type) {
            if (this.data.length < length) {
                this.data = new byte[length];
            }
            init(data, offset, length, bufferInfo, type);
        }

        private void init(byte[] data, int offset, int length, BufferInfoEx bufferInfo, int type) {
            System.arraycopy(data, offset, this.data, 0, length);
            this.length = length;
            this.bufferInfo = bufferInfo;
            this.type = type;
            bufferInfo.getBufferInfo().size = length;
            bufferInfo.getBufferInfo().offset = 0;
            bufferInfo.getBufferInfo().presentationTimeUs = bufferInfo.getTotalTime() * 1000;
        }

        public static void sortFrame(List<Frame> frameQueue) {
            Collections.sort(frameQueue, new Comparator<Frame>() {
                @Override
                public int compare(Frame left, Frame right) {
                    if (left.bufferInfo.getTotalTime() < right.bufferInfo.getTotalTime()) {
                        return -1;
                    } else if (left.bufferInfo.getTotalTime() == right.bufferInfo.getTotalTime()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }
}

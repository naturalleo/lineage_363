package com.lineage.server.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * org.w3c.dom.NodeListにIterableを付加するためのアダプタ。
 */
// 标准ライブラリに同じものが用意されているようなら置换してください。
public class IterableNodeList implements Iterable<Node> {
    private final NodeList _list;

    private class MyIterator implements Iterator<Node> {
        private int _idx = 0;

        @Override
        public boolean hasNext() {
            return this._idx < IterableNodeList.this._list.getLength();
        }

        @Override
        public Node next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return IterableNodeList.this._list.item(this._idx++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public IterableNodeList(final NodeList list) {
        this._list = list;
    }

    @Override
    public Iterator<Node> iterator() {
        return new MyIterator();
    }

}
